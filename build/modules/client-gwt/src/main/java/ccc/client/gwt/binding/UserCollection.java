/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.client.gwt.binding;

import java.util.ArrayList;
import java.util.Collection;

import ccc.api.core.PagedCollection;
import ccc.api.core.User;
import ccc.api.types.Link;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserCollection
    extends
        PagedCollection<User> {

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String me() {
        return getLink("me");
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param pageNo
     * @param pageSize
     * @param sort
     * @param order
     * @param email
     * @param username
     * @param groups
     * @return
     */
    // FIXME: Should accept a UserCriteria and encode internally.
    public String list(final int pageNo,
                        final int pageSize,
                        final String sort,
                        final String order,
                        final String email,
                        final String username,
                        final String groups) {
        final StringBuilder path = new StringBuilder();
        path.append(getLink("self"));
        path.append("?page="+pageNo);
        path.append("&count="+pageSize);
        path.append("&order="+order);
        if (null!=sort) { path.append("&sort="+sort); }
        if (null != email) {
            path.append("&email="+email);
        }
        if (null != username) {
            path.append("&username="+username);
        }
        if (null != groups) {
            path.append("&groups="+groups);
        }
        return path.toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param username
     * @return
     */
    public String exists(final String username) {
        return
            new Link(getLink("exists"))
            .build("uname", username, new GWTTemplateEncoder());
    }



    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);
        _totalCount = json.getLong(JsonKeys.SIZE);
        final Collection<Json> elementJson = json.getCollection(JsonKeys.ELEMENTS);
        final ArrayList<User> elements = new ArrayList<User>();
        for (final Json element : elementJson) {
            final User u = new User();
            u.fromJson(element);
            elements.add(u);
        }
        _elements.addAll(elements);
    }
}
