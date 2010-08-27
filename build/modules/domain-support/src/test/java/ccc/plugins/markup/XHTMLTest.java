/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.markup;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;


/**
 * Tests for the {@link XHTML} class.
 *
 * TODO: Add tests for printErrors().
 * TODO: Add sanitization tests from the Antisamy project?
 *
 * @author Civic Computing Ltd
 */
public final class XHTMLTest extends TestCase {

    /**
     * Test.
     */
    public void testSanitizeUrl() {

        // ARRANGE
        final String[] badUrls = {
            "javascript:alert('XSS');",
            "JaVaScRiPt:alert('XSS')",
            "jav\tascript:alert('XSS');",
            "java\u0000script:alert(\"XSS\")",
            "//ha.ckers.org/.j",
            "vbscript:msgbox(\"XSS\")",
            "http://;URL=javascript:alert('XSS');",
            "data:image/svg+xml;base64,PHN2ZyB4bWxuczpzdmc9=="
        };

        // ACT
        for (final String badUrl : badUrls) {
            final String sanitized = new XHTML().sanitizeUrl(badUrl);

        // ASSERT
            assertEquals("", sanitized);
        }
    }

    /**
     * Test.
     */
    public void testXssExamplesSanitized() {

        // ARRANGE

        // ACT
        for (final String[] example : XSS_EXAMPLES) {

        // ASSERT
            assertEquals(example[0], new XHTML().sanitize(example[1]));
        }
    }

    /**
     * Test.
     */
    public void testSanitize() {

        // ARRANGE
        final String raw =
            "<html>"
            + "<head><title>title</title></head>"
            + "<body><p><b>Hello</B> <I>World</i></p><a>foo</a></body>"
            + "</html>";

        // ACT
        final String sanitized = new XHTML().sanitize(raw);

        // ASSERT
        assertEquals("<p><b>Hello</b> <i>World</i></p>foo", sanitized);
    }

    /**
     * Test.
     */
    public void testSanitizeMarkupWithHeadContent() {

        // ARRANGE
        final String raw =
            "<head>foo<script>bad();</script> <a>head-link</head><body />";

        // ACT
        final String sanitized = new XHTML().sanitize(raw);

        // ASSERT
        assertEquals("foo head-link", sanitized);
    }

    /**
     * Test.
     */
    public void testSanitizeHandlesEscapedContent() {

        // ARRANGE
        final String raw = "&lt;foo&gt;bar&lt;/foo&gt;";

        // ACT
        final String sanitized = new XHTML().sanitize(raw);

        // ASSERT
        assertEquals("&lt;foo&gt;bar&lt;/foo&gt;", sanitized);
    }

    /**
     * Test.
     */
    public void testEscape() {

        // ARRANGE
        final String badString = "<p id=\"e&m\">";

        // ACT
        final String encodedString = new XHTML().escape(badString);

        // ASSERT
        assertEquals("&lt;p id=&quot;e&amp;m&quot;&gt;", encodedString);
    }

    /**
     * Test.
     *
     * @throws IOException If the server cannot be reached.
     */
    public void testIsValid() throws IOException {

        // ARRANGE
        final InputStream page =
            getClass()
                .getResource("minimal-strict-document.xhtml")
                .openStream();

        // ACT
        final boolean isValid = new XHTML().isValid(page);

        // ASSERT
        assertTrue("isValid found a valid document to be invalid.", isValid);
    }

    /**
     * Test.
     *
     * @throws IOException If the server cannot be reached.
     */
    public void testEvaluateXpath() throws IOException {

        // ARRANGE
        final InputStream page =
            getClass()
                .getResource("minimal-strict-document.xhtml")
                .openStream();

        // ACT
        final String titleCount =
            new XHTML().evaluateXPath(page, "count(//xhtml:title)");

        // ASSERT
        assertEquals("1", titleCount);
    }

