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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.PageDto;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.core.Editable;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.ValidationResult;
import ccc.client.gwt.events.PageCreated;
import ccc.client.gwt.events.PageCreated.PageCreatedHandler;
import ccc.client.gwt.remoting.CreatePageAction;
import ccc.client.gwt.validation.Validate;
import ccc.client.gwt.validation.Validations;
import ccc.client.gwt.views.CreatePage;


/**
 * MVP Presenter for creating page.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePagePresenter
    extends
        AbstractPresenter<CreatePage, ResourceSummaryModelData>
    implements
        Editable,
        PageCreatedHandler {


    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreatePagePresenter(final Globals globals,
                                 final CreatePage view,
                                 final ResourceSummaryModelData model) {
        super(globals, view, model);
        addHandler(PageCreated.TYPE, this);
        getView().show(this);
    }


    private void hide() {
        clearHandlers();
        getView().hide();
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
        if (vr.isValid()) {

            final Set<Paragraph> paragraphs = getView().getParagraphs();
                new HashSet<Paragraph>();

            Validate.callTo(createPage(paragraphs))
                .check(Validations.notEmpty(getView().getName()))
                .stopIfInError()
                .check(Validations.notValidResourceName(
                    getView().getName()))
                .stopIfInError()
                .check(Validations.uniqueResourceName(
                    getModel(),  getView().getName()))
                .stopIfInError()
                .check(Validations.validateFields(paragraphs,
                    getView().getDefinition()))
                .callMethodOr(Validations.reportErrors());
        } else {
            getView().alert(vr.getErrorText());
        }
    }

    private Runnable createPage(final Set<Paragraph> paragraphs) {
        return new Runnable() {
            public void run() {

                final UUID template =
                    (null==getView().getSelectedTemplate())
                    ? null
                        :getView().getSelectedTemplate().getId();

                final PageDto page = PageDto.delta(paragraphs);
                page.setParent(getModel().getId());
                page.setComment(getView().getComment());
                page.setMajorChange(getView().getMajorEdit());
                page.setTitle(getView().getName().getValue());
                page.setTemplate(template);
                page.setName(new ResourceName(getView().getName().getValue()));

                new CreatePageAction(page).execute();
            }
        };
    }


    /** {@inheritDoc} */
    @Override
    public void onCreate(final PageCreated event) { hide(); }

}
