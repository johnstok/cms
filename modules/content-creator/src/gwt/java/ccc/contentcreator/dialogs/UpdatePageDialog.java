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

import static ccc.services.api.ParagraphType.*;

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.PageElement;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.client.ui.FCKEditor;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
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


        private final AsyncCallback<Void> _saveDraftCompletedCallback =
            new AsyncCallback<Void>() {
            public void onFailure(final Throwable arg0) {
                Globals.unexpectedError(arg0);
            }
            public void onSuccess(final Void arg0) {
                final ResourceSummaryModelData md = rt().tableSelection();
                md.setWorkingCopy(true);
                rt().update(md);
                close();
            }
        };


    /**
     * Constructor.
     *
     * @param page PageDelta of the page to be updated.
     * @param template TemplateDelta of the template assigned to the page.
     * @param rt ResourceTable required in order to refresh the contents.
     */
    public UpdatePageDialog(final PageDelta page,
                            final TemplateDelta template,
                            final ResourceTable rt) {
        super(Globals.uiConstants().updateContent());
        _rt = rt;
        _page = page;
        _template = template;

        setLayout(new FitLayout());

        drawGUI();
    }

    private void drawGUI() {
        _panel.setScrollMode(Style.Scroll.ALWAYS);
        _panel.createFields(_template.getDefinition());
        _panel.populateFields(_page);
        _panel.layout();

        add(_panel);

        addButton(_cancel);
        addButton(createSaveDraftButton());
        addButton(createApplyNowButton());
    }

    private Button createApplyNowButton() {

        final Button applyNowButton = new Button(
            _constants.applyNow(),
            applyNowAction());
        applyNowButton.setId("applyNow");
        return applyNowButton;
    }

    private Button createSaveDraftButton() {

        final Button saveDraftButton = new Button(
            _constants.saveDraft(),
            saveDraftAction());
        saveDraftButton.setId("saveDraft");
        return saveDraftButton;
    }

    private SelectionListener<ButtonEvent> applyNowAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final List<ParagraphDelta> paragraphs = assignParagraphs();

                Validate.callTo(updatePage(paragraphs))
                    .check(Validations.notEmpty(panel().title()))
                    .check(Validations.noBrackets(panel().title()))
                    .stopIfInError()
                    .check(Validations.validateDatefields(paragraphs))
                    .stopIfInError()
                    .check(Validations.validateFields(paragraphs,
                        _panel.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private SelectionListener<ButtonEvent> saveDraftAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final List<ParagraphDelta> paragraphs = assignParagraphs();

                Validate.callTo(saveDraft(paragraphs))
                .check(Validations.notEmpty(panel().title()))
                .check(Validations.noBrackets(panel().title()))
                .stopIfInError()
                .check(Validations.validateDatefields(paragraphs))
                .stopIfInError()
                .check(Validations.validateFields(paragraphs,
                    _panel.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable updatePage(final List<ParagraphDelta> paragraphs) {
        return new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {
                _page.setTitle(panel().title().getValue());
                final PageCommentDialog commentDialog =
                    new PageCommentDialog(_page, UpdatePageDialog.this);
                commentDialog.show();
            }
        };
    }

    private Runnable saveDraft(final List<ParagraphDelta> paragraphs) {
        return new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {
                _page.setTitle(panel().title().getValue());
                commands().updateWorkingCopy(_page,
                                             saveDraftCompletedCallback());
            }
        };
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
     * @return Returns the _saveDraftCompletedCallback.
     */
    protected AsyncCallback<Void> saveDraftCompletedCallback() {
        return _saveDraftCompletedCallback;
    }

    private List<ParagraphDelta> assignParagraphs() {

        final List<ParagraphDelta> paragraphs =
            new ArrayList<ParagraphDelta>();

        final List<PageElement> definitions =
            panel().pageElements();
        for (final PageElement c : definitions) {
            if ("TEXT".equals(c.type())) {
                final Field<String> f = c.field();
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        TEXT,
                        null,
                        f.getValue(),
                        null,
                        null);
                paragraphs.add(p);
            } else if ("DATE".equals(c.type())) {
                final DateField f = c.dateField();
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        DATE,
                        f.getRawValue(),
                        null,
                        f.getValue(),
                        null);
                paragraphs.add(p);
            } else if ("HTML".equals(c.type())) {
                final FCKEditor f = c.editor();
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        TEXT,
                        null,
                        f.getHTML(),
                        null,
                        null);
                paragraphs.add(p);
            }
        }
        _page.setParagraphs(paragraphs);
        return paragraphs;
    }
}
