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
package ccc.rest.dto;
import static ccc.serialization.JsonKeys.*;

import java.io.Serializable;

import ccc.serialization.Json;
import ccc.serialization.Jsonable;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PagingDto implements Serializable, Jsonable {

    private int _offset = 0;
    private int _limit = 20;



    @SuppressWarnings("unused")
    private PagingDto() {
        super();
    }

    /**
     * Constructor.
     *
     * @param offset
     * @param limit
     */
    public PagingDto(final int offset, final int limit) {
        _offset = offset;
        _limit = limit;
    }

    /**
     * Constructor.
     *
     * @param json
     */
    public PagingDto(final Json json) {
        _offset = json.getInt(OFFSET).intValue();
        _limit = json.getInt(LIMIT).intValue();
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(OFFSET, Long.valueOf(_offset));
        json.set(LIMIT, Long.valueOf(_limit));
    }

    /**
     * Accessor.
     *
     * @return Returns the offset.
     */
    public final int getOffset() {

        return _offset;
    }


    /**
     * Mutator.
     *
     * @param offset The offset to set.
     */
    public final void setOffset(final int offset) {

        _offset = offset;
    }


    /**
     * Accessor.
     *
     * @return Returns the limit.
     */
    public final int getLimit() {

        return _limit;
    }


    /**
     * Mutator.
     *
     * @param limit The limit to set.
     */
    public final void setLimit(final int limit) {

        _limit = limit;
    }

}
