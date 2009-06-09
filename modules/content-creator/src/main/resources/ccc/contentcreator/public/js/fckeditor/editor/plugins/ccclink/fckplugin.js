// Plugin for link selector.
        
var LinkSelectCommand=function(){
//create our own command, we dont want to use the FCKDialogCommand because it uses the default fck layout and not our own
};
LinkSelectCommand.GetState=function() {
return FCK_TRISTATE_OFF; //we dont want the button to be toggled
}
LinkSelectCommand.Execute=function() {
//open a popup window when the button is clicked
window.open('/ContentCreator.jsp?browse=link', 'CCC_Link', 'width=640,height=480,scrollbars=no,scrolling=no,location=no,toolbar=no');
}
FCKCommands.RegisterCommand('CCC_Link', LinkSelectCommand );
        
// Create the toolbar button.
var oCCCLinkItem = new FCKToolbarButton('CCC_Link', FCKLang['CCCLinkBtn']);
oCCCLinkItem.IconPath = FCKPlugins.Items['ccclink'].Path + 'ccclink.gif';

// 'CCC_Link' is the name used in the Toolbar config.
FCKToolbarItems.RegisterItem('CCC_Link', oCCCLinkItem);
