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

package ccc.contentcreator.dialogs;

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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

    private final EditPagePanel _panel = new EditPagePanel();

    private PageDelta _page;
    private TemplateDelta _template;
    private final ResourceTable _rt;

    private final AsyncCallback<Void> _saveCompletedCallback =
        new AsyncCallback<Void>() {
            public void onFailure(final Throwable arg0) {
                Globals.unexpectedError(arg0);
            }
            public void onSuccess(final Void arg0) {
                rt().refreshTable();
                close();
            }
        };


    /**
     * Constructor.
     *
     * @param rt ResourceTable required in order to refresh the contents.
     */
    public UpdatePageDialog(final PageDelta page, final TemplateDelta template, final ResourceTable rt) {
        super(Globals.uiConstants().updateContent());
        _rt = rt;
        _page = page;
        _template = template;

        setLayout(new FitLayout());

        drawGUI();
    }

    private void drawGUI() {

        _panel.createFields(_template._definition);
        _panel.populateFields(_page);
        _panel.layout();

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
                    _page._title = panel().title().getValue();

                    final List<ParagraphDelta> paragraphs =
                        new ArrayList<ParagraphDelta>();

                    final List<Component> definitions =
                        panel().definitionItems();
                    for (final Component c : definitions) {
                        if ("TEXT".equals(c.getData("type"))) {
                            final Field<String> f = (Field<String>) c;
                            final ParagraphDelta p = new ParagraphDelta();
                            p._name = c.getId();
                            p._textValue = f.getValue();
                            p._type = "TEXT";
                            paragraphs.add(p);
                        } else if ("DATE".equals(c.getData("type"))) {
                            final DateField f = (DateField) c;
                            final ParagraphDelta p = new ParagraphDelta();
                            p._name = c.getId();
                            p._dateValue = f.getValue();
                            p._type = "TEXT";
                            paragraphs.add(p);
                        }
                    }
                    _page._paragraphs = paragraphs;

                    commands().updatePage(
                        _page,
                        saveCompletedCallback());
                }
            });
        saveButton.setId("save");
        return saveButton;
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
}
