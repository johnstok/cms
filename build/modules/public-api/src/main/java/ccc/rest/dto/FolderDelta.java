/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.DBC;


/**
 * Delta for a folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDelta implements Serializable, Jsonable {

    private final String _sortOrder;
    private final UUID     _indexPage;
    private final Collection<String> _sortList;


    /**
     * Constructor.
     *
     * @param sortOrder The sort order for the folder's children.
     * @param indexPage The folder index page (may be NULL).
     * @param sortList The list of children for this folder, in sorted order.
     */
    public FolderDelta(final String sortOrder,
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
     * @return Returns the sortOrder.
     */
    public final String getSortOrder() {
        return _sortOrder;
    }


    /**
     * Accessor.
     *
     * @return Returns the indexPage.
     */
    public final UUID getIndexPage() {
        return _indexPage;
    }


    /**
     * Accessor.
     *
     * @return Returns the sort list.
     */
    public final Collection<String> getSortList() {
        return _sortList;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.SORT_ORDER, _sortOrder);
        json.set(JsonKeys.INDEX_PAGE_ID, _indexPage);
        json.setStrings(JsonKeys.SORT_LIST, _sortList);
    }
}
