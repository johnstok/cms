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
 * An event bus for asynchronous collaboration between UI components.
 *
 * @author Civic Computing Ltd.
 */
public interface EventBus {

    /**
     * Put a new event onto the bus.
     *
     * @param event The event to put.
     */
    void put(Event event);
}
