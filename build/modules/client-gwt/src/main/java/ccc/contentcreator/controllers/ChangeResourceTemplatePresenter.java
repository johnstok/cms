/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.controllers;

import java.util.Collection;

import ccc.contentcreator.actions.UpdateResourceTemplateAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.AbstractPresenter;
import ccc.contentcreator.client.CommandResponseHandler;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.EventBus;
import ccc.contentcreator.client.IGlobals;
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
                               final IGlobals globals,
                               final EventBus bus,
                               final ChangeResourceTemplate view,
                               final ResourceSummaryModelData model,
                               final Collection<TemplateSummary> templates) {
        super(globals, bus, view, model);
        getView().setPresenter(this);
        getView().setTemplates(templates);
        getView().setSelectedTemplate(getModel().getTemplateId());
        getView().show();
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
