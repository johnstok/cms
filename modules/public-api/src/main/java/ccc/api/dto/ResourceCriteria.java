/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


/**
 * Criteria class for resource search.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceCriteria implements Serializable {

    private UUID _parent = null;
    private String _tag = null;
    private Date _changedAfter = null;
    private Date _changedBefore = null;
    private String _mainmenu = null;

    /**
     * Constructor.
     *
     */
    public ResourceCriteria() {
        super();
    }

    /**
     * Accessor.
     *
     * @return Tha parent.
     */
    public UUID getParent() {
        return _parent;
    }

    /**
     * Accessor.
     *
     * @return The tag.
     */
    public String getTag() {
        return _tag;
    }

    /**
     * Accessor.
     *
     * @return Date changed before.
     */
    public Date getChangedBefore() {
        return _changedBefore;
    }

    /**
     * Mutator.
     *
     * @param date Date changed before.
     */
    public void setChangedBefore(final Date date) {
        _changedBefore = date;
    }
     /**
     * Accessor.
     *
     * @return Date changed after.
     */
    public Date getChangedAfter() {
        return _changedAfter;
    }
    /**
     * Mutator.
     *
     * @param date Date changed after.
     */
    public void setChangedAfter(final Date date) {
        _changedAfter = date;
    }
    /**
     * Mutator.
     *
     * @param tag The tag.
     */
    public void setTag(final String tag) {
        _tag = tag;
    }
    /**
     * Mutator.
     *
     * @param parent The resource's parent.
     */
    public void setParent(final UUID parent) {
        _parent = parent;
    }

    /**
     * Mutator.
     *
     * @param mainmenu Included in main menu.
     */
    public void setMainmenu(final String mainmenu) {
        _mainmenu = mainmenu;
    }

    /**
     * Accessor.
     *
     * @return  Included in main menu.
     */
    public String getMainmenu() {
        return _mainmenu;
    }

//    /**
//     * Mutator.
//     *
//     * @param metadataKey The metadata key to set.
//     */
//    public void setMetadataKey(final String metadataKey) {
//
//        _metadataKey = metadataKey;
//    }
//
//    /**
//     * Accessor.
//     *
//     * @return Returns the metadata key.
//     */
//    public String getMetadataKey() {
//
//        return _metadataKey;
//    }


}
