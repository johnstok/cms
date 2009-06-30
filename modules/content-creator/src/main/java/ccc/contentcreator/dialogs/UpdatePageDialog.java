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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.api.ID;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.TemplateSummary;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.PageElement;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * A dialog box for updating content.
 *
 * @author Civic Computing Ltd
 */
public class UpdatePageDialog
    extends
        AbstractBaseDialog {

    private final ID _pageId;
    private final PageDelta _page;
    private final TemplateSummary _template;
    private final ResourceTable _rt;
    private final EditPagePanel _panel = new EditPagePanel();



        private final AsyncCallback<Void> _saveDraftCompletedCallback =
            new AsyncCallback<Void>() {
            public void onFailure(final Throwable arg0) {
                Globals.unexpectedError(arg0, _constants.saveDraft());
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
     * @param pageId ID of the page to be updated.
     * @param page PageDelta of the page to be updated.
     * @param pageName Name of the page to be updated.
     * @param template TemplateDelta of the template assigned to the page.
     * @param rt ResourceTable required in order to refresh the contents.
     */
    public UpdatePageDialog(final ID pageId,
                            final PageDelta page,
                            final String pageName,
                            final TemplateSummary template,
                            final ResourceTable rt) {
        super(Globals.uiConstants().updateContent());
        _rt = rt;
        _page = page;
        _template = template;
        _pageId = pageId;

        setLayout(new FitLayout());

        drawGUI(pageName);
    }

    private void drawGUI(final String pageName) {
        _panel.setScrollMode(Style.Scroll.AUTOY);
        _panel.createFields(_template.getDefinition());
        _panel.populateFields(_page, pageName);
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
                final Set<Paragraph> paragraphs = assignParagraphs();

                Validate.callTo(updatePage(paragraphs))
                    .check(Validations.validateFields(
                        paragraphs, _panel.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private SelectionListener<ButtonEvent> saveDraftAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final Set<Paragraph> paragraphs = assignParagraphs();

                Validate.callTo(saveDraft(paragraphs))
                    .check(Validations.validateFields(
                        paragraphs, _panel.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable updatePage(final Set<Paragraph> paragraphs) {
        return new Runnable() {
            public void run() {
                final PageCommentDialog commentDialog =
                    new PageCommentDialog(
                        _pageId, _page, UpdatePageDialog.this);
                commentDialog.show();
            }
        };
    }

    private Runnable saveDraft(final Set<Paragraph> paragraphs) {
        return new Runnable() {
            public void run() {
                commands().updateWorkingCopy(
                    _pageId, _page, saveDraftCompletedCallback());
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

    private Set<Paragraph> assignParagraphs() {

        final Set<Paragraph> paragraphs =
            new HashSet<Paragraph>();

        final List<PageElement> definitions =
            panel().pageElements();

        _panel.extractValues(definitions, paragraphs);
        _page.setParagraphs(paragraphs);
        return paragraphs;
    }
}
