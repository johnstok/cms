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
package ccc.client.gwt.views.gxt;

import java.util.Set;

import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.types.Paragraph;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.I18n;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.remoting.UpdatePageAction;
import ccc.client.gwt.validation.Validate;
import ccc.client.gwt.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;

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
    private final Set<Paragraph> _paras;
    private final UpdatePageDialog _updatePageDialog;
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();


    /**
     * Constructor.
     *
     * @param paras The updated paragraphs.
     * @param updatePageDialog The parent dialog.
     *
     */
    public PageCommentDialog(final Set<Paragraph> paras,
                             final UpdatePageDialog updatePageDialog) {
        super(I18n.UI_CONSTANTS.pageEditComment(),
              new GlobalsImpl());
        _paras = paras;
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
                final ResourceSummaryModelData md =
                    _updatePageDialog.getModelData();
                final Page update = new Page();
                update.setId(md.getId());
                update.setParagraphs(_paras);
                update.setComment(_comment.getValue());
                update.setMajorChange(_majorEdit.getValue().booleanValue());
                update.addLink(
                    Resource.SELF,
                    _updatePageDialog.getModelData().getDelegate().getLink(Resource.SELF));

                new UpdatePageAction(update) {
                        /** {@inheritDoc} */
                        @Override protected void onNoContent(
                                                     final Response response) {

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
