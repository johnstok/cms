/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views.gxt;

import java.util.UUID;

import ccc.contentcreator.actions.UpdatePageAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.IGlobalsImpl;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.rest.dto.PageDelta;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.http.client.Response;

/**
 * Dialog for capturing the page edit metadata.
 *
 * @author Civic Computing Ltd.
 */
public class PageCommentDialog extends AbstractEditDialog {

    /** DIALOG_HEIGHT : int. */
    private static final int DIALOG_HEIGHT = 300;
    /** DIALOG_WIDTH : int. */
    private static final int DIALOG_WIDTH = 400;
    private final UUID _pageId;
    private final PageDelta _page;
    private final UpdatePageDialog _updatePageDialog;
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();


    /**
     * Constructor.
     *
     * @param pageId The id of the page to be updated.
     * @param page The page being edited.
     * @param updatePageDialog The parent dialog.
     *
     */
    public PageCommentDialog(final UUID pageId,
                             final PageDelta page,
                             final UpdatePageDialog updatePageDialog) {
        super(new IGlobalsImpl().uiConstants().pageEditComment(),
              new IGlobalsImpl());
        _pageId = pageId;
        _page = page;
        _updatePageDialog = updatePageDialog;
        setModal(true);
        setBodyStyle("backgroundColor: white;");
        setHeading(getUiConstants().editComment());
        setWidth(DIALOG_WIDTH);
        setHeight(DIALOG_HEIGHT);

        _majorEdit.setId("majorEdit");
        _majorEdit.setValue(Boolean.FALSE);
        _majorEdit.setBoxLabel(getUiConstants().yes());
        _majorEdit.setFieldLabel(getUiConstants().majorEdit());
        addField(_majorEdit);

        _comment.setFieldLabel(getUiConstants().comment());
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
            public void run() {
                new UpdatePageAction(
                    _pageId,
                    _page,
                    _comment.getValue(),
                    _majorEdit.getValue().booleanValue()) {
                        /** {@inheritDoc} */
                        @Override protected void onNoContent(
                                                     final Response response) {
                            final ResourceSummaryModelData md =
                                _updatePageDialog.rt().tableSelection();
                            md.setWorkingCopy(false);
                            _updatePageDialog.rt().update(md);
                            hide();
                            _updatePageDialog.hide();
                        }
                }.execute();
                hide(); // TODO: Why is this here?
            }
        };
    }
}
