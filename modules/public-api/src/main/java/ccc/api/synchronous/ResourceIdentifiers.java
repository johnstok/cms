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
package ccc.api.synchronous;


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
    public static final class Scheduler {
        private Scheduler() { super(); }

        /** SCHEDULER : String. */
        public static final String SCHEDULER =
            "/scheduler";
    }


    /**
     * URIs for the search API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class SearchEngine {
        private SearchEngine() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION =
            "/secure/search";
        /** FIND : String. */
        public static final String FIND       =
            "/find";
        /** FIND_SORT : String. */
        public static final String FIND_SORT  =
            "/find_sort";
        /** SIMILAR : String. */
        public static final String SIMILAR    =
            "/similar";
        /** INDEX : String. */
        public static final String INDEX      =
            "/index";
    }


    /**
     * URIs for the security API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Security {
        private Security() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION =
            "/public/sessions";
        /** CURRENT : String. */
        public static final String CURRENT    =
            COLLECTION+"/current";
    }


    /**
     * URIs for the templates API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Template {
        private Template() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION =
            "/secure/templates";
        /** ELEMENT : String. */
        public static final String ELEMENT    =
            COLLECTION + "/{id}";
        /** EXISTS : String. */
        public static final String EXISTS     =
            COLLECTION + "/{name}/exists";
        /** REVISION : String. */
        public static final String REVISION   =
            ELEMENT + "/{revision}";
    }


    /**
     * URIs for the page API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Page {
        private Page() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION =
            "/secure/pages";
        /** VALIDATOR : String. */
        public static final String VALIDATOR  =
            COLLECTION + "/validator";
        /** SEARCH : String. */
        public static final String SEARCH     =
            COLLECTION + "/search";
        /** ELEMENT : String. */
        public static final String ELEMENT    =
            COLLECTION + "/{id}";
        /** WC : String. */
        public static final String WC         =
            ELEMENT + "/wc";
    }


    /**
     * URIs for the groups API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Group {
        private Group() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION =
            "/secure/groups";
        /** ELEMENT : String. */
        public static final String ELEMENT    =
            COLLECTION+"/{id}";
    }


    /**
     * URIs for the folders API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Folder {
        private Folder() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION            =
            "/secure/folders";
        /** ELEMENT : String. */
        public static final String ELEMENT               =
            COLLECTION + "/{id}";
        /** ROOTS : String. */
        public static final String ROOTS                 =
            COLLECTION + "/roots";
        /** FOLDER_CHILDREN : String. */
        public static final String FOLDER_CHILDREN       =
            ELEMENT + "/folder-children";
        /** CHILDREN : String. */
        public static final String CHILDREN              =
            ELEMENT + "/children";
        /** ACCESSIBLE_CHILDREN : String. */
        public static final String ACCESSIBLE_CHILDREN   =
            ELEMENT + "/accessible-children";
        /** CHILDREN_MANUAL_ORDER : String. */
        public static final String CHILDREN_MANUAL_ORDER =
            ELEMENT + "/children-manual-order";
        /** EXISTS : String. */
        public static final String EXISTS                =
            ELEMENT + "/{name}/exists";
        /** DEPRECATED : String. */
        public static final String DEPRECATED            =
            COLLECTION + "/deprecated";
        /** ROOT_NAME : String. */
        public static final String ROOT_NAME             =
            ROOTS + "/{name}";
    }


    /**
     * URIs for the files API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class File {
        private File() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION        =
            "/secure/files";
        /** IMAGES : String. */
        public static final String IMAGES            =
            COLLECTION + "/images/";
        /** ELEMENT : String. */
        public static final String ELEMENT           =
            COLLECTION + "/{id}";
        /** BINARY_COLLECTION : String. */
        public static final String BINARY_COLLECTION =
            COLLECTION + "/bin";
        /** BINARY_ELEMENT : String. */
        public static final String BINARY_ELEMENT    =
            BINARY_COLLECTION + "/{id}";
        /** BINARY_WC : String. */
        public static final String BINARY_WC         =
            BINARY_ELEMENT + "/wc";
        /** BINARY_REVISION : String. */
        public static final String BINARY_REVISION   =
            BINARY_ELEMENT + "/rev";
    }


    /**
     * URIs for the comments API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Comment {
        private Comment() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION =
            "/secure/comments";
        /** ELEMENT : String. */
        public static final String ELEMENT    =
            COLLECTION+"/{id}";
    }


    /**
     * URIs for the users API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class User {
        private User() { super(); }

        /** COLLECTION : String. */
        public static final String  COLLECTION =
            "/secure/users";
        /** ELEMENT : String. */
        public static final String  ELEMENT    =
            COLLECTION + "/{id}";
        /** ME : String. */
        public static final String  ME         =
            COLLECTION + "/me";
        /** EXISTS : String. */
        public static final String  EXISTS     =
            COLLECTION + "/exists";
        /** LEGACY : String. */
        public static final String  LEGACY     =
            COLLECTION + "/by-legacy-id/{id}";
        /** METADATA : String. */
        public static final String  METADATA   =
            COLLECTION + "/metadata/{key}";
        /** DELTA : String. */
        public static final String  DELTA      =
            ELEMENT + "/delta";
        /** PASSWORD : String. */
        public static final String  PASSWORD   =
            ELEMENT + "/password";
        /** TOKEN : String. */
        public static final String  TOKEN   =
            COLLECTION + "/token";
        /** TOKEN : String. */
        public static final String  RESET_PASSWORD   =
            COLLECTION + "/resetpassword";

    }


    /**
     * URIs for the actions API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Action {
        private Action() { super(); }

        /** COLLECTION : String. */
        public static final String  COLLECTION  =
            "/secure/actions";
        /** ELEMENT : String. */
        public static final String  ELEMENT     =
            "/{id}";
        /** EXECUTE : String. */
        public static final String  EXECUTE     =
            "/all";
    }


    /**
     * URIs for the aliases API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Alias {
        private Alias() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION  =
            "/secure/aliases";
        /** ELEMENT : String. */
        public static final String ELEMENT     =
            COLLECTION + "/{id}";
        /** TARGET_NAME : String. */
        public static final String TARGET_NAME =
            ELEMENT + "/targetname";
    }


    /**
     * URIs for the resources API.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Resource {
        private Resource() { super(); }

        /** COLLECTION : String. */
        public static final String COLLECTION         =
            "/secure/resources";
        /** LOCKED : String. */
        public static final String LOCKED             =
            COLLECTION + "/locked";
        /** SEARCH_PATH_SIMPLE : String. */
        public static final String SEARCH_PATH_SIMPLE =
            COLLECTION + "/by-path";
        /** SEARCH_PATH : String. */
        public static final String SEARCH_PATH        =
            SEARCH_PATH_SIMPLE + "{path:.*}";
        /** SEARCH_LEGACY : String. */
        public static final String SEARCH_LEGACY      =
            COLLECTION + "/by-legacy-id/{id}";
        /** SEARCH_METADATA : String. */
        public static final String SEARCH_METADATA    =
            COLLECTION + "/by-metadata-key/{id}";
        /** TEXT_SIMPLE : String. */
        public static final String TEXT_SIMPLE        =
            COLLECTION + "/text-content";
        /** TEXT : String. */
        public static final String TEXT               =
            TEXT_SIMPLE + "{path:.*}";
        /** PATH_SECURE : String. */
        public static final String PATH_SECURE        =
            COLLECTION + "/by-path-secure{path:.*}";
        /** PATH_WC : String. */
        public static final String PATH_WC            =
            COLLECTION + "/by-path-wc{path:.*}";
        /** SEARCH : String. */
        public static final String SEARCH             =
            COLLECTION + "/search/{id}/{title}";
        /** SEARCH : String. */
        public static final String SEARCH2            =
            COLLECTION + "/search2";
        /** ELEMENT : String. */
        public static final String ELEMENT            =
            COLLECTION + "/{id}";
        /** PATH : String. */
        public static final String PATH               =
            ELEMENT + "/path";
        /** REVISIONS : String. */
        public static final String REVISIONS          =
            ELEMENT + "/revisions";
        /** METADATA : String. */
        public static final String METADATA           =
            ELEMENT + "/metadata";
        /** ACL : String. */
        public static final String ACL                =
            ELEMENT + "/acl";
        /** DURATION : String. */
        public static final String DURATION           =
            ELEMENT + "/duration";
        /** TEMPLATE : String. */
        public static final String TEMPLATE           =
            ELEMENT + "/template";
        /** LOCK : String. */
        public static final String LOCK               =
            ELEMENT + "/lock";
        /** PUBLISH : String. */
        public static final String PUBLISH            =
            ELEMENT + "/publish";
        /** PARENT : String. */
        public static final String PARENT             =
            ELEMENT + "/parent";
        /** NAME : String. */
        public static final String NAME               =
            ELEMENT + "/name";
        /** EXCLUDE_MM : String. */
        public static final String EXCLUDE_MM         =
            ELEMENT + "/exclude-mm";
        /** INCLUDE_MM : String. */
        public static final String INCLUDE_MM         =
            ELEMENT + "/include-mm";
        /** WC : String. */
        public static final String WC                 =
            ELEMENT + "/wc";
        /** LOG_ENTRY : String. */
        public static final String LOG_ENTRY          =
            ELEMENT + "/logentry-create";
    }
}
