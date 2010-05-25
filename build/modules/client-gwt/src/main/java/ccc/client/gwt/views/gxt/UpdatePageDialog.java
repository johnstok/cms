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
import java.util.List;
import java.util.Set;

import ccc.api.core.Page;
import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.remoting.UpdateWorkingCopyAction;
import ccc.client.gwt.validation.Validate;
import ccc.client.gwt.validation.Validations;
import ccc.client.gwt.widgets.EditPagePanel;
import ccc.client.gwt.widgets.PageElement;
import ccc.client.gwt.widgets.ResourceTable;

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
    private final ResourceTable _rt;
    private final EditPagePanel _panel;
    private int _fckReadyCount = 0;

    private Button _saveDraftButton;
    private Button _applyNowButton;
    private final ResourceSummaryModelData _modelData;


    /**
     * Constructor.
     *
     * @param page PageDelta of the page to be updated.
     * @param template TemplateDelta of the template assigned to the page.
     * @param rt ResourceTable required in order to refresh the contents.
     */
    public UpdatePageDialog(final Page page,
                            final Template template,
                            final ResourceTable rt) {
        super(new GlobalsImpl().uiConstants().updateContent(),
              new GlobalsImpl());
        _rt = rt;
        _modelData = rt().tableSelection();
        _paras = new HashSet<Paragraph>(page.getParagraphs());
        _template = template;
        _panel = new EditPagePanel(_template);

        setLayout(new FitLayout());

        drawGUI(_modelData.getName());
        // in case of FCKeditors add JS function for ready status checking.
        if (_panel.getFCKCount()>0) {
            _applyNowButton.disable();
            _saveDraftButton.disable();
            initJSNI(this);
        }
    }

    private static native String initJSNI(final UpdatePageDialog obj) /*-{
        $wnd.FCKeditor_OnComplete = function(editorInstance) {
            obj.@ccc.client.gwt.views.gxt.UpdatePageDialog::checkFCK()();
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
        _panel.populateFields(_paras, pageName);
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
        _applyNowButton.setId("applyNow");
        return _applyNowButton;
    }

    private Button createSaveDraftButton() {

        _saveDraftButton = new Button(
            getUiConstants().saveDraft(),
            saveDraftAction());
        _saveDraftButton.setId("saveDraft");
        return _saveDraftButton;
    }

    private SelectionListener<ButtonEvent> applyNowAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final Page p = Page.delta(getParagraphs());
                p.setTemplate(_panel.template().getId());

                Validate.callTo(updatePage())
                    .check(Validations.validateFields(p))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private SelectionListener<ButtonEvent> saveDraftAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final Page p = Page.delta(getParagraphs());
                p.setTemplate(_panel.template().getId());

                Validate.callTo(saveDraft())
                    .check(Validations.validateFields(p))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable updatePage() {
        return new Runnable() {
            public void run() {
                final PageCommentDialog commentDialog =
                    new PageCommentDialog(getParagraphs(),
                                          UpdatePageDialog.this);
                commentDialog.show();
            }
        };
    }

    private Runnable saveDraft() {
        return new Runnable() {
            public void run() {
                final Page update = new Page();
                update.setId(getModelData().getId());
                update.setParagraphs(getParagraphs());
                update.addLink(
                    Page.WORKING_COPY,
                    _modelData.getDelegate().getLink(
                        Page.WORKING_COPY));

                new UpdateWorkingCopyAction(update) {
                    /** {@inheritDoc} */
                    @Override protected void onNoContent(
                                                     final Response response) {
                        final ResourceSummaryModelData md = getModelData();
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
    protected ResourceSummaryModelData getModelData() {
        return _modelData;
    }

    private Set<Paragraph> getParagraphs() {
        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        final List<PageElement> definitions = panel().pageElements();
        _panel.extractValues(definitions, paragraphs);
        return paragraphs;
    }
}
