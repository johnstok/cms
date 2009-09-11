/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import ccc.serialization.Json;
import ccc.serialization.Jsonable;
import ccc.types.ResourceName;
import ccc.types.ResourceType;

/**
 * Dummy resource for testing only.
 *
 * @author Civic Computing Ltd
 */
final class DummyResource extends Resource {

    /**
     * Constructor.
     *
     * @param title The resource's title.
     */
    public DummyResource(final String title) {
        super(title);
    }

    /**
     * Constructor.
     *
     * @param name The resource's name.
     * @param title The resource's title.
     */
    DummyResource(final ResourceName name, final String title) {
        super(name, title);
    }

    /** {@inheritDoc} */
    @Override public ResourceType type() { return ResourceType.FOLDER; }

    /** {@inheritDoc} */
    @Override public Jsonable createSnapshot() { return this; }

    /** {@inheritDoc} */
    @Override public void toJson(final Json json) { /* NO OP */ }
}
