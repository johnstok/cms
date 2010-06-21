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
package ccc.client.gwt.remoting;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.plugins.s11n.json.TemplateSerializer;

import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ComputeTemplateAction
    extends
        RemotingAction {

    private final ResourceSummary _resource;
    private final String _name;

    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     * @param resource The resource to invoke.
     */
    public ComputeTemplateAction(final String actionName,
                                 final ResourceSummary resource) {
        _name     = actionName;
        _resource = resource;
    }

    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return new Request(
            HttpMethod.GET,
            Globals.API_URL + _resource.uriTemplate().build(new GWTTemplateEncoder()),
            "",
            new ResponseHandlerAdapter(_name) {

                /** {@inheritDoc} */
                @Override
                public void onNoContent(final Response response) {
                    noTemplate();
                }

                /** {@inheritDoc} */
                @Override
                public void onOK(final Response response) {
                    final Template ts =
                        new TemplateSerializer().read(
                            new GwtJson(
                                JSONParser.parse(response.getText())
                                .isObject()));
                    template(ts);
                }

            });
    }

    protected abstract void noTemplate();
    protected abstract void template(Template t);
}
