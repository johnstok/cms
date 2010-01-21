/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.contentcreator.actions.GetContentImagesAction;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ImageSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.rest.dto.FileDto;
import ccc.types.Paragraph;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

/**
 * Image selection dialog for FCKEditor use.
 *
 * @author Civic Computing Ltd.
 */
public class ImageSelectionDialog extends AbstractBaseDialog {

    private static final int PANEL_HEIGHT = 460;
    private static final int PANEL_WIDTH = 700;
    private static final int DIALOG_WIDTH = 720;

    private  final ListView<ImageSummaryModelData> _view =
        new ListView<ImageSummaryModelData>();
    private List<ImageSummaryModelData> _models;
    private final String _elementid;
    private final UIConstants _constants;
    private String _cccId;

    private final TextField<String> _urlField = new TextField<String>();
    private final TextField<String> _altField = new TextField<String>();
    private final TextField<String> _titleField = new TextField<String>();
    
    /**
     * Constructor.
     *
     * @param elementid Element ID for FCKEditor.
     * @param url The URL of the selected image.
     * @param alt The alternative text for the selected image.
     * @param title The title of the selected image.
     * @param cccId The ccc id stored in class of the image.
     */
    public ImageSelectionDialog(final String elementid, String url, String alt, String title, String cccId) {
        super(new IGlobalsImpl().uiConstants().selectImage(),
              new IGlobalsImpl());
        _constants = new IGlobalsImpl().uiConstants();
        setLayout(new RowLayout());
        _elementid = elementid;
        _cccId = cccId;

        setWidth(DIALOG_WIDTH);
        setFrame(true);
        final ListStore<ImageSummaryModelData> store =
            new ListStore<ImageSummaryModelData>();

        new GetContentImagesAction(getUiConstants().selectImage()){
            @Override
            protected void execute(final Collection<FileDto> images) {
                loadModel(store, images);
            }
        }.execute();
        final ContentPanel details = createDetailPanel(url, alt, title);

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

        _view.getSelectionModel().addListener(Events.SelectionChange,  
        		new Listener<SelectionChangedEvent<ImageSummaryModelData>>() {  
        	public void handleEvent(SelectionChangedEvent<ImageSummaryModelData> be) {
        		final ImageSummaryModelData md = be.getSelectedItem();
        		if (md != null) {
                    final String path = Paragraph.escape(md.getPath());
                    final String appContext =
                        new IGlobalsImpl().getSetting("application.context");
                    _urlField.setValue(appContext+path);
                    _titleField.setValue(Paragraph.escape(md.getTitle()));
                    _altField.setValue(Paragraph.escape(md.getTitle()));
                    _cccId = md.getId().toString();
        		}
        	}  
        });  

        panel.add(_view);
        add(details, new MarginData(5,5,5,5));
        add(panel);

        addButton(getCancel());
        final Button save = new Button(constants().save(), saveAction());
        addButton(save);
    }

    private ContentPanel createDetailPanel(String url, String alt, String title) {
    	ContentPanel details = new ContentPanel();
        details.setCollapsible(false);
        details.setAnimCollapse(false);
        details.setHeaderVisible(false);
        details.setWidth(PANEL_WIDTH);
        details.setHeight(100);
        details.setLayout(new FormLayout());
        details.setBorders(false);
        details.setBodyBorder(false);
        details.setBodyStyleName("backgroundColor: white;");
        
        _urlField.setFieldLabel("url");
        _urlField.setReadOnly(true);
        _urlField.setValue(url);
        details.add(_urlField, new FormData("95%"));

        _altField.setFieldLabel("alt");
        _altField.setValue(alt);
        details.add(_altField, new FormData("95%"));
        
        _titleField.setFieldLabel(_constants.title());
        _titleField.setValue(title);
        details.add(_titleField, new FormData("95%"));
		return details;
	}

	/** {@inheritDoc} */
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override
            public void componentSelected(final ButtonEvent ce) {
            	if (_cccId != null && !_cccId.equals("")) {
            		jsniSetUrl(
            				_urlField.getValue(),
            				_titleField.getValue(),
            				_altField.getValue(),
            				_cccId,
            				_elementid);
            		hide();
            	} 
            }
        };
    }


    private static native String jsniSetUrl(final String selectedUrl,
                                            final String title,
                                            final String alt,
                                            final String uuid,
                                            final String elementID) /*-{
     if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.InsertHtml("<img title='"+title+"' alt='"
                +alt+"' class='ccc:"+uuid+"' src='"+selectedUrl+"'/>");
            }
        }
        return null;
    }-*/;

    // TODO: Property names aren't type safe.
    private native String getTemplate() /*-{

    return ['<tpl for=".">',
     '<div class="thumb-wrap" id="{NAME}" style="border: 1px solid white">',
     '<div class="thumb">',
     '<img src="preview/{PATH}?thumb=200" title="{TITLE}"></div>',
     '<span class="x-editable">{SHORT_NAME} {WIDTH}x{HEIGHT}px</span></div>',
     '</tpl>',
     '<div class="x-clear"></div>'].join("");

     }-*/;

    private void loadModel(final ListStore<ImageSummaryModelData> store,
                           final Collection<FileDto> arg0) {

        _models = DataBinding.bindFileSummary(arg0);
        if (_models != null && _models.size() > 0) {
            store.add(_models);
        }
    }

}
