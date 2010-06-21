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

import java.util.HashMap;
import java.util.Map;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.client.core.HttpMethod;
import ccc.client.core.Response;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.TemplateSerializer;


/**
 * Create a template.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateTemplateAction
    extends
        RemotingAction {

    private final Template _delta;

    /**
     * Constructor.
     *
     * @param delta The template's details.
     */
    public CreateTemplateAction(final Template delta) {
        super(UI_CONSTANTS.createTemplate(), HttpMethod.POST);
        _delta = DBC.require().notNull(delta);
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("count", new String[] {"999"});
        params.put("page", new String[] {"1"});
        return
            new Link(GlobalsImpl.getAPI().templates())
            .build(params, new GWTTemplateEncoder());
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = new GwtJson();
        new TemplateSerializer().write(json, _delta);
        return json.toString();
    }

    /**
     * Handle the result of a successful call.
     *
     * @param template The newly created template.
     */
    protected abstract void execute(ResourceSummary template);
}
