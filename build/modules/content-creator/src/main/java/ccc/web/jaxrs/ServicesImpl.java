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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.core.API;
import ccc.api.synchronous.ResourceIdentifiers.Action;
import ccc.api.synchronous.ResourceIdentifiers.Alias;
import ccc.api.synchronous.ResourceIdentifiers.Comment;
import ccc.api.synchronous.ResourceIdentifiers.File;
import ccc.api.synchronous.ResourceIdentifiers.Folder;
import ccc.api.synchronous.ResourceIdentifiers.Group;
import ccc.api.synchronous.ResourceIdentifiers.Page;
import ccc.api.synchronous.ResourceIdentifiers.Resource;
import ccc.api.synchronous.ResourceIdentifiers.SearchEngine;
import ccc.api.synchronous.ResourceIdentifiers.Security;
import ccc.api.synchronous.ResourceIdentifiers.Template;
import ccc.api.synchronous.ResourceIdentifiers.User;


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

        api.addLink(API.Links.ACTIONS,   Action.COLLECTION);
        api.addLink(API.Links.ALIASES,   Alias.COLLECTION);
        api.addLink(API.Links.COMMENTS,  Comment.COLLECTION);
        api.addLink(API.Links.FILES,     File.COLLECTION);
        api.addLink(API.Links.FOLDERS,   Folder.COLLECTION);
        api.addLink(API.Links.GROUPS,    Group.COLLECTION);
        api.addLink(API.Links.PAGES,     Page.COLLECTION);
        api.addLink(API.Links.RESOURCES, Resource.COLLECTION);
        api.addLink(API.Links.SEARCH,    SearchEngine.COLLECTION);
        api.addLink(API.Links.SECURITY,  Security.COLLECTION);
        api.addLink(API.Links.USERS,     User.COLLECTION);
        api.addLink(API.Links.TEMPLATES, Template.COLLECTION
                                         + "?{-join|&|count,page}");
        api.addLink(API.Links.IMAGES,    File.IMAGES
                                         + "?{-join|&|count,page,sort,order}");

        api.addLink(
            ccc.api.core.Folder.Links.ROOTS,
            Folder.ROOTS);

        api.addLink(
            ccc.api.core.Resource.Links.SEARCH,
            Resource.SEARCH2 + "?{-join|&|count,page,sort,order}");

        api.addLink(
            ccc.api.core.Template.Links.EXISTS,
            ccc.api.synchronous.ResourceIdentifiers.Template.EXISTS);

        api.addLink(
            ccc.api.core.Page.Links.VALIDATOR,
            ccc.api.synchronous.ResourceIdentifiers.Page.VALIDATOR);

        api.addLink(
            ccc.api.core.User.Links.CURRENT,
            Security.CURRENT);
        api.addLink(
            ccc.api.core.User.Links.COLLECTION,
            Security.COLLECTION+"?{-join|&|u,p}");

        api.addLink(
            ccc.api.core.File.Links.LIST_BINARY,
            File.BINARY_COLLECTION);

        final Map<String, String> props = new HashMap<String, String>();
        props.put(API.BUILD_NUMBER, CCCProperties.buildNumber());
        props.put(API.CCC_VERSION, CCCProperties.version());
        props.put(API.TIMESTAMP, CCCProperties.timestamp());
        props.put(API.APPLICATION_NAME, CCCProperties.getAppName());
        props.put(API.APPLICATION_CONTEXT, CCCProperties.getContextName());
        api.setProps(props);

        return api;
    }
}
