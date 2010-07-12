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

import ccc.api.core.Template;
import ccc.api.types.Link;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GlobalsImpl;

import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class TemplateNameExistsAction
    extends
        RemotingAction {

    private final String _name;

    /**
     * Constructor.
     *
     * @param name The name of the template.
     */
    public TemplateNameExistsAction(final String name) {
        super(USER_ACTIONS.checkUniqueTemplateName());
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            new Link(GlobalsImpl.getAPI().getLink(Template.EXISTS))
            .build("name", _name, new GWTTemplateEncoder());
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final boolean nameExists =
            JSONParser.parse(response.getText()).isBoolean().booleanValue();
        execute(nameExists);
    }

    /**
     * Handle a successful execution.
     *
     * @param nameExists True if the template name exists, false otherwise.
     */
    protected abstract void execute(boolean nameExists);
}
