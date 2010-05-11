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

    public static interface Scheduler {
        String START   = "/start";
        String STOP    = "/stop";
        String RUNNING = "/running";
    }

    public static interface SearchEngine {
        String COLLECTION = "/secure/search";
        String FIND       = "/find";
        String SIMILAR    = "/similar";
        String INDEX      = "/index";
    }

    public static interface Security {
        String COLLECTION = "/public/sessions";
        String PROPERTIES = COLLECTION+"/allproperties";
        String CURRENT    = COLLECTION+"/current";
    }

    public static interface Template {
        String COLLECTION = "/secure/templates";
        String ELEMENT    = COLLECTION + "/{id}";
        String EXISTS     = COLLECTION + "/{name}/exists";
        String DELTA      = ELEMENT + "/delta";
    }

    public static interface Page {
        String COLLECTION  = "/secure/pages";
        String VALIDATOR   = COLLECTION + "/validator";
        String ELEMENT     = COLLECTION + "/{id}";
        String WC          = ELEMENT + "/wc";
    }

    public static interface Group {
        String COLLECTION = "/secure/groups";
        String ELEMENT = COLLECTION+"/{id}";
    }

    public static interface Folder {
        String COLLECTION            = "/secure/folders";
        String ELEMENT               = COLLECTION + "/{id}";
        String ROOTS                 = COLLECTION + "/roots";
        String FOLDER_CHILDREN       = ELEMENT + "/folder-children";
        String CHILDREN              = ELEMENT + "/children";
        String ACCESSIBLE_CHILDREN   = ELEMENT + "/accessible-children";
        String CHILDREN_MANUAL_ORDER = ELEMENT + "/children-manual-order";
        String EXISTS                = ELEMENT + "/{name}/exists";
        String DEPRECATED            = COLLECTION + "/deprecated";
        String ROOT_NAME             = ROOTS + "/{name}";
    }

    public static interface File {
        String COLLECTION        = "/secure/files";
        String IMAGES            = COLLECTION + "/images/{id}";
        String ELEMENT           = COLLECTION + "/{id}";
        String BINARY_COLLECTION = COLLECTION + "/bin";
        String BINARY_ELEMENT    = BINARY_COLLECTION + "/{id}";
        String BINARY_WC         = BINARY_ELEMENT + "/wc";
        String BINARY_REVISION   = BINARY_ELEMENT + "/rev";
    }

    public static interface Comment {
        String COLLECTION = "/secure/comments";
        String ELEMENT = COLLECTION+"/{id}";
    }

    public static interface User {
        String  COLLECTION = "/secure/users";
        String  ELEMENT    = COLLECTION + "/{id}";
        String  ME         = COLLECTION + "/me";
        String  EXISTS     = COLLECTION + "/{uname}/exists";
        String  LEGACY     = COLLECTION + "/by-legacy-id/{id}";
        String  METADATA   = COLLECTION + "/metadata/{key}";
        String  DELTA      = ELEMENT + "/delta";
        String  PASSWORD   = ELEMENT + "/password";
    }

    public static interface Action {
        String  COLLECTION  = "/secure/actions";
        String  ELEMENT     = "/{id}";
        String  PENDING     = "/pending";
        String  COMPLETED   = "/completed";
        String  EXECUTE     = "/all";
    }

    public static interface Alias {
        String COLLECTION  = "/secure/aliases";
        String ELEMENT     = COLLECTION + "/{id}";
        String TARGET_NAME = ELEMENT + "/targetname";
    }

    public static interface Resource {

        String COLLECTION         = "/secure/resources";

        String LIST               = COLLECTION + "/list";
        String LOCKED             = COLLECTION + "/locked";
        String SEARCH_PATH_SIMPLE = COLLECTION + "/by-path";
        String SEARCH_PATH        = SEARCH_PATH_SIMPLE + "{path:.*}";
        String SEARCH_LEGACY      = COLLECTION + "/by-legacy-id/{id}";
        String SEARCH_METADATA    = COLLECTION + "/by-metadata-key/{id}";
        String TEXT               = COLLECTION + "/text-content{path:.*}";
        String PATH_SECURE        = COLLECTION + "/by-path-secure{path:.*}";
        String PATH_WC            = COLLECTION + "/by-path-wc{path:.*}";
        String SEARCH             = COLLECTION + "/search/{id}/{title}";

        String ELEMENT            = COLLECTION + "/{id}";

        String DELETE             = ELEMENT + "/delete";
        String PATH               = ELEMENT + "/path";
        String REVISIONS          = ELEMENT + "/revisions";
        String METADATA           = ELEMENT + "/metadata";
        String ACL                = ELEMENT + "/acl";
        String DURATION           = ELEMENT + "/duration";
        String TEMPLATE           = ELEMENT + "/template";
        String LOCK               = ELEMENT + "/lock";
        String UNLOCK             = ELEMENT + "/unlock";
        String UNPUBLISH          = ELEMENT + "/unpublish";
        String PUBLISH            = ELEMENT + "/publish";
        String PARENT             = ELEMENT + "/parent";
        String NAME               = ELEMENT + "/name";
        String EXCLUDE_MM         = ELEMENT + "/exclude-mm";
        String INCLUDE_MM         = ELEMENT + "/include-mm";
        String WC_APPLY           = ELEMENT + "/wc-apply";
        String WC_CLEAR           = ELEMENT + "/wc-clear";
        String WC_CREATE          = ELEMENT + "/wc-create";
        String LOG_ENTRY          = ELEMENT + "/logentry-create";
    }
}
