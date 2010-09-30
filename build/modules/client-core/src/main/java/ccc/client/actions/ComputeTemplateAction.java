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
package ccc.client.actions;

import ccc.api.core.Template;
import ccc.api.types.Link;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Determine the template for a resource.
 *
 * @author Civic Computing Ltd.
 */
public final class ComputeTemplateAction
    extends
        RemotingAction<Template> {

    private final Link _resourceLink;
    private final String _name;


    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     * @param resource The resource to invoke.
     */
    public ComputeTemplateAction(final String actionName,
                                 final Link resource) {
        super(actionName);
        _name         = actionName;
        _resourceLink = resource;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Template> callback) {
        return new Request(
            HttpMethod.GET,
            Globals.API_URL
                + _resourceLink.build(InternalServices.ENCODER),
            "",
            new CallbackResponseHandler<Template>(
                _name,
                callback,
                new Parser<Template>() {
                    @Override public Template parse(final Response response) {
                        return parseTemplate(response);
                    }}));
    }
}
