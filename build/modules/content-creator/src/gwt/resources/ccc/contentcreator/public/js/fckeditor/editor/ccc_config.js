//ccc_config.js - custom configuration file
//see fckconfig.js - FCKConfig.CustomConfigurationsPath

FCKConfig.Plugins.Add( 'cccimage', 'en' ) ;


// Stripped down toolbar for CCC
FCKConfig.ToolbarSets["ccc"] = [
        ['Source','FitWindow','-','Preview'],
        ['Cut','Copy','Paste','PasteText','PasteWord','-'],
        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        '/',
        ['FontFormat','Bold','Italic','Underline','StrikeThrough','-','Subscript','Superscript'],
        ['Link','Unlink','Anchor'],
        ['Table','SpecialChar','CCC_Image']
] ;

FCKConfig.EnterMode = 'p' ;

FCKConfig.LinkBrowser = false ;
FCKConfig.LinkBrowserURL = '/ContentCreator.html?browse=link' ;
FCKConfig.LinkBrowserWindowWidth	= FCKConfig.ScreenWidth * 0.7 ;		// 70%
FCKConfig.LinkBrowserWindowHeight	= FCKConfig.ScreenHeight * 0.7 ;	// 70%

FCKConfig.ImageBrowser = false ;
FCKConfig.FlashUpload = false ;
FCKConfig.ImageUpload = false ;
FCKConfig.LinkUpload = false ;
FCKConfig.FlashBrowser = false ;