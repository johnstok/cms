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
package ccc.contentcreator.presenters;

import java.util.Collection;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.AbstractPresenter;
import ccc.contentcreator.core.CommandResponseHandler;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.EventBus;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.events.ResourceUpdatedEvent;
import ccc.contentcreator.remoting.UpdateResourceTemplateAction;
import ccc.contentcreator.views.ChangeResourceTemplate;
import ccc.rest.dto.TemplateSummary;

import com.google.gwt.http.client.Response;


/**
 * MVP Presenter for changing a resource's template.
 *
 * @author Civic Computing Ltd.
 */
public class ChangeResourceTemplatePresenter
    extends
        AbstractPresenter<ChangeResourceTemplate, ResourceSummaryModelData>
    implements
        Editable,
        CommandResponseHandler<Void> {


    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param bus Implementation of the Event Bus API.
     * @param view View implementation.
     * @param model Model implementation.
     * @param templates The templates to choose from.
     */
    public ChangeResourceTemplatePresenter(
                               final Globals globals,
                               final EventBus bus,
                               final ChangeResourceTemplate view,
                               final ResourceSummaryModelData model,
                               final Collection<TemplateSummary> templates) {
        super(globals, bus, view, model);
        getView().setTemplates(templates);
        getView().setSelectedTemplate(getModel().getTemplateId());
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
        new UpdateResourceTemplateAction(
            getModel().getId(), getView().getSelectedTemplate()) {
                /** {@inheritDoc} */ // TODO Pull method up up into superclass.
                @Override protected void onNoContent(final Response r) {
                    onSuccess(null);
                }
        }.execute();
    }


    /** {@inheritDoc} */
    @Override
    public void onSuccess(final Void result) {
        getView().hide();
        getModel().setTemplateId(getView().getSelectedTemplate());
        getBus().put(new ResourceUpdatedEvent(getModel()));
    }
}
