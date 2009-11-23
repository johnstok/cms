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
import java.util.UUID;

import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ResourceName;


/**
 * A new folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDto
    extends
        ResourceSnapshot
    implements
        Jsonable,
        Serializable {

    private UUID _indexPage;
    private UUID _defaultPage;


    /**
     * Constructor.
     *
     * @param parentId The folder's parent.
     * @param name     The folder's name.
     */
    public FolderDto(final UUID parentId, final ResourceName name) {
        setParent(parentId);
        setName(name);
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


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, getParent());
        json.set(JsonKeys.NAME, getName().toString());
    }
}
