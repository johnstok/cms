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

package ccc.contentcreator.client.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccc.contentcreator.api.ResourceService;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ParagraphDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A dialog box for updating content.
 *
 * @author Civic Computing Ltd
 */
public class UpdatePageDialog
    extends
        AbstractBaseDialog {

    private final UIConstants _uiConstants = GWT.create(UIConstants.class);
    private final ResourceServiceAsync _resourceService =
        (ResourceServiceAsync) GWT.create(ResourceService.class);

    private final String _contentPath;
    private EditPagePanel _panel = new EditPagePanel();
    private PageDTO _page = null;

    private final ResourceTable _rt;

    /**
     * Constructor.
     *
     * @param contentPath The absolute path to the content resource this dialog
     *          will update.
     */
    public UpdatePageDialog(final String contentPath, final ResourceTable rt) {
        super(Globals.uiConstants().updateContent());
        _rt = rt;
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

        add(_panel);

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

        addButton(createSaveButton());

        // load definition and paragraphs
        final AsyncCallback<TemplateDTO> defCallback =
            new ErrorReportingCallback<TemplateDTO>() {

            /** {@inheritDoc} */
            public void onSuccess(final TemplateDTO template) {
               _panel.createFields(template.getDefinition());
               _panel.populateFields(_page);
               _panel.layout(); // Refresh UI when callback is done
            }
        };


        final AsyncCallback<PageDTO> callback =
            new ErrorReportingCallback<PageDTO>() {

            /** {@inheritDoc} */
            public void onSuccess(final PageDTO page) {
                _page  = page;
                _resourceService.getTemplateForResource(page, defCallback);

            }
        };
        _resourceService.getResource(_contentPath, callback);
    }

    private Button createSaveButton() {

        return new Button(
            _uiConstants.save(),
            new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {

                    if (_panel.title().getValue() == null
                        || _panel.title().getValue().trim().length() == 0) {
                        return;
                    }

                    final Map<String, ParagraphDTO> paragraphs =
                        new HashMap<String, ParagraphDTO>();

                    final List<Component> definitions =_panel.definitionItems();
                    for (final Component c : definitions) {
                        if ("TEXT".equals(c.getData("type"))) {
                            final Field<String> f = (Field<String>) c;
                            final ParagraphDTO p =
                                new ParagraphDTO("TEXT", f.getValue());
                            paragraphs.put(c.getId(), p);
                        } else if ("DATE".equals(c.getData("type"))) {
                            final DateField f = (DateField) c;
                            final ParagraphDTO p = new ParagraphDTO(
                                "DATE", ""+f.getValue().getTime());
                            paragraphs.put(c.getId(), p);
                        }
                    }

                    final AsyncCallback<Void> callback =
                        new AsyncCallback<Void>() {
                            public void onFailure(final Throwable arg0) {
                                GWT.log("Page saving failed", arg0);
                            }
                            public void onSuccess(final Void arg0) {
                                _rt.refreshTable();
                                hide();
                                //TODO: tree.fire_selection_event();
                            }
                        };

                    _resourceService.saveContent(
                        _contentPath,
                        _panel.title().getValue(),
                        paragraphs,
                        callback);
                }
            });
    }
}
