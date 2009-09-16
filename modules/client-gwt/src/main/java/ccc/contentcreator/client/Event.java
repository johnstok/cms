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
package ccc.contentcreator.client;


/**
 * Events supported by the event bus.
 *
 * @author Civic Computing Ltd.
 */
public interface Event {

    /**
     * Accessor.
     *
     * @return The type of the event.
     */
    Type getType();

    /**
     * Enumeration of event types.
     */
    public static enum Type {
        /** RESOURCE_UPDATED : Type. */
        RESOURCE_UPDATED,
        /** RESOURCE_CREATED : Type. */
        RESOURCE_CREATED;
    }
}
