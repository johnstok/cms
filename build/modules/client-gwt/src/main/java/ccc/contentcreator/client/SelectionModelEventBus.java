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

import ccc.contentcreator.controllers.ResourceCreatedEvent;
import ccc.contentcreator.controllers.ResourceUpdatedEvent;
import ccc.types.DBC;


/**
 * This is an interim implementation of the event bus that simply wraps a
 * single selection model.
 *
 * @author Civic Computing Ltd.
 */
public class SelectionModelEventBus
    implements
        EventBus {

    private final SingleSelectionModel _ssm;


    /**
     * Constructor.
     *
     * @param ssm The selection model to wrap.
     */
    public SelectionModelEventBus(final SingleSelectionModel ssm) {
        _ssm = ssm;
    }


    /** {@inheritDoc} */
    @Override
    public void put(final Event event) {
        DBC.require().notNull(event);

        switch (event.getType()) {
            case RESOURCE_UPDATED:
                final ResourceUpdatedEvent rue = (ResourceUpdatedEvent) event;
                _ssm.update(rue.getResource());
                break;

            case RESOURCE_CREATED:
                final ResourceCreatedEvent rce = (ResourceCreatedEvent) event;
                _ssm.create(rce.getResource(), rce.getParentFolder());
                break;

            default:
                throw new IllegalArgumentException(
                    "Unsupported event type: "+event);
        }
    }
}
