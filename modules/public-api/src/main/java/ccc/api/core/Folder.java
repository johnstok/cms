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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.api.types.DBC;
import ccc.api.types.ResourceName;
import ccc.api.types.URIBuilder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * A new folder.
 *
 * @author Civic Computing Ltd.
 */
public class Folder
    extends
        Resource {

    private UUID _indexPage;
    private UUID _defaultPage;
    private String _sortOrder;
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
     * Constructor.
     *
     * @param sortOrder The sort order for the folder's children.
     * @param indexPage The folder index page (may be NULL).
     * @param sortList The list of children for this folder, in sorted order.
     */
    public Folder(final String sortOrder,
                     final UUID indexPage,
                     final Collection<String> sortList) {
        DBC.require().notNull(sortOrder);
        _sortOrder = sortOrder;
        _indexPage = indexPage;
        _sortList = sortList;
    }


    /**
     * Accessor.
     *
     * @return Returns the indexPage.
     */
    public UUID getIndexPage() {
        return _indexPage;
    }


    /**
     * Mutator.
     *
     * @param indexPage The indexPage to set.
     */
    public void setIndexPage(final UUID indexPage) {
        _indexPage = indexPage;
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
     * Accessor.
     *
     * @return Returns the sortOrder.
     */
    public final String getSortOrder() {
        return _sortOrder;
    }


    /**
     * Mutator.
     *
     * @param sortOrder The sortOrder to set.
     */
    public void setSortOrder(final String sortOrder) {
        _sortOrder = sortOrder;
    }


    /**
     * Mutator.
     *
     * @param sortList The sortList to set.
     */
    public void setSortList(final Collection<String> sortList) {
        _sortList = sortList;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);

        json.set(JsonKeys.SORT_ORDER, _sortOrder);
        json.set(JsonKeys.INDEX_PAGE_ID, _indexPage);
        json.setStrings(JsonKeys.SORT_LIST, _sortList);
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);

        _sortOrder = json.getString(JsonKeys.SORT_ORDER);
        _indexPage = json.getId(JsonKeys.INDEX_PAGE_ID);
        _sortList = json.getStrings(JsonKeys.SORT_LIST);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public static String list() {
        return ccc.api.core.ResourceIdentifiers.Folder.COLLECTION;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param id
     * @return
     */
    public static URIBuilder childrenFolder(final UUID id) {
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Folder.FOLDER_CHILDREN);
//            .build("id", id.toString());
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public static String roots() {
        return ccc.api.core.ResourceIdentifiers.Folder.ROOTS;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param folderId
     * @param encode
     * @return
     */
    public static URIBuilder exists(final UUID folderId,
                                final ResourceName resourceName) {
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("id", new String[] {folderId.toString()});
        params.put("name", new String[] {resourceName.toString()});
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Folder.EXISTS);
//            .build(params, new NormalisingEncoder());
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public URIBuilder self() {
        return
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Folder.ELEMENT);
//            .build("id", getId().toString());
    }
}
