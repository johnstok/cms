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
package ccc.api.core;


/**
 * Resource identifiers for the API.
 *
 * @deprecated This interface will be removed in a future release and should not
 *  be used.
 * @author Civic Computing Ltd.
 */
@Deprecated
public interface ResourceIdentifiers {

    /**
     * URIs for the scheduler.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Scheduler {
        /** SCHEDULER : String. */
        String SCHEDULER   = "/scheduler";
    }

    /**
     * URIs for the search API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface SearchEngine {
        /** COLLECTION : String. */
        String COLLECTION = "/secure/search";
        /** FIND : String. */
        String FIND       = "/find";
        /** SIMILAR : String. */
        String SIMILAR    = "/similar";
        /** INDEX : String. */
        String INDEX      = "/index";
    }

    /**
     * URIs for the security API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Security {
        /** COLLECTION : String. */
        String COLLECTION = "/public/sessions";
        /** PROPERTIES : String. */
        String PROPERTIES = COLLECTION+"/allproperties";
        /** CURRENT : String. */
        String CURRENT    = COLLECTION+"/current";
    }

    /**
     * URIs for the templates API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Template {
        /** COLLECTION : String. */
        String COLLECTION = "/secure/templates";
        /** ELEMENT : String. */
        String ELEMENT    = COLLECTION + "/{id}";
        /** EXISTS : String. */
        String EXISTS     = COLLECTION + "/{name}/exists";
    }

    /**
     * URIs for the page API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Page {
        /** COLLECTION : String. */
        String COLLECTION  = "/secure/pages";
        /** VALIDATOR : String. */
        String VALIDATOR   = COLLECTION + "/validator";
        /** ELEMENT : String. */
        String ELEMENT     = COLLECTION + "/{id}";
        /** WC : String. */
        String WC          = ELEMENT + "/wc";
    }

    /**
     * URIs for the groups API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Group {
        /** COLLECTION : String. */
        String COLLECTION = "/secure/groups";
        /** ELEMENT : String. */
        String ELEMENT = COLLECTION+"/{id}";
    }

    /**
     * URIs for the folders API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Folder {
        /** COLLECTION : String. */
        String COLLECTION            = "/secure/folders";
        /** ELEMENT : String. */
        String ELEMENT               = COLLECTION + "/{id}";
        /** ROOTS : String. */
        String ROOTS                 = COLLECTION + "/roots";
        /** FOLDER_CHILDREN : String. */
        String FOLDER_CHILDREN       = ELEMENT + "/folder-children";
        /** CHILDREN : String. */
        String CHILDREN              = ELEMENT + "/children";
        /** ACCESSIBLE_CHILDREN : String. */
        String ACCESSIBLE_CHILDREN   = ELEMENT + "/accessible-children";
        /** CHILDREN_MANUAL_ORDER : String. */
        String CHILDREN_MANUAL_ORDER = ELEMENT + "/children-manual-order";
        /** EXISTS : String. */
        String EXISTS                = ELEMENT + "/{name}/exists";
        /** DEPRECATED : String. */
        String DEPRECATED            = COLLECTION + "/deprecated";
        /** ROOT_NAME : String. */
        String ROOT_NAME             = ROOTS + "/{name}";
    }

    /**
     * URIs for the files API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface File {
        /** COLLECTION : String. */
        String COLLECTION        = "/secure/files";
        /** IMAGES : String. */
        String IMAGES            = COLLECTION + "/images/{id}";
        /** ELEMENT : String. */
        String ELEMENT           = COLLECTION + "/{id}";
        /** BINARY_COLLECTION : String. */
        String BINARY_COLLECTION = COLLECTION + "/bin";
        /** BINARY_ELEMENT : String. */
        String BINARY_ELEMENT    = BINARY_COLLECTION + "/{id}";
        /** BINARY_WC : String. */
        String BINARY_WC         = BINARY_ELEMENT + "/wc";
        /** BINARY_REVISION : String. */
        String BINARY_REVISION   = BINARY_ELEMENT + "/rev";
    }

    /**
     * URIs for the comments API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Comment {
        /** COLLECTION : String. */
        String COLLECTION = "/secure/comments";
        /** ELEMENT : String. */
        String ELEMENT = COLLECTION+"/{id}";
    }

    /**
     * URIs for the users API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface User {
        /** COLLECTION : String. */
        String  COLLECTION = "/secure/users";
        /** ELEMENT : String. */
        String  ELEMENT    = COLLECTION + "/{id}";
        /** ME : String. */
        String  ME         = COLLECTION + "/me";
        /** EXISTS : String. */
        String  EXISTS     = COLLECTION + "/{uname}/exists";
        /** LEGACY : String. */
        String  LEGACY     = COLLECTION + "/by-legacy-id/{id}";
        /** METADATA : String. */
        String  METADATA   = COLLECTION + "/metadata/{key}";
        /** DELTA : String. */
        String  DELTA      = ELEMENT + "/delta";
        /** PASSWORD : String. */
        String  PASSWORD   = ELEMENT + "/password";
    }

    /**
     * URIs for the actions API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Action {
        /** COLLECTION : String. */
        String  COLLECTION  = "/secure/actions";
        /** ELEMENT : String. */
        String  ELEMENT     = "/{id}";
        /** COMPLETED : String. */
        String  COMPLETED   = "/completed";
        /** EXECUTE : String. */
        String  EXECUTE     = "/all";
    }

    /**
     * URIs for the aliases API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Alias {
        /** COLLECTION : String. */
        String COLLECTION  = "/secure/aliases";
        /** ELEMENT : String. */
        String ELEMENT     = COLLECTION + "/{id}";
        /** TARGET_NAME : String. */
        String TARGET_NAME = ELEMENT + "/targetname";
    }

    /**
     * URIs for the resources API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Resource {

        /** COLLECTION : String. */
        String COLLECTION         = "/secure/resources";

        /** LOCKED : String. */
        String LOCKED             = COLLECTION + "/locked";
        /** SEARCH_PATH_SIMPLE : String. */
        String SEARCH_PATH_SIMPLE = COLLECTION + "/by-path";
        /** SEARCH_PATH : String. */
        String SEARCH_PATH        = SEARCH_PATH_SIMPLE + "{path:.*}";
        /** SEARCH_LEGACY : String. */
        String SEARCH_LEGACY      = COLLECTION + "/by-legacy-id/{id}";
        /** SEARCH_METADATA : String. */
        String SEARCH_METADATA    = COLLECTION + "/by-metadata-key/{id}";
        /** TEXT : String. */
        String TEXT               = COLLECTION + "/text-content{path:.*}";
        /** PATH_SECURE : String. */
        String PATH_SECURE        = COLLECTION + "/by-path-secure{path:.*}";
        /** PATH_WC : String. */
        String PATH_WC            = COLLECTION + "/by-path-wc{path:.*}";
        /** SEARCH : String. */
        String SEARCH             = COLLECTION + "/search/{id}/{title}";

        /** ELEMENT : String. */
        String ELEMENT            = COLLECTION + "/{id}";

        /** PATH : String. */
        String PATH               = ELEMENT + "/path";
        /** REVISIONS : String. */
        String REVISIONS          = ELEMENT + "/revisions";
        /** METADATA : String. */
        String METADATA           = ELEMENT + "/metadata";
        /** ACL : String. */
        String ACL                = ELEMENT + "/acl";
        /** DURATION : String. */
        String DURATION           = ELEMENT + "/duration";
        /** TEMPLATE : String. */
        String TEMPLATE           = ELEMENT + "/template";
        /** LOCK : String. */
        String LOCK               = ELEMENT + "/lock";
        /** PUBLISH : String. */
        String PUBLISH            = ELEMENT + "/publish";
        /** PARENT : String. */
        String PARENT             = ELEMENT + "/parent";
        /** NAME : String. */
        String NAME               = ELEMENT + "/name";
        /** EXCLUDE_MM : String. */
        String EXCLUDE_MM         = ELEMENT + "/exclude-mm";
        /** INCLUDE_MM : String. */
        String INCLUDE_MM         = ELEMENT + "/include-mm";
        /** WC : String. */
        String WC                 = ELEMENT + "/wc";
        /** LOG_ENTRY : String. */
        String LOG_ENTRY          = ELEMENT + "/logentry-create";
    }
}
