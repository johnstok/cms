/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import java.util.Collection;
import java.util.List;

import ccc.api.FileSummary;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.FileSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ImageTriggerField;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ListView;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ImageChooserDialog extends AbstractEditDialog {

    private final ImageTriggerField _image;
    private  final ListView<FileSummaryModelData> _view =
        new ListView<FileSummaryModelData>();
    private List<FileSummaryModelData> _models;

    /**
     * Constructor.
     * @param image
     *
     * @param title
     */
    public ImageChooserDialog(ImageTriggerField image) {

        super(Globals.uiConstants().selectImage());

        _image = image;
        final ListStore<FileSummaryModelData> store =
            new ListStore<FileSummaryModelData>();

        queries().getAllContentImages(
            new ErrorReportingCallback<Collection<FileSummary>>(){
                public void onSuccess(final Collection<FileSummary> arg0) {
                    _models = DataBinding.bindFileSummary(arg0);
                    if (_models != null && _models.size() > 0) {
                        store.add(_models);
                    }
                }
            });


        _view.setTemplate(getTemplate());
        _view.setStore(store);
        _view.setItemSelector("div.thumb-wrap");
        addField(_view);

    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override
            public void componentSelected(final ButtonEvent ce) {
                FileSummaryModelData md = _view.getSelectionModel().getSelectedItem();
                if (md != null)
                    _image.setValue(md.getPath());
                    _image.setFSModel(md);
                hide();
            }
        };
    }

    // FIXME: Property names aren't type safe.
    private native String getTemplate() /*-{
    return ['<tpl for=".">',
     '<div class="thumb-wrap" id="{NAME}" style="border: 1px solid white">',
     '<div class="thumb"><img src="{PATH}" title="{TITLE}"></div>',
     '<span class="x-editable">{SHORT_NAME}</span></div>',
     '</tpl>',
     '<div class="x-clear"></div>'].join("");

     }-*/;
}
