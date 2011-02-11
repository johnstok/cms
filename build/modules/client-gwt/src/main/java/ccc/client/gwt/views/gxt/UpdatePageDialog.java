/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.client.gwt.views.gxt;

import java.util.HashSet;
import java.util.Set;

import ccc.api.core.Page;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.client.actions.UpdateWorkingCopyAction;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.SingleSelectionModel;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.widgets.EditPagePanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * A dialog box for updating content.
 *
 * @author Civic Computing Ltd
 */
public class UpdatePageDialog
    extends
        AbstractBaseDialog {

    private final Set<Paragraph> _paras;
    private final Template _template;
    private final SingleSelectionModel _rt;
    private final EditPagePanel _panel;

    private Button _saveDraftButton;
    private Button _applyNowButton;
    private final ResourceSummary _modelData;


    /**
     * Constructor.
     *
     * @param page PageDelta of the page to be updated.
     * @param template TemplateDelta of the template assigned to the page.
     * @param rt ResourceTable required in order to refresh the contents.
     */
    public UpdatePageDialog(final Page page,
                            final Template template,
                            final SingleSelectionModel rt) {
        super(I18n.uiConstants.updateContent(),
            InternalServices.globals);
        _rt = rt;
        _modelData = rt().tableSelection();
        _paras = new HashSet<Paragraph>(page.getParagraphs());
        _template = template;
        _panel = new EditPagePanel(_template.getDefinition(), rt.root());

        setLayout(new FitLayout());

        drawGUI();
    }


    private void drawGUI() {
        _panel.setName(_modelData.getName());
        _panel.setResourceTitle(_modelData.getTitle());
        _panel.setValues(_paras);
        _panel.layout();

        add(_panel);

        addButton(getCancel());
        addButton(createSaveDraftButton());
        addButton(createApplyNowButton());
    }


    private Button createApplyNowButton() {
        _applyNowButton = new Button(
            getUiConstants().applyNow(),
            applyNowAction());
        return _applyNowButton;
    }


    private Button createSaveDraftButton() {
        _saveDraftButton = new Button(
            getUiConstants().saveDraft(),
            saveDraftAction());
        return _saveDraftButton;
    }


    private SelectionListener<ButtonEvent> applyNowAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final ValidationResult vr = _panel.getValidationResult();

                if (vr.isValid()) {
                    updatePage();
                } else {
                    InternalServices.window.alert(vr.getErrorText());
                }
            }
        };
    }


    private SelectionListener<ButtonEvent> saveDraftAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final ValidationResult vr = _panel.getValidationResult();

                if (vr.isValid()) {
                    saveDraft();
                } else {
                    InternalServices.window.alert(vr.getErrorText());
                }
            }
        };
    }


    private void updatePage() {
        final PageCommentDialog commentDialog =
            new PageCommentDialog(getParagraphs(),
                                  UpdatePageDialog.this);
        commentDialog.show();
    }


    private void saveDraft() {
        final Page update = new Page();
        update.setId(getModelData().getId());
        update.setParagraphs(getParagraphs());
        update.addLink(
            Page.Links.WORKING_COPY,
            _modelData.getLink(
                Page.Links.WORKING_COPY));

        new UpdateWorkingCopyAction(update) {
            /** {@inheritDoc} */
            @Override protected void onSuccess(final Page p) {
                final ResourceSummary md = getModelData();
                md.setHasWorkingCopy(true);
                rt().update(md);
                hide();
            }

        }.execute();
    }


    /**
     * Accessor.
     *
     * @return Returns the _rt.
     */
    public SingleSelectionModel rt() {
        return _rt;
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
     * @return Returns The selected model data.
     */
    protected ResourceSummary getModelData() {
        return _modelData;
    }


    private Set<Paragraph> getParagraphs() { return _panel.getValues(); }
}
