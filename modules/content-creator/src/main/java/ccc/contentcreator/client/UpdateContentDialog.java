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

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.AnchorData;
import com.extjs.gxt.ui.client.widget.layout.AnchorLayout;
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

        setHeading(_uiConstants.updateContent());
        setWidth(640);
        setHeight(480);
        setLayout(new AnchorLayout());

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
        add(panel, new AnchorData("100%"));

        // TODO: Move "title" to constants.
        _title.setFieldLabel("Title");
        _title.setAllowBlank(false);
        panel.add(_title, new FormData("100%"));

        final TabPanel tabs = new TabPanel();

        tabs.setPlain(true);
        tabs.setTabScroll(true);
        final AnchorData ld = new AnchorData("100% -59");
        ld.setMargins(new Margins(0, 10, 0, 10));
        add(tabs, ld);

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
            new ErrorReportingCallback<PageDTO>() {

            /** {@inheritDoc} */
            public void onSuccess(final PageDTO page) {

                _title.setValue(page.getTitle());

                for (Map.Entry<String, String> para
                            : page.getParagraphs().entrySet()) {

                    final TextArea area = new TextArea();
                    area.setWidth("100%");
                    area.setHeight("100%");
                    area.setBorders(false);
                    area.setFieldLabel(para.getKey());
                    area.setHideLabel(true);
                    area.setValue(para.getValue());

                    final TabItem paraTab = new TabItem();
                    paraTab.setText(para.getKey());
                    paraTab.addListener(
                        Events.Select,
                        new Listener<ComponentEvent>( ){
                            public void handleEvent(ComponentEvent be) {
                                    area.focus();
                                }
                            });
                    tabs.add(paraTab);

                    paraTab.add(area);

                    _paras.add(area);
                }

            }
        };

        _resourceService.getResource(_contentPath, callback);
    }

}