    /**
     * XSS examples from
     * <a href="http://ha.ckers.org/xss.html">ha.ckers.org</a>.
     */
    private static final String[][] XSS_EXAMPLES = {
        // TODO: Example 1.
        {"'';!--&quot;", "'';!--\"<XSS>=&{()}"}, // FIXME: Dodgy?
        {"", "<SCRIPT SRC=http://ha.ckers.org/xss.js></SCRIPT>"},
        {"", "<IMG SRC=\"javascript:alert('XSS');\">"},
        {"", "<IMG SRC=javascript:alert('XSS')>"},
        {"", "<IMG SRC=JaVaScRiPt:alert('XSS')>"},
        {"", "<IMG SRC=javascript:alert(&quot;XSS&quot;)>"},
        {"", "<IMG SRC=`javascript:alert(\"RSnake says, 'XSS'\")`>"},
        {"&quot;&gt;", "<IMG \"\"\"><SCRIPT>alert(\"XSS\")</SCRIPT>\">"}, // FIXME: Dodgy?
        {"", "<IMG SRC=javascript:alert(String.fromCharCode(88,83,83))>"},
        {"", "<IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>"},
        {"", "<IMG SRC=&#0000106&#0000097&#0000118&#0000097&#0000115&#0000099&#0000114&#0000105&#0000112&#0000116&#0000058&#0000097&#0000108&#0000101&#0000114&#0000116&#0000040&#0000039&#0000088&#0000083&#0000083&#0000039&#0000041>"},
        {"", "<IMG SRC=&#x6A&#x61&#x76&#x61&#x73&#x63&#x72&#x69&#x70&#x74&#x3A&#x61&#x6C&#x65&#x72&#x74&#x28&#x27&#x58&#x53&#x53&#x27&#x29>"},
        {"", "<IMG SRC=\"jav\tascript:alert('XSS');\">"},
        {"", "<IMG SRC=\"jav&#x09;ascript:alert('XSS');\">"},
        {"", "<IMG SRC=\"jav&#x0A;ascript:alert('XSS');\">"},
        {"", "<IMG SRC=\"jav&#x0D;ascript:alert('XSS');\">"},
        {"", "<IMG\nSRC\n=\n\"\nj\na\nv\na\ns\nc\nr\ni\np\nt\n:\na\nl\ne\nr\nt\n(\n'\nX\nS\nS\n'\n)\n\"\n>"},
        {"", "<IMG SRC=java\u0000script:alert(\"XSS\")>"},
        {"", "<SCR\u0000IPT>alert(\"XSS\")</SCR\u0000IPT>"},
        {"", "<IMG SRC=\" &#14;  javascript:alert('XSS');\">"},
        {"", "<SCRIPT/XSS SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"", "<BODY onload!#$%&()*~+-_.,:;?@[/|\\]^`=alert(\"XSS\")>"},
        {"", "<SCRIPT/SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"", "<<SCRIPT>alert(\"XSS\");//<</SCRIPT>"},
        {"", "<SCRIPT SRC=http://ha.ckers.org/xss.js?<B>"},
        {"", "<SCRIPT SRC=//ha.ckers.org/.j>"},
        {"", "<IMG SRC=\"javascript:alert('XSS')\""},
        {"", "<iframe src=http://ha.ckers.org/scriptlet.html <"},
        {"", "<SCRIPT>alert(/XSS/.source)</SCRIPT>"},
        {"\\&quot;;alert('XSS');//", "\\\";alert('XSS');//"}, // FIXME: Dodgy?
        {"", "</TITLE><SCRIPT>alert(\"XSS\");</SCRIPT>"},
        {"", "<INPUT TYPE=\"IMAGE\" SRC=\"javascript:alert('XSS');\">"},
        {"", "<BODY BACKGROUND=\"javascript:alert('XSS')\">"},
        {"", "<BODY ONLOAD=alert('XSS')>"},
        {"", "<IMG DYNSRC=\"javascript:alert('XSS')\">"},
        {"", "<IMG LOWSRC=\"javascript:alert('XSS')\">"},
        {"", "<BGSOUND SRC=\"javascript:alert('XSS');\">"},
        {"<br></br>", "<BR SIZE=\"&{alert('XSS')}\">"},
        {"", "<LAYER SRC=\"http://ha.ckers.org/scriptlet.html\"></LAYER>"},
        {"", "<LINK REL=\"stylesheet\" HREF=\"javascript:alert('XSS');\">"},
        {"", "<LINK REL=\"stylesheet\" HREF=\"http://ha.ckers.org/xss.css\">"},
        {"", "<STYLE>@import'http://ha.ckers.org/xss.css';</STYLE>"},
        {"", "<META HTTP-EQUIV=\"Link\" Content=\"<http://ha.ckers.org/xss.css>; REL=stylesheet\">"},
        {"", "<STYLE>BODY{-moz-binding:url(\"http://ha.ckers.org/xssmoz.xml#xss\")}</STYLE>"},
        {"", "<XSS STYLE=\"behavior: url(xss.htc);\">"},
        {"", "<STYLE>li {list-style-image: url(\"javascript:alert('XSS')\");}</STYLE><UL><LI>XSS"},
        {"", "<IMG SRC='vbscript:msgbox(\"XSS\")'>"},
        {"", "<IMG SRC=\"mocha:[code]\">"},
        {"", "<IMG SRC=\"livescript:[code]\">"},
        {"žscriptualert(EXSSE)ž/scriptu", "žscriptualert(EXSSE)ž/scriptu"}, // TODO: http://ha.ckers.org/charsets.html
        {"", "<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;url=javascript:alert('XSS');\">"},
        {"", "<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;url=data:text/html;base64,PHNjcmlwdD5hbGVydCgnWFNTJyk8L3NjcmlwdD4K\">"},
        {"", "<META HTTP-EQUIV=\"refresh\" CONTENT=\"0; URL=http://;URL=javascript:alert('XSS');\">"},
        {"", "<IFRAME SRC=\"javascript:alert('XSS');\"></IFRAME>"},
        {"", "<FRAMESET><FRAME SRC=\"javascript:alert('XSS');\"></FRAMESET>"},
        {"", "<TABLE BACKGROUND=\"javascript:alert('XSS')\">"},
        {"", "<TABLE><TD BACKGROUND=\"javascript:alert('XSS')\">"},
        {"", "<DIV STYLE=\"background-image: url(javascript:alert('XSS'))\">"},
        {"", "<DIV STYLE=\"background-image:\0075\0072\006C\0028'\006a\0061\0076\0061\0073\0063\0072\0069\0070\0074\003a\0061\006c\0065\0072\0074\0028.1027\0058.1053\0053\0027\0029'\0029\">"},
        {"", "<DIV STYLE=\"background-image: url(&#1;javascript:alert('XSS'))\">"},
        {"", "<DIV STYLE=\"width: expression(alert('XSS'));\">"},
        {"", "<STYLE>@im\\port'\\ja\\vasc\\ript:alert(\"XSS\")';</STYLE>"},
        {"", "<IMG STYLE=\"xss:expr/*XSS*/ession(alert('XSS'))\">"},
        {"", "<XSS STYLE=\"xss:expression(alert('XSS'))\">"},
        {"exp/*", "exp/*<A STYLE='no\\xss:noxss(\"*//*\");\nxss:&#101;x&#x2F;*XSS*//*/*/pression(alert(\"XSS\"))'>"},
        {"", "<STYLE TYPE=\"text/javascript\">alert('XSS');</STYLE>"},
        {"", "<STYLE>.XSS{background-image:url(\"javascript:alert('XSS')\");}</STYLE><A CLASS=XSS></A>"},
        {"", "<STYLE type=\"text/css\">BODY{background:url(\"javascript:alert('XSS')\")}</STYLE>"},
        {"", "<!--[if gte IE 4]>\n<SCRIPT>alert('XSS');</SCRIPT>\n<![endif]-->"},
        {"", "<BASE HREF=\"javascript:alert('XSS');//\">"},
        {"", "<OBJECT TYPE=\"text/x-scriptlet\" DATA=\"http://ha.ckers.org/scriptlet.html\"></OBJECT>"},
        {"", "<OBJECT classid=clsid:ae24fdae-03c6-11d1-8b76-0080c744f389><param name=url value=javascript:alert('XSS')></OBJECT>"},
        {"", "<EMBED SRC=\"http://ha.ckers.org/xss.swf\" AllowScriptAccess=\"always\"></EMBED>"},
        {"", "<EMBED SRC=\"data:image/svg+xml;base64,PHN2ZyB4bWxuczpzdmc9Imh0dH A6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcv MjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hs aW5rIiB2ZXJzaW9uPSIxLjAiIHg9IjAiIHk9IjAiIHdpZHRoPSIxOTQiIGhlaWdodD0iMjAw IiBpZD0ieHNzIj48c2NyaXB0IHR5cGU9InRleHQvZWNtYXNjcmlwdCI+YWxlcnQoIlh TUyIpOzwvc2NyaXB0Pjwvc3ZnPg==\" type=\"image/svg+xml\" AllowScriptAccess=\"always\"></EMBED>"},
        {"", "<HTML xmlns:xss><?import namespace=\"xss\" implementation=\"http://ha.ckers.org/xss.htc\"><xss:xss>XSS</xss:xss></HTML>"},
        {"", "<XML ID=I><X><C><![CDATA[<IMG SRC=\"javas]]><![CDATA[cript:alert('XSS');\">]]></C></X></xml><SPAN DATASRC=#I DATAFLD=C DATAFORMATAS=HTML></SPAN>"},
        {"", "<XML ID=\"xss\"><I><B>&lt;IMG SRC=\"javas<!-- -->cript:alert('XSS')\"&gt;</B></I></XML><SPAN DATASRC=\"#xss\" DATAFLD=\"B\" DATAFORMATAS=\"HTML\"></SPAN>"},
        {"", "<XML SRC=\"xsstest.xml\" ID=I></XML><SPAN DATASRC=#I DATAFLD=C DATAFORMATAS=HTML></SPAN>"},
        {"", "<HTML><BODY><?xml:namespace prefix=\"t\" ns=\"urn:schemas-microsoft-com:time\"><?import namespace=\"t\" implementation=\"#default#time2\"><t:set attributeName=\"innerHTML\" to=\"XSS&lt;SCRIPT DEFER&gt;alert(&quot;XSS&quot;)&lt;/SCRIPT&gt;\"></BODY></HTML>"},
        {"", "<SCRIPT SRC=\"http://ha.ckers.org/xss.jpg\"></SCRIPT>"},
        {"", "<META HTTP-EQUIV=\"Set-Cookie\" Content=\"USERID=&lt;SCRIPT&gt;alert('XSS')&lt;/SCRIPT&gt;\">"},
        {"+ADw-SCRIPT+AD4-alert('XSS');+ADw-/SCRIPT+AD4-", "<HEAD><META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=UTF-7\"></HEAD>+ADw-SCRIPT+AD4-alert('XSS');+ADw-/SCRIPT+AD4-"},
        {"", "<SCRIPT a=\">\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"", "<SCRIPT =\">\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"", "<SCRIPT a=\">\" '' SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"", "<SCRIPT \"a='>'\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"", "<SCRIPT a=`>` SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"", "<SCRIPT a=\">'>\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"PT SRC=&quot;http://ha.ckers.org/xss.js&quot;&gt;", "<SCRIPT>document.write(\"<SCRI\");</SCRIPT>PT SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>"},
        {"XSS", "<A HREF=\"http://%77%77%77%2E%67%6F%6F%67%6C%65%2E%63%6F%6D\">XSS</A>"},
        {"XSS", "<A HREF=\"http://1113982867/\">XSS</A>"},
        {"XSS", "<A HREF=\"http://0x42.0x0000066.0x7.0x93/\">XSS</A>"},
        {"XSS", "<A HREF=\"http://0102.0146.0007.00000223/\">XSS</A>"},
        {"XSS", "<A HREF=\"htt\tp://6&#9;6.000146.0x7.147/\">XSS</A>"},
        {"XSS", "<A HREF=\"//www.google.com/\">XSS</A>"},
        {"XSS", "<A HREF=\"//google\">XSS</A>"},
        {"XSS", "<A HREF=\"http://ha.ckers.org@google\">XSS</A>"},
        {"XSS", "<A HREF=\"http://google:ha.ckers.org\">XSS</A>"},
        {"XSS", "<A HREF=\"http://google.com/\">XSS</A>"},
        {"XSS", "<A HREF=\"http://www.google.com./\">XSS</A>"},
        {"XSS", "<A HREF=\"javascript:document.location='http://www.google.com/'\">XSS</A>"},
    };

    public static void main(final String[] args) {
        System.out.print(
            new XHTML().fix(
                "<head>foo <a>head-link<script>bad();</script></head><body />")
        );
    }
}
