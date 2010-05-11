/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

package ccc.web.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.core.API;
import ccc.api.core.ResourceIdentifiers.Action;
import ccc.api.core.ResourceIdentifiers.Alias;
import ccc.api.core.ResourceIdentifiers.Comment;
import ccc.api.core.ResourceIdentifiers.File;
import ccc.api.core.ResourceIdentifiers.Folder;
import ccc.api.core.ResourceIdentifiers.Group;
import ccc.api.core.ResourceIdentifiers.Page;
import ccc.api.core.ResourceIdentifiers.Resource;
import ccc.api.core.ResourceIdentifiers.SearchEngine;
import ccc.api.core.ResourceIdentifiers.Security;
import ccc.api.core.ResourceIdentifiers.Template;
import ccc.api.core.ResourceIdentifiers.User;


/**
 * This class lists the API collections available.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class ServicesImpl {

    @GET
    public API isLoggedIn() {
        final API api = new API();
        api.addLink(API.ACTIONS,   Action.COLLECTION);
        api.addLink(API.ALIASES,   Alias.COLLECTION);
        api.addLink(API.COMMENTS,  Comment.COLLECTION);
        api.addLink(API.FILES,     File.COLLECTION);
        api.addLink(API.FOLDERS,   Folder.COLLECTION);
        api.addLink(API.GROUPS,    Group.COLLECTION);
        api.addLink(API.PAGES,     Page.COLLECTION);
        api.addLink(API.RESOURCES, Resource.COLLECTION);
        api.addLink(API.SEARCH,    SearchEngine.COLLECTION);
        api.addLink(API.SECURITY,  Security.COLLECTION);
        api.addLink(API.TEMPLATES, Template.COLLECTION);
        api.addLink(API.USERS,     User.COLLECTION);

        api.addLink(ccc.api.core.Folder.ROOTS, Folder.ROOTS);

        api.addLink(
            ccc.api.core.Template.EXISTS,
            ccc.api.core.ResourceIdentifiers.Template.EXISTS);

        api.addLink(
            ccc.api.core.Page.VALIDATOR,
            ccc.api.core.ResourceIdentifiers.Page.VALIDATOR);

        api.addLink(ccc.api.core.Security.PROPERTIES, Security.PROPERTIES);
        api.addLink(ccc.api.core.Security.CURRENT,    Security.CURRENT);
        api.addLink(ccc.api.core.Security.COLLECTION, Security.COLLECTION+"?{-join|&|u,p}");

        return api;
    }
}
