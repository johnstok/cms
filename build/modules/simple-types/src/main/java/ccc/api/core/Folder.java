/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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

import java.util.Collection;
import java.util.UUID;

import ccc.api.types.ResourceName;


/**
 * A new folder.
 *
 * @author Civic Computing Ltd.
 */
public class Folder
    extends
        Resource {

    private UUID _index;
    private UUID _defaultPage;
    private Collection<String> _sortList;


    /**
     * Constructor.
     */
    public Folder() { super(); }


    /**
     * Constructor.
     *
     * @param parentId The folder's parent.
     * @param name     The folder's name.
     */
    public Folder(final UUID parentId, final ResourceName name) {
        setParent(parentId);
        setName(name);
    }


    /**
     * Accessor.
     *
     * @return Returns the index resource.
     */
    public UUID getIndex() {
        return _index;
    }


    /**
     * Mutator.
     *
     * @param index The index resource to set.
     */
    public void setIndex(final UUID index) {
        _index = index;
    }


    /**
     * Mutator.
     *
     * @param defaultPage The defaultPage to set.
     */
    public void setDefaultPage(final UUID defaultPage) {
        _defaultPage = defaultPage;
    }


    /**
     * Accessor.
     *
     * @return Returns the defaultPage.
     */
    public UUID getDefaultPage() {
        return _defaultPage;
    }


    /**
     * Accessor.
     *
     * @return Returns the sort list.
     */
    public final Collection<String> getSortList() {
        return _sortList;
    }


    /**
     * Mutator.
     *
     * @param sortList The sortList to set.
     */
    public void setSortList(final Collection<String> sortList) {
        _sortList = sortList;
    }


    /** ROOTS : String. */
    public static final String ROOTS = "roots";
    /** IMAGES : String. */
    public static final String IMAGES = "images";
    /** EXISTS : String. */
    public static final String EXISTS = "exists";
}
