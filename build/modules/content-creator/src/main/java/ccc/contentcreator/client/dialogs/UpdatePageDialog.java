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

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.services.api.PageDelta;
import ccc.services.api.TemplateSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A dialog box for updating content.
 * TODO: Extend AbstractEditDialog?
 *
 * @author Civic Computing Ltd
 */
public class UpdatePageDialog
    extends
        AbstractBaseDialog {

    private final String _resourceId;
    private final EditPagePanel _panel = new EditPagePanel();
    private PageDelta _page = null;
    private final ResourceTable _rt;

    private final AsyncCallback<Void> _saveCompletedCallback =
        new AsyncCallback<Void>() {
            public void onFailure(final Throwable arg0) {
                GWT.log("Page saving failed", arg0);
            }
            public void onSuccess(final Void arg0) {
                rt().refreshTable();
                close();
            }
        };

      private final AsyncCallback<TemplateSummary> _lookupTemplateCallback =
            new ErrorReportingCallback<TemplateSummary>() {

            /** {@inheritDoc} */
            public void onSuccess(final TemplateSummary template) {
                if (template == null) {
                    Globals.alert(constants().noTemplateFound());
                    close();
                } else {
                    panel().createFields(template._definition);
                    panel().populateFields(page());
                    panel().layout(); // Refresh UI when callback is done
                }
            }
        };

       private final AsyncCallback<PageDelta> _lookupPageCallback =
            new ErrorReportingCallback<PageDelta>() {

            /** {@inheritDoc} */
            public void onSuccess(final PageDelta page) {
                page(page);
                queries()
                    .getTemplateForResource(null, lookupTemplateCallback()); // FIXME: needs pageId

            }
        };


    /**
     * Constructor.
     *
     * @param resourceId The UUID of the content resource this dialog
     *          will update.
     * @param rt ResourceTable required in order to refresh the contents.
     */
    public UpdatePageDialog(final String resourceId, final ResourceTable rt) {
        super(Globals.uiConstants().updateContent());
        _rt = rt;
        setLayout(new FitLayout());

        _resourceId = resourceId;
        drawGUI();
    }

    private void drawGUI() {

        add(_panel);

        addButton(
            new Button(
                constants().cancel(),
                new SelectionListener<ButtonEvent>() {
                    @Override
                    public void componentSelected(final ButtonEvent ce) {
                        close();
                    }
                }
            ));

        addButton(createSaveButton());

//        queries().resource(_resourceId, _lookupPageCallback); // FIXME: Should get PageDelta?
    }

    private Button createSaveButton() {

        final Button saveButton = new Button(
            constants().save(),
            new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {

                    if (panel().title().getValue() == null
                        || panel().title().getValue().trim().length() == 0) {
                        return;
                    }

                    PageDelta pd = new PageDelta();

//                    final Map<String, ParagraphDTO> paragraphs =
//                        new HashMap<String, ParagraphDTO>();
//
//                    final List<Component> definitions =
//                        panel().definitionItems();
//                    for (final Component c : definitions) {
//                        if ("TEXT".equals(c.getData("type"))) {
//                            final Field<String> f = (Field<String>) c;
//                            final ParagraphDTO p =
//                                new ParagraphDTO("TEXT", f.getValue());
//                            paragraphs.put(c.getId(), p);
//                        } else if ("DATE".equals(c.getData("type"))) {
//                            final DateField f = (DateField) c;
//                            final ParagraphDTO p = new ParagraphDTO(
//                                "DATE", ""+f.getValue().getTime());
//                            paragraphs.put(c.getId(), p);
//                        }
//                    }

                    commands().updatePage(
                        resourceId(),
                        -1, // FIXME: Erm...
                        pd,
                        saveCompletedCallback());
                }
            });
        saveButton.setId("save");
        return saveButton;
    }


    /**
     * Accessor.
     *
     * @return Returns the _resourceId.
     */
    protected String resourceId() {
        return _resourceId;
    }


    /**
     * Accessor.
     *
     * @return Returns the _rt.
     */
    public ResourceTable rt() {
        return _rt;
    }


    /**
     * Accessor.
     *
     * @return Returns the _page.
     */
    protected PageDelta page() {
        return _page;
    }


    /**
     * Mutator.
     *
     * @param page The _page to set.
     */
    protected void page(final PageDelta page) {
        _page = page;
    }


    /**
     * Accessor.
     *
     * @return Returns the _panel.
     */
    protected EditPagePanel panel() {
        return _panel;
    }


    /**
     * Accessor.
     *
     * @return Returns the _saveCompletedCallback.
     */
    protected AsyncCallback<Void> saveCompletedCallback() {
        return _saveCompletedCallback;
    }


    /**
     * Accessor.
     *
     * @return Returns the _lookupTemplateCallback.
     */
    protected AsyncCallback<TemplateSummary> lookupTemplateCallback() {
        return _lookupTemplateCallback;
    }


    /**
     * Accessor.
     *
     * @return Returns the _lookupPageCallback.
     */
    protected AsyncCallback<PageDelta> lookupPageCallback() {
        return _lookupPageCallback;
    }
}
