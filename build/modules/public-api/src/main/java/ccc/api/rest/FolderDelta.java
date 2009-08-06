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
package ccc.api.rest;

import ccc.api.DBC;
import ccc.api.ID;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.Jsonable;


/**
 * Delta for a folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDelta implements Jsonable {

    private final String _sortOrder;
    private final ID     _indexPage;


    /**
     * Constructor.
     *
     * @param sortOrder The sort order for the folder's children.
     * @param indexPage The folder index page (may be NULL).
     */
    public FolderDelta(final String sortOrder, final ID indexPage) {
        DBC.require().notNull(sortOrder);
        _sortOrder = sortOrder;
        _indexPage = indexPage;
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
    public final ID getIndexPage() {
        return _indexPage;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.SORT_ORDER, _sortOrder);
        json.set(JsonKeys.INDEX_PAGE_ID, _indexPage);
    }
}
