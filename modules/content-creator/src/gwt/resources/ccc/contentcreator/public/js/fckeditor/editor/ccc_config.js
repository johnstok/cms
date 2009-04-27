//ccc_config.js - custom configuration file
//see fckconfig.js - FCKConfig.CustomConfigurationsPath

FCKConfig.Plugins.Add( 'cccimage', 'en' ) ;
FCKConfig.Plugins.Add( 'ccclink', 'en' ) ;

// Stripped down toolbar for CCC
FCKConfig.ToolbarSets["ccc"] = [
        ['Source','FitWindow'],
        ['Cut','Copy','Paste','PasteText','PasteWord','-'],
        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        '/',
        ['FontFormat','Bold','Italic','Underline','StrikeThrough','-'],
        ['Anchor','Table','SpecialChar','CCC_Image','CCC_Link']
] ;

FCKConfig.EnterMode = 'p' ;

FCKConfig.LinkBrowser = false ;
FCKConfig.ImageBrowser = false ;
FCKConfig.FlashUpload = false ;
FCKConfig.ImageUpload = false ;
FCKConfig.LinkUpload = false ;
FCKConfig.FlashBrowser = false ;