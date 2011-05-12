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

import java.util.HashMap;
import java.util.Map;




/**
 * API entry point.
 *
 * @author Civic Computing Ltd.
 */
public class API
    extends
        Res {

    /*
     * Property names
     */
    /** APPLICATION_CONTEXT : String. */
    public static final String APPLICATION_CONTEXT = "application.context";
    /** APPLICATION_NAME : String. */
    public static final String APPLICATION_NAME = "application.name";
    /** TIMESTAMP : String. */
    public static final String TIMESTAMP = "timestamp";
    /** CCC_VERSION : String. */
    public static final String CCC_VERSION = "ccc-version";
    /** BUILD_NUMBER : String. */
    public static final String BUILD_NUMBER = "buildNumber";


    /**
     * Rel names for links on this class.
     *
     * @author Civic Computing Ltd.
     */
    public static final class Links {
        private Links() { super(); }

        /** RESOURCES : String. */
        public static final String RESOURCES = "resources";
        /** SEARCH : String. */
        public static final String SEARCH = "search";
        /** SECURITY : String. */
        public static final String SECURITY = "security";
        /** TEMPLATES : String. */
        public static final String TEMPLATES = "templates";
        /** USERS : String. */
        public static final String USERS = "users";
        /** PAGES : String. */
        public static final String PAGES = "pages";
        /** GROUPS : String. */
        public static final String GROUPS = "groups";
        /** FOLDERS : String. */
        public static final String FOLDERS = "folders";
        /** FILES : String. */
        public static final String FILES = "files";
        /** COMMENTS : String. */
        public static final String COMMENTS = "comments";
        /** ALIASES : String. */
        public static final String ALIASES = "aliases";
        /** ACTIONS : String. */
        public static final String ACTIONS = "actions";
        /** ACTIONS : String. */
        public static final String IMAGES = "images";
    }

    private Map<String, String> _props = new HashMap<String, String>();


    /**
     * Link.
     *
     * @return A link to the actions collection.
     */
    public String actions() { return getLink(Links.ACTIONS); }


    /**
     * Link.
     *
     * @return A link to the aliases collection.
     */
    public String aliases() { return getLink(Links.ALIASES); }


    /**
     * Link.
     *
     * @return A link to the comments collection.
     */
    public String comments() { return getLink(Links.COMMENTS); }


    /**
     * Link.
     *
     * @return A link to the files collection.
     */
    public String files() { return getLink(Links.FILES); }


    /**
     * Link.
     *
     * @return A link to the actions collection.
     */
    public String folders() { return getLink(Links.FOLDERS); }


    /**
     * Link.
     *
     * @return A link to the groups collection.
     */
    public String groups() { return getLink(Links.GROUPS); }


    /**
     * Link.
     *
     * @return A link to the pages collection.
     */
    public String pages() { return getLink(Links.PAGES); }


    /**
     * Link.
     *
     * @return A link to the resources collection.
     */
    public String resources() { return getLink(Links.RESOURCES); }


    /**
     * Link.
     *
     * @return A link to the search collection.
     */
    public String search() { return getLink(Links.SEARCH); }


    /**
     * Link.
     *
     * @return A link to the security collection.
     */
    public String security() { return getLink(Links.SECURITY); }


    /**
     * Link.
     *
     * @return A link to the templates collection.
     */
    public String templates() { return getLink(Links.TEMPLATES); }


    /**
     * Link.
     *
     * @return A link to the users collection.
     */
    public String users() { return getLink(Links.USERS); }


    /**
     * Link.
     *
     * @return A link to the images collection.
     */
    public String images() { return getLink(Links.IMAGES); }


    /**
     * Accessor.
     *
     * @return Returns the props.
     */
    public Map<String, String> getProps() {
        return _props;
    }


    /**
     * Mutator.
     *
     * @param props The props to set.
     */
    public void setProps(final Map<String, String> props) {
        _props = props;
    }
}
