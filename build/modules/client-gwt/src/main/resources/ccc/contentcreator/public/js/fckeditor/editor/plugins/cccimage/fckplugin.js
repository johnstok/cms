// Plugin for image selector.
        
var ImageSelectCommand=function(){
	//create our own command, we dont want to use the FCKDialogCommand because it uses the default fck layout and not our own
};
ImageSelectCommand.GetState=function() {
	return FCK_TRISTATE_OFF; //we dont want the button to be toggled
}
ImageSelectCommand.Execute=function() {
	//open a popup window when the button is clicked
	var fckname = FCKURLParams['InstanceName'] ;

	parent.cccImageSelector(fckname);
}
FCKCommands.RegisterCommand('CCC_Image', ImageSelectCommand );
        
// Create the toolbar button.
var oCCCImageItem = new FCKToolbarButton('CCC_Image', FCKLang['CCCImageBtn']);
oCCCImageItem.IconPath = FCKPlugins.Items['cccimage'].Path + 'cccimage.gif';

// 'CCC_Image' is the name used in the Toolbar config.
FCKToolbarItems.RegisterItem('CCC_Image', oCCCImageItem);
