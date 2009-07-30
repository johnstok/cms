/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2009-01-06 16:14:14 +0000 (Tue, 06 Jan 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import java.util.Collection;
import java.util.List;

import ccc.api.FileSummary;
import ccc.contentcreator.actions.GetContentImagesAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.FileSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Image selection dialog for FCKEditor use.
 *
 * @author Civic Computing Ltd.
 */
public class ImageSelectionDialog extends AbstractBaseDialog {

    private static final int PANEL_HEIGHT = 460;
    private static final int PANEL_WIDTH = 620;

    private  final ListView<FileSummaryModelData> _view =
        new ListView<FileSummaryModelData>();
    private List<FileSummaryModelData> _models;
    private final String _elementid;


    /**
     * Constructor.
     *
     * @param elementid Element ID for FCKEditor
     */
    public ImageSelectionDialog(final String elementid) {
        super(new IGlobalsImpl().uiConstants().selectImage(),
              new IGlobalsImpl());
        _elementid = elementid;

        final ListStore<FileSummaryModelData> store =
            new ListStore<FileSummaryModelData>();

        new GetContentImagesAction(_constants.selectImage()){
            @Override
            protected void execute(final Collection<FileSummary> images) {
                loadModel(store, images);
            }
        }.execute();


        final ContentPanel panel = new ContentPanel();
        panel.setCollapsible(false);
        panel.setAnimCollapse(false);
        panel.setId("images-view");
        panel.setHeaderVisible(false);
        panel.setWidth(PANEL_WIDTH);
        panel.setHeight(PANEL_HEIGHT);
        panel.setLayout(new FitLayout());
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setBodyStyleName("backgroundColor: white;");

        _view.setBorders(false);
        _view.setTemplate(getTemplate());
        _view.setStore(store);
        _view.setItemSelector("div.thumb-wrap");

        panel.add(_view);
        add(panel);

        addButton(_cancel);
        final Button save = new Button(constants().save(), saveAction());
        addButton(save);
    }

    /** {@inheritDoc} */
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override
            public void componentSelected(final ButtonEvent ce) {
                final FileSummaryModelData md =
                    _view.getSelectionModel().getSelectedItem();
                if (md != null) {
                    jsniSetUrl(
                        md.getPath().substring(1),
                        md.getTitle(),
                        md.getId().toString(),
                        _elementid);
                    hide();
                }
            }
        };
    }


    private static native String jsniSetUrl(final String selectedUrl,
                                            final String title,
                                            final String uuid,
                                            final String elementID) /*-{
     if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.InsertHtml("<img title='"+title+"' alt='"
                +title+"' class='ccc:"+uuid+"' src='"+selectedUrl+"'/>");
            }
        }
        return null;
    }-*/;


    // TODO: Property names aren't type safe.
    private native String getTemplate() /*-{
    return ['<tpl for=".">',
     '<div class="thumb-wrap" id="{NAME}" style="border: 1px solid white">',
     '<div class="thumb"><img src="{PATH}" title="{TITLE}"></div>',
     '<span class="x-editable">{SHORT_NAME}</span></div>',
     '</tpl>',
     '<div class="x-clear"></div>'].join("");

     }-*/;

    private void loadModel(final ListStore<FileSummaryModelData> store,
                           final Collection<FileSummary> arg0) {

        _models = DataBinding.bindFileSummary(arg0);
        if (_models != null && _models.size() > 0) {
            store.add(_models);
        }
    }

}
