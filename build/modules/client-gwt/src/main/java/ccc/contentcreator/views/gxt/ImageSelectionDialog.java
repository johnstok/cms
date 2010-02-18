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
package ccc.contentcreator.views.gxt;

import ccc.contentcreator.binding.ImageSummaryModelData;
import ccc.contentcreator.core.IGlobalsImpl;
import ccc.contentcreator.dialogs.ImageSelectionPanel;
import ccc.types.Paragraph;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListViewSelectionModel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
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

    private static final int CCC_ID_LENGTH = 40;
    private static final int DIALOG_WIDTH = 720;
    private static final int DIALOG_HEIGHT = 645;
    private static final int PANEL_WIDTH = 700;
    private static final int DETAILS_HEIGHT = 90;

    private final String _elementid;
    private ImageSelectionPanel _imagePanel = new ImageSelectionPanel();

    private String _uuid = null;

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
    public ImageSelectionDialog(final String elementid,
                                final String url,
                                final String alt,
                                final String title,
                                final String cccId) {
        super(new IGlobalsImpl().uiConstants().selectImage(),
              new IGlobalsImpl());
        setLayout(new RowLayout());
        _elementid = elementid;
        if (cccId != null
            && cccId.indexOf("ccc:") != -1
            && cccId.length() >= cccId.indexOf("ccc:")+CCC_ID_LENGTH){
            _uuid = cccId.substring(cccId.indexOf("ccc:")+4,
                cccId.indexOf("ccc:")+CCC_ID_LENGTH);
        }


        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);
        setFrame(true);
        final ListViewSelectionModel<ImageSummaryModelData> selectionModel =
            _imagePanel.getView().getSelectionModel();
        selectionModel.addListener(Events.SelectionChange,
            new Listener<SelectionChangedEvent<ImageSummaryModelData>>() {
            public void handleEvent(final SelectionChangedEvent
                                    <ImageSummaryModelData> be) {
                final ImageSummaryModelData md = be.getSelectedItem();
                if (md != null) {
                    final String path = Paragraph.escape(md.getPath());
                    final String appContext =
                        new IGlobalsImpl().getSetting("application.context");
                    _urlField.setValue(appContext + path);
                    _titleField.setValue(Paragraph.escape(md.getTitle()));
                    _altField.setValue(Paragraph.escape(md.getTitle()));
                    _uuid = md.getId().toString();
                }
            }
        });

        final ContentPanel details = createDetailPanel(url, alt, title);
        add(details, new MarginData(5, 5, 5, 5));

        add(_imagePanel);
        addButton(getCancel());
        final Button save = new Button(constants().save(), saveAction());
        addButton(save);
    }


    private ContentPanel createDetailPanel(final String url,
                                           final String alt,
                                           final String title) {
        final ContentPanel details = new ContentPanel();
        details.setCollapsible(false);
        details.setAnimCollapse(false);
        details.setHeaderVisible(false);
        details.setWidth(PANEL_WIDTH);
        details.setHeight(DETAILS_HEIGHT);
        details.setLayout(new FormLayout());
        details.setBorders(false);
        details.setBodyBorder(false);
        details.setBodyStyleName("backgroundColor: white;");

        _urlField.setFieldLabel(getConstants().path());
        _urlField.setValue(url);
        _urlField.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyUp(final ComponentEvent event) {
                _uuid = null;
            }
        });

        details.add(_urlField, new FormData("95%"));

        _altField.setFieldLabel(getConstants().alternativeText());
        _altField.setValue(alt);
        details.add(_altField, new FormData("95%"));

        _titleField.setFieldLabel(getConstants().title());
        _titleField.setValue(title);
        details.add(_titleField, new FormData("95%"));
        return details;
    }



    /** {@inheritDoc} */
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override
            public void componentSelected(final ButtonEvent ce) {
                if (_urlField.getValue() != null
                    && !_urlField.getValue().equals("")) {
                    jsniSetUrl(
                        _urlField.getValue(),
                        _titleField.getValue(),
                        _altField.getValue(),
                        _uuid,
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
                var linkURL = "<img title='"+title+"' alt='"
                +alt+"' src='"+selectedUrl+"'"

                if (uuid != null) {
                    linkURL = linkURL +" class='ccc:"+uuid+"'";
                }
                linkURL = linkURL +"/>";
                return instance.InsertHtml(linkURL);
            }
        }
        return null;
    }-*/;



}
