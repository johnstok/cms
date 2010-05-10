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

import ccc.api.core.Page;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;


/**
 * Remote action for field validation.
 *
 * @author Civic Computing Ltd.
 */
public class ValidateFieldAction
    extends
        RemotingAction {

    private final Page _page;


    /**
     * Constructor.
     *
     * @param page The page to validate.
     */
    public ValidateFieldAction(final Page page) {
        super(USER_ACTIONS.validatePageFields(), HttpMethod.POST);
        _page = page;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return GlobalsImpl.getAPI().getLink(Page.VALIDATOR);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _page.toJson(json);
        return json.toString();
    }
}
