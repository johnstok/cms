/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ResourceType;


/**
 * A search that can return other results.
 *
 * @author Civic Computing Ltd.
 */
public class Search
    extends
        Resource {

    /** Constructor: for persistence only. */
    protected Search() { super(); }


    /**
     * Constructor.
     *
     * @param title The title for this resource.
     */
    public Search(final String title) {
        super(title);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceType type() {
        return ResourceType.SEARCH;
    }


    /** {@inheritDoc} */
    @Override
    public Jsonable createSnapshot() {
        return new Jsonable(){
            /** {@inheritDoc} */
            @Override public void toJson(final Json json) {
                json.set(JsonKeys.TITLE, getTitle());
            }
        };
    }


    /** {@inheritDoc} */
    @Override
    public final ResourceSnapshot forCurrentRevision() {
        final ResourceSnapshot dto = new ResourceSnapshot();
        setDtoProps(dto);
        return dto;
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot forSpecificRevision(final int revNo) {
        final ResourceSnapshot dto = new ResourceSnapshot();
        setDtoProps(dto);
        return dto;
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot forWorkingCopy() {
        final ResourceSnapshot dto = new ResourceSnapshot();
        setDtoProps(dto);
        return dto;
    }

}
