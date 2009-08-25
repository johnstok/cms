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

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;



/**
 * A partial update, sets the working copy for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceRevisionPU implements Jsonable {

    private final long _revision;


    /**
     * Constructor.
     *
     * @param revision The revision used to create the working copy.
     */
    public ResourceRevisionPU(final long revision) {
        _revision = revision;
    }


    /**
     * Accessor.
     *
     * @return Returns the revision.
     */
    public final long getRevision() {
        return _revision;
    }


    /** {@inheritDoc} */
    @Override public void toJson(final Json json) {
        json.set(JsonKeys.REVISION, Long.valueOf(_revision));
    }
}
