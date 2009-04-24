/*
FCKCommands.RegisterCommand(commandName, command)
       commandName - Command name, referenced by the Toolbar, etc...
       command - Command object (must provide an Execute() function).
*/
// Register the related commands.
  
        
var ImageSelectCommand=function(){
//create our own command, we dont want to use the FCKDialogCommand because it uses the default fck layout and not our own
};
ImageSelectCommand.GetState=function() {
return FCK_TRISTATE_OFF; //we dont want the button to be toggled
}
ImageSelectCommand.Execute=function() {
//open a popup window when the button is clicked
window.open('/ContentCreator.html?browse=image', 'CCC_Image', 'width=800,height=500,scrollbars=no,scrolling=no,location=no,toolbar=no');
}
FCKCommands.RegisterCommand('CCC_Image', ImageSelectCommand ); //otherwise our command will not be found
        
        
// Create the toolbar button.
var oCCCLinkItem = new FCKToolbarButton('CCC_Image', FCKLang['CCCImageBtn']);
oCCCLinkItem.IconPath = FCKPlugins.Items['cccimage'].Path + 'cccimage.gif';

// 'CCC_Link' is the name used in the Toolbar config.
FCKToolbarItems.RegisterItem('CCC_Image', oCCCLinkItem);
