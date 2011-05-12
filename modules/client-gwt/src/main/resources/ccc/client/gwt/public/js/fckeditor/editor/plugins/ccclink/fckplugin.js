//Plugin for link selector.

// get HTML from selection
function getSelectionHTML(selection1)
{
   var range = (document.all ? selection1.createRange() : selection1.getRangeAt(selection1.rangeCount - 1).cloneRange());

   if (document.all) {
      return range.htmlText;
   } else {
      var clonedSelection = range.cloneContents();
      var div = document.createElement('div');
      div.appendChild(clonedSelection);
      return div.innerHTML;
   }
}


var LinkSelectCommand=function(){
    //create our own command, we dont want to use the FCKDialogCommand because it uses the default fck layout and not our own
};
LinkSelectCommand.GetState=function() {
    return FCK_TRISTATE_OFF; //we dont want the button to be toggled
}
LinkSelectCommand.Execute=function() {
    // open a popup window when the button is clicked

    var fckname = FCKURLParams['InstanceName'] ;
    var editor = FCKeditorAPI.GetInstance(fckname) ;
    var selection = editor.Selection;
   
    var url = "";
    var title = "";
    var innerText = "";
    var openInNew = false;
    var cccId = "";
    
    var ieImage = false;
    
    // IE specific image selection
	if (selection.GetType() === "Control" &&
	  editor.Selection.GetSelection().createRange){ 
		var oControlRange = editor.Selection.GetSelection().createRange();
    	for (i = 0; i < oControlRange.length; i++) {
	      	if (oControlRange(i).tagName == "IMG") {
				ieImage = true;
		    	var  selectedUrl =  oControlRange(i).getAttribute( 'src' , 2 ) || '' ;
        		var alt = oControlRange(i).alt;
       			var  title = oControlRange(i).title;
       			innerText = '<img title="'+title+'" alt="'+alt+'" src="'+selectedUrl+'"/>';
		    }
        }
    }


	var selectionHTML = (editor.EditorWindow.getSelection ? editor.EditorWindow.getSelection() : editor.EditorDocument.selection);
	
    if (selection.HasAncestorNode('A')) {
        var oldLink = selection.MoveToAncestorNode( 'A' ) ;
        url =  oldLink.getAttribute( 'href' , 2 ) || '' ;
        title = oldLink.title;
        if(!ieImage) {
        	innerText = oldLink.firstChild.nodeValue;
		}        
        if (oldLink.target == "_blank") {
            openInNew = true;
        }
        cccId =  oldLink.getAttribute( 'class' , 2 ) || '' ;
    } 

    if (!ieImage) {
        if (selection.HasAncestorNode('IMG')) {
       innerText = getSelectionHTML(selectionHTML);
	    } 
	    if (innerText == "") {
	       innerText = getSelectionHTML(selectionHTML);
	    }
    }

    parent.cccLinkSelector(fckname, url, title, innerText, cccId, openInNew);
}
FCKCommands.RegisterCommand('CCC_Link', LinkSelectCommand );

//Create the toolbar button.
var oCCCLinkItem = new FCKToolbarButton('CCC_Link', FCKLang['CCCLinkBtn']);
oCCCLinkItem.IconPath = FCKPlugins.Items['ccclink'].Path + 'ccclink.gif';

//'CCC_Link' is the name used in the Toolbar config.
FCKToolbarItems.RegisterItem('CCC_Link', oCCCLinkItem);
