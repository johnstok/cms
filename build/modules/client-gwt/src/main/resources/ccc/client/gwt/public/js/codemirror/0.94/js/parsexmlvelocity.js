// we're going to count parens in and out to help determine line ending in velocity since it has no terminators
var velocityparens = 0;
var velocityparensinit = false;

/* This file defines an XML parser, with a few kludges to make it
 * useable for HTML. autoSelfClosers defines a set of tag names that
 * are expected to not have a closing tag, and doNotIndent specifies
 * the tags inside of which no indentation should happen (see Config
 * object). These can be disabled by passing the editor an object like
 * {useHTMLKludges: false} as parserConfig option.
 */

var XMLParser = Editor.Parser = (function() {
  var Kludges = {
    autoSelfClosers: {"br": true, "img": true, "hr": true, "link": true, "input": true,
                      "meta": true, "col": true, "frame": true, "base": true, "area": true},
    doNotIndent: {"pre": true, "!cdata": true}
  };
  var NoKludges = {autoSelfClosers: {}, doNotIndent: {"!cdata": true}};
  var UseKludges = Kludges;
  var alignCDATA = false;
  
  function wordRegexp(words) {
    return new RegExp("^(?:" + words.join("|") + ")$", "i");
  }
  var directives = wordRegexp(["#foreach", "#if", "#else", "#elseif", "#end", "#set", '#parse', '#include', '#stop', '#break', '#evaluate', '#define', '#macro']);
  
  var keywords = wordRegexp(["in","while"]);

  // Simple stateful tokenizer for XML documents. Returns a
  // MochiKit-style iterator, with a state property that contains a
  // function encapsulating the current state. See tokenize.js.
  var tokenizeXML = (function() {
    function inText(source, setState) {
      var ch = source.next();
  
      if (ch == "<") {
		if (source.equals("!")) {
          source.next();
          if (source.equals("[")) {
            if (source.lookAhead("[CDATA[", true)) {
              setState(inBlock("xml-cdata", "]]>"));
              return null;
            }
            else {
              return "xml-text";
            }
          }
          else if (source.lookAhead("--", true)) {
            setState(inBlock("xml-comment", "-->"));
            return null;
          }
          else if (source.lookAhead("DOCTYPE", true)) {
            source.nextWhileMatches(/[\w\._\-]/);
            setState(inBlock("xml-doctype", ">"));
            return "xml-doctype";
          }
          else {
            return "xml-text";
          }
        }
        else if (source.equals("?")) {
          source.next();
          source.nextWhileMatches(/[\w\._\-]/);
          setState(inBlock("xml-processing", "?>"));
          return "xml-processing";
        }
        else {
          if (source.equals("/")) source.next();
          setState(inTag);
          return "xml-punctuation";
        }
      }
      else if (ch == "&") {
        while (!source.endOfLine()) {
          if (source.next() == ";")
            break;
        }
        return "xml-entity";
      }
	  else if (ch == "$") {
		setState(inVelocity)
		source.nextWhileMatches(/\w/);
		return "vel-object";
	  }
	  else if (ch == "#") {
		  if(source.equals("#")) {
			setState(inVelSingleComment);
			return null;
		  }
		  else if (source.equals("*")) {
	      	setState(inVelMultiComment);
			return null;
		  }
		  else {
			setState(inVelocity);
	        return null;
		  }
	  }
	  else {
        source.nextWhileMatches(/[^&<\n]/);
        return "xml-text";
      }
    }
	
    function inVelocity(source, setState) {
	  if (source.endOfLine()) {
		  setState(inText);return 'xml-text';
	  }
	  if (source.peek() == "<") {
		  if(source.lookAheadRegex(/^<[\w\/]/,false)) {setState(inText);return 'xml-text';} 
	  }
	  
	  var returnVal = null;
      var ch = source.next();
	  
	  if (ch == "<" && source.equals('/')) {
		  setState(inText);return 'xml-text';
	  }

	  if (ch == "$") {
		source.nextWhileMatches(/\w/);
		returnVal = "vel-object";
      }
	  else if (/\d/.test(ch)) {
		  source.nextWhileMatches(/\d/);
		  returnVal = "vel-number";
	  }
	  else if (/[()=\.,<>]/.test(ch)) {
		  if (ch == "(") {velocityparens++;velocityparensinit=true;}
		  if (ch == ")") velocityparens--;
		  if (velocityparens==0&&velocityparensinit) {
			if (source.peek() != ".") setState(inText);
			velocityparensinit=false;
		  }
		  returnVal = "vel-punctuation";
	  }
      else if (/[\'\"]/.test(ch)) {
        setState(inVelocityAttribute(ch));
        returnVal = null;
      }
      else {
		source.nextWhileMatches(/[_\-\w\d]/);
		var word = source.get(), type;
		if (directives.test(word))
			type = "vel-directive";
		else if (word == "null")
			type = "vel-null";
		else if (keywords.test(word))
			type = "vel-keyword";
		else
			type = "vel-word";
		returnVal = {style: type, content: word};
      }
	  return returnVal;
    }
	
	function inVelMultiComment(source, setState) {
		var maybeEnd = false;
		while (!source.endOfLine()) {
			var ch = source.next();
			if (maybeEnd && ch == "#") {
				setState(inText);
				break;
			}
			maybeEnd = (ch == "*");
		}
		return "vel-comment";
	}
	
	function inVelSingleComment(source, setState) {
		var maybeEnd = false;
		while (!source.endOfLine()) {
			var ch = source.next();
		}
		setState(inText);
		return "vel-comment";
	}

	function inVelocityAttribute(quote) {
		return function(source, setState) {
        while (!source.endOfLine()) {
          if (source.next() == quote) {
            setState(inVelocity);
            break;
          }
        }
        return "vel-attribute";
      };
	}
	
	function inVelocityObject(quote) {
        return "vel-attribute";

	}
	
	function inTag(source, setState) {
      var ch = source.next();
      if (ch == ">") {
        setState(inText);
        return "xml-punctuation";
      }
      else if (/[?\/]/.test(ch) && source.equals(">")) {
        source.next();
        setState(inText);
        return "xml-punctuation";
      }
      else if (ch == "=") {
        return "xml-punctuation";
      }
      else if (/[\'\"]/.test(ch)) {
        setState(inAttribute(ch));
        return null;
      }
      else {
		source.nextWhileMatches(/[^\s\u00a0=<>\"\'\/?]/);
        return "xml-name";
      }
    }

    function inAttribute(quote) {
      return function(source, setState) {
        while (!source.endOfLine()) {
          if (source.next() == quote) {
            setState(inTag);
            break;
          }
        }
        return "xml-attribute";
      };
    }

    function inBlock(style, terminator) {
      return function(source, setState) {
        while (!source.endOfLine()) {
          if (source.lookAhead(terminator, true)) {
            setState(inText);
            break;
          }
          source.next();
        }
        return style;
      };
    }

    return function(source, startState) {
      return tokenizer(source, startState || inText);
    };
  })();

  // The parser. The structure of this function largely follows that of
  // parseJavaScript in parsejavascript.js (there is actually a bit more
  // shared code than I'd like), but it is quite a bit simpler.
  function parseXML(source) {
    var tokens = tokenizeXML(source), token;
    var cc = [base];
    var tokenNr = 0, indented = 0;
    var currentTag = null, context = null;
    var consume;
    
    function push(fs) {
      for (var i = fs.length - 1; i >= 0; i--)
        cc.push(fs[i]);
    }
    function cont() {
      push(arguments);
      consume = true;
    }
    function pass() {
      push(arguments);
      consume = false;
    }

    function markErr() {
      token.style += " xml-error";
    }
    function expect(text) {
      return function(style, content) {
        if (content == text) cont();
        else {markErr();cont(arguments.callee);}
      };
    }

    function pushContext(tagname, startOfLine) {
      var noIndent = UseKludges.doNotIndent.hasOwnProperty(tagname) || (context && context.noIndent);
      context = {prev: context, name: tagname, indent: indented, startOfLine: startOfLine, noIndent: noIndent};
    }
    function popContext() {
      context = context.prev;
    }
    function computeIndentation(baseContext) {
      return function(nextChars, current) {
        var context = baseContext;
        if (context && context.noIndent)
          return current;
        if (alignCDATA && /<!\[CDATA\[/.test(nextChars))
          return 0;
        if (context && /^<\//.test(nextChars))
          context = context.prev;
        while (context && !context.startOfLine)
          context = context.prev;
        if (context)
          return context.indent + indentUnit;
        else
          return 0;
      };
    }

    function base() {
      return pass(element, base);
    }
    var harmlessTokens = {"xml-text": true, "xml-entity": true, "xml-comment": true, "xml-processing": true, "xml-doctype": true, "vel-comment": true, "velocity": true, "vel-attribute": true, "vel-method": true, "vel-keyword": true, "vel-null": true, "vel-number": true, "vel-object": true, "vel-directive": true, "vel-punctuation": true, "vel-word": true};
    function element(style, content) {
      
	  if (content == "<" && !style.match('vel-punctuation')) {
		  cont(tagname, attributes, endtag(tokenNr == 1))
	  }
	  else if (content == "</") cont(closetagname, expect(">"));
	  else if (style == "xml-cdata") {
		if (!context || context.name != "!cdata") pushContext("!cdata");
		if (/\]\]>$/.test(content)) popContext();
		cont();
	  }
	  else if (harmlessTokens.hasOwnProperty(style)) cont();
	  else {markErr(); cont();}
    } 
    function tagname(style, content) {
      if (style == "xml-name") {
        currentTag = content.toLowerCase();
        token.style = "xml-tagname";
        cont();
      }
      else {
        currentTag = null;
        pass();
      }
    }
    function closetagname(style, content) {
      if (style == "xml-name") {
        token.style = "xml-tagname";
        if (context && content.toLowerCase() == context.name) popContext();
        else {
			markErr();}
      }
      cont();
    }
    function endtag(startOfLine) {
      return function(style, content) {
        if (content == "/>" || (content == ">" && UseKludges.autoSelfClosers.hasOwnProperty(currentTag))) cont();
        else if (content == ">") {pushContext(currentTag, startOfLine); cont();}
        else {
			markErr(); cont(arguments.callee);
		}
      };
    }
    function attributes(style) {
      if (style == "xml-name") {token.style = "xml-attname"; cont(attribute, attributes);}
      else pass();
    }
    function attribute(style, content) {
      if (content == "=") cont(value);
      else if (content == ">" || content == "/>") pass(endtag);
      else pass();
    }
    function value(style) {
      if (style == "xml-attribute") cont(value);
      else pass();
    }

    return {
      indentation: function() {return indented;},

      next: function(){
        token = tokens.next();
        if (token.style == "whitespace" && tokenNr == 0)
          indented = token.value.length;
        else
          tokenNr++;
        if (token.content == "\n") {
          indented = tokenNr = 0;
          token.indentation = computeIndentation(context);
        }

        if (token.style == "whitespace" || token.type == "xml-comment")
          return token;

        while(true){
          consume = false;
          cc.pop()(token.style, token.content);
          if (consume) return token;
        }
      },

      copy: function(){
        var _cc = cc.concat([]), _tokenState = tokens.state, _context = context;
        var parser = this;
        
        return function(input){
          cc = _cc.concat([]);
          tokenNr = indented = 0;
          context = _context;
          tokens = tokenizeXML(input, _tokenState);
          return parser;
        };
      }
    };
  }

  return {
    make: parseXML,
    electricChars: "/",
    configure: function(config) {
      if (config.useHTMLKludges != null)
        UseKludges = config.useHTMLKludges ? Kludges : NoKludges;
      if (config.alignCDATA)
        alignCDATA = config.alignCDATA;
    }
  };
})();
