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

import java.util.Collection;

import ccc.api.core.Resource;
import ccc.api.core.Template;
import ccc.api.types.CommandType;
import ccc.client.core.Editable;
import ccc.client.events.Event;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.remoting.UpdateResourceTemplateAction;
import ccc.client.views.ChangeResourceTemplate;


/**
 * MVP Presenter for changing a resource's template.
 *
 * @author Civic Computing Ltd.
 */
public class ChangeResourceTemplatePresenter
    extends
        AbstractPresenter<ChangeResourceTemplate, ResourceSummaryModelData>
    implements
        Editable {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     * @param templates The templates to choose from.
     */
    public ChangeResourceTemplatePresenter(
                               final ChangeResourceTemplate view,
                               final ResourceSummaryModelData model,
                               final Collection<Template> templates) {
        super(view, model);

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
        final Resource r = new Resource();
        r.setId(getModel().getId());
        r.setTemplate(getView().getSelectedTemplate());
        r.addLink(
            Resource.TEMPLATE,
            getModel().getDelegate().getLink(Resource.TEMPLATE));
        new UpdateResourceTemplateAction(r).execute();
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {

        switch (event.getType()) {
            case RESOURCE_CHANGE_TEMPLATE:
                dispose();
                break;

            default:
                break;
        }
    }
}
