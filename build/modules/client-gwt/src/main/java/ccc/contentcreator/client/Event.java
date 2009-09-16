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

    Type getType();

    public static enum Type {
        RESOURCE_UPDATED,
        RESOURCE_CREATED;
    }
}
