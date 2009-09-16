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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.contentcreator.actions.GetContentImagesAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.FileSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ImageTriggerField;
import ccc.rest.dto.FileDto;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Image chooser for image type paragraph fields.
 *
 * @author Civic Computing Ltd.
 */
public class ImageChooserDialog extends AbstractBaseDialog {

    private final ImageTriggerField _image;
    private  final ListView<FileSummaryModelData> _view =
        new ListView<FileSummaryModelData>();
    private List<FileSummaryModelData> _models;

    private static final int PANEL_HEIGHT = 460;
    private static final int PANEL_WIDTH = 620;

    /**
     * Constructor.
     *
     * @param image The trigger field for the image.
     */
    public ImageChooserDialog(final ImageTriggerField image) {

        super(new IGlobalsImpl().uiConstants().selectImage(),
              new IGlobalsImpl());

        _image = image;
        final ListStore<FileSummaryModelData> store =
            new ListStore<FileSummaryModelData>();

        new GetContentImagesAction(getUiConstants().selectImage()){
            @Override
            protected void execute(final Collection<FileDto> images) {
                loadModel(image, store, images);
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

        addButton(getCancel());
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
                    _image.setValue(md.getPath());
                }
                    _image.setFSModel(md);
                hide();
            }
        };
    }

    // TODO: Property names aren't type safe.
    private native String getTemplate() /*-{
    return ['<tpl for=".">',
     '<div class="thumb-wrap" id="{NAME}" style="border: 1px solid white">',
     '<div class="thumb"><img src="{PATH}" title="{TITLE}"></div>',
     '<span class="x-editable">{SHORT_NAME}</span></div>',
     '</tpl>',
     '<div class="x-clear"></div>'].join("");

     }-*/;

    private void loadModel(final ImageTriggerField image,
                           final ListStore<FileSummaryModelData> store,
                           final Collection<FileDto> arg0) {

        _models = DataBinding.bindFileSummary(arg0);
        if (_models != null && _models.size() > 0) {
            store.add(_models);
            final FileSummaryModelData fs = image.getFSModel();

            if (fs != null) {
                final List<FileSummaryModelData> selection =
                    new ArrayList<FileSummaryModelData>();
                for (final FileSummaryModelData item :_models) {
                    if (item.getId().equals(fs.getId())) {
                        selection.add(item);
                    }
                }
                _view.getSelectionModel().setSelection(selection);
            }
        }
    }
}
