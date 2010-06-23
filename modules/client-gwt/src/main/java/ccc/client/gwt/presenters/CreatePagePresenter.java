/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.presenters;

import static ccc.client.core.InternalServices.*;

import java.util.Set;
import java.util.UUID;

import ccc.api.core.Page;
import ccc.api.core.Template;
import ccc.api.types.CommandType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.client.core.Editable;
import ccc.client.core.I18n;
import ccc.client.core.ValidationResult;
import ccc.client.events.Event;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.remoting.CreatePageAction;
import ccc.client.views.CreatePage;


/**
 * MVP Presenter for creating page.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePagePresenter
    extends
        AbstractPresenter<CreatePage, ResourceSummaryModelData>
    implements
        Editable {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreatePagePresenter(final CreatePage view,
                               final ResourceSummaryModelData model) {
        super(view, model);
        getView().show(this);
    }


    /** {@inheritDoc} */
    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void save() {
        final ValidationResult vr = getView().getValidationResult();
        vr.addError(
            VALIDATOR.notEmpty(
                getView().getName(), I18n.UI_CONSTANTS.name()));
        vr.addError(
            VALIDATOR.notValidResourceName(
                getView().getName(), I18n.UI_CONSTANTS.name()));


        if (vr.isValid()) {

            final Set<Paragraph> paragraphs = getView().getParagraphs();
            final Page p = Page.delta(paragraphs);
            final Template tData = getView().getSelectedTemplate();

            if (tData == null) {
                getView().alert(I18n.UI_CONSTANTS.noTemplateChosen());
                return;
            }
            p.setTemplate(tData.getId());

            createPage(paragraphs);

        } else {
            getView().alert(vr.getErrorText());
        }
    }

    private void createPage(final Set<Paragraph> paragraphs) {
        final UUID template =
            (null==getView().getSelectedTemplate())
            ? null
                :getView().getSelectedTemplate().getId();

        final Page page = Page.delta(paragraphs);
        page.setParent(getModel().getId());
        page.setComment(getView().getComment());
        page.setMajorChange(getView().getMajorEdit());
        page.setTitle(getView().getName());
        page.setTemplate(template);
        page.setName(new ResourceName(getView().getName()));

        new CreatePageAction(page).execute();
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case PAGE_CREATE:
                dispose();
                break;

            default:
                break;
        }
    }

}
