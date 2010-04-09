//Plugin for link selector.

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

    if (selection.HasAncestorNode('A')) {
        var oldLink = selection.MoveToAncestorNode( 'A' ) ;
        url =  oldLink.getAttribute( 'href' , 2 ) || '' ;
        title = oldLink.title;
        innerText = oldLink.innerHTML;
        if (oldLink.target == "_blank") {
            openInNew = true;
        }
        cccId =  oldLink.getAttribute( 'class' , 2 ) || '' ;
    }

    parent.cccLinkSelector(fckname, url, title, innerText, cccId, openInNew);
}
FCKCommands.RegisterCommand('CCC_Link', LinkSelectCommand );

//Create the toolbar button.
var oCCCLinkItem = new FCKToolbarButton('CCC_Link', FCKLang['CCCLinkBtn']);
oCCCLinkItem.IconPath = FCKPlugins.Items['ccclink'].Path + 'ccclink.gif';

//'CCC_Link' is the name used in the Toolbar config.
FCKToolbarItems.RegisterItem('CCC_Link', oCCCLinkItem);
