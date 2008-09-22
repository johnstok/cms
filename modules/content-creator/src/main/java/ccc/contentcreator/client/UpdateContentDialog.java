/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.contentcreator.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccc.contentcreator.api.ResourceService;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dto.PageDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A dialog box for updating content.
 *
 * @author Civic Computing Ltd
 */
public class UpdateContentDialog extends Window {

    private final UIConstants _uiConstants = GWT.create(UIConstants.class);
    private final List<TextArea> _paras = new ArrayList<TextArea>();
    private final ResourceServiceAsync _resourceService =
        (ResourceServiceAsync) GWT.create(ResourceService.class);

    private final String _contentPath;
    private final TextField<String> _title = new TextField<String>();

    /**
     * Constructor.
     *
     * @param contentPath The absolute path to the content resource this dialog
     *          will update.
     */
    public UpdateContentDialog(final String contentPath) {

        setPlain(true);
        setHeading(_uiConstants.updateContent());
        setWidth(640);
        setHeight(480);
        setLayout(new FitLayout());

        _contentPath = contentPath;
        ensureDebugId("dialogBox");
        drawGUI();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param title
     */
    private void drawGUI() {


        final FormPanel panel = new FormPanel();
        panel.setWidth("100%");
        panel.setBorders(false);
        panel.setBodyBorder(false);
        panel.setHeaderVisible(false);
        add(panel);

        _title.setFieldLabel("Title");
        _title.setAllowBlank(false);
        panel.add(_title, new FormData("100%"));

        addButton(
            new Button(
                _uiConstants.cancel(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(final ButtonEvent ce) {
                        hide();
                    }
                }
            ));

        addButton(
            new Button(
                _uiConstants.save(),
                new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {

                        if (_title.getValue() == null
                            || _title.getValue().trim().length() == 0) {
//                            _title.setStyleName("gwt-TextBox-error");
                            return;
                        }

//                        _title.setStyleName("gwt-TextBox");

                        final Map<String, String> paragraphs =
                            new HashMap<String, String>();
                        for (final TextArea para : _paras) {
                            String body = para.getValue();
                            if (null == body || body.trim().length()==0) {
                                body = "<!-- empty -->";
                            }
                            paragraphs.put(para.getFieldLabel(), body);
                        }
//
                        final AsyncCallback<Void> callback =
                            new AsyncCallback<Void>() {
                                public void onFailure(final Throwable arg0) {
                                    GWT.log("Page saving failed", arg0);
                                }
                                public void onSuccess(final Void arg0) {
                                    hide();
                                    //TODO: tree.fire_selection_event();
                                }
                            };

//                            final AsyncCallback<Void> callback =
//                                new DisposingCallback(_app, UpdateContentDialog.this);

                        _resourceService.saveContent(
                            _contentPath,
                            _title.getValue(),
                            paragraphs,
                            callback);
                    }
                }));

        final AsyncCallback<PageDTO> callback =
            new ErrorReportingCallback<PageDTO>(null) {

            /** {@inheritDoc} */
            public void onSuccess(final PageDTO page) {

                _title.setValue(page.getTitle());

                for (Map.Entry<String, String> para
                            : page.getParagraphs().entrySet()) {

                    TextArea area = new TextArea();
                    area.setFieldLabel(para.getKey());
                    area.setValue(para.getValue());
                    panel.add(area, new FormData("100%"));
                    _paras.add(area);
                }

            }
        };

        _resourceService.getResource(_contentPath, callback);
    }

}
