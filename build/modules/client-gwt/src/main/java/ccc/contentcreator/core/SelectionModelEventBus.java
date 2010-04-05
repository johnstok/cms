/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.core;

import ccc.contentcreator.events.ResourceCreatedEvent;
import ccc.contentcreator.events.ResourceUpdatedEvent;
import ccc.types.DBC;


/**
 * This is an interim implementation of the event bus that simply wraps a
 * single selection model.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated
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
                _ssm.create(rce.getResource());
                break;

            default:
                throw new IllegalArgumentException(
                    "Unsupported event type: "+event);
        }
    }
}
