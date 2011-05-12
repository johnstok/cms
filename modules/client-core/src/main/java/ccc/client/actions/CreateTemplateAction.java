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

import java.util.HashMap;
import java.util.Map;

import ccc.api.core.Template;
import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Create a template.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateTemplateAction
    extends
        RemotingAction<Template> {

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
            new Link(InternalServices.api.templates())
            .build(params, InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Template t) {
        execute(t);
    }


    /** {@inheritDoc} */
    @Override
    protected Template parse(final Response response) {
        return readTemplate(response);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return writeTemplate(_delta);
    }


    /**
     * Handle the result of a successful call.
     *
     * @param template The newly created template.
     */
    protected abstract void execute(Template template);
}
