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

import ccc.contentcreator.actions.UpdateWorkingCopyAction_;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.PageElement;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.types.ID;
import ccc.types.Paragraph;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.http.client.Response;


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
    private int _fckReadyCount = 0;

    private Button _saveDraftButton;
    private Button _applyNowButton;


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
        super(new IGlobalsImpl().uiConstants().updateContent(),
              new IGlobalsImpl());
        _rt = rt;
        _page = page;
        _template = template;
        _pageId = pageId;

        setLayout(new FitLayout());

        drawGUI(pageName);
        // in case of FCKeditors add JS function for ready status checking.
        if (_panel.getFCKCount()>0) {
            _applyNowButton.disable();
            _saveDraftButton.disable();
            initJSNI(this);
        }
    }

    private static native String initJSNI(final UpdatePageDialog obj) /*-{
        $wnd.FCKeditor_OnComplete = function(editorInstance) {
            obj.@ccc.contentcreator.dialogs.UpdatePageDialog::checkFCK()();
        }
    }-*/;

    /**
     * Enable save buttons when FCKEditors are ready.
     */
    public void checkFCK() {
        _fckReadyCount++;
        if (_fckReadyCount == _panel.getFCKCount()) {
            _saveDraftButton.enable();
            _applyNowButton.enable();
        }
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

        _applyNowButton = new Button(
            _constants.applyNow(),
            applyNowAction());
        _applyNowButton.setId("applyNow");
        return _applyNowButton;
    }

    private Button createSaveDraftButton() {

        _saveDraftButton = new Button(
            _constants.saveDraft(),
            saveDraftAction());
        _saveDraftButton.setId("saveDraft");
        return _saveDraftButton;
    }

    private SelectionListener<ButtonEvent> applyNowAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final Set<Paragraph> paragraphs = assignParagraphs();

                Validate.callTo(updatePage())
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

                Validate.callTo(saveDraft())
                    .check(Validations.validateFields(
                        paragraphs, _panel.definition()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable updatePage() {
        return new Runnable() {
            public void run() {
                final PageCommentDialog commentDialog =
                    new PageCommentDialog(
                        _pageId, _page, UpdatePageDialog.this);
                commentDialog.show();
            }
        };
    }

    private Runnable saveDraft() {
        return new Runnable() {
            public void run() {
                new UpdateWorkingCopyAction_(_pageId, _page) {
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(final Response response) {
                        final ResourceSummaryModelData md = rt().tableSelection();
                        md.setWorkingCopy(true);
                        rt().update(md);
                        hide();
                    }

                }.execute();
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
