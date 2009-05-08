/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.PageDelta;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageCommentDialog extends AbstractEditDialog {

    private PageDelta _page;
    private UpdatePageDialog _updatePageDialog;
    private CheckBox _majorEdit = new CheckBox();
    private TextArea _comment = new TextArea();

    private final AsyncCallback<Void> _applyNowCompletedCallback =
        new AsyncCallback<Void>() {
            public void onFailure(final Throwable arg0) {
                Globals.unexpectedError(arg0);
            }
            public void onSuccess(final Void arg0) {
                final ResourceSummaryModelData md = _updatePageDialog.rt().tableSelection();
                md.setTitle(_page.getTitle());
                md.setWorkingCopy(false);
                _updatePageDialog.rt().update(md);
                close();
                _updatePageDialog.close();
            }
        };


    /**
     * Constructor.
     * @param page The page being edited.
     * @param updatePageDialog The parent dialog.
     *
     */
    public PageCommentDialog(final PageDelta page,
                             final UpdatePageDialog updatePageDialog) {
        super(Globals.uiConstants().pageEditComment());
        _page = page;
        _updatePageDialog = updatePageDialog;
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setHeading(_constants.editComment());
        setWidth(400);
        setHeight(300);

        _majorEdit.setId("majorEdit");
        _majorEdit.setValue(false);
        _majorEdit.setBoxLabel(_constants.yes());
        _majorEdit.setFieldLabel(_constants.majorEdit());
        addField(_majorEdit);

        _comment.setFieldLabel(_constants.comment());
        _comment.setId("comment");
        addField(_comment);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {

                Validate.callTo(savePage())
                .check(Validations.noBrackets(_comment))
                .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable savePage() {
        return new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {
                commands().updatePage(
                    _page,
                    _comment.getValue(),
                    _majorEdit.getValue(),
                    applyNowCompletedCallback());
                      close();
            }
        };
    }

    /**
     * Accessor.
     *
     * @return Returns the _applyNowCompletedCallback.
     */
    protected AsyncCallback<Void> applyNowCompletedCallback() {
        return _applyNowCompletedCallback;
    }
}
