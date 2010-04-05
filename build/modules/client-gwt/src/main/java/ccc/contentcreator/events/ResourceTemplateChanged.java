/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.contentcreator.events;

import java.util.UUID;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating a resource's template has changed.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTemplateChanged
    extends
        GwtEvent<ResourceTemplateChanged.ResTemChangedHandler> {

    private final UUID _resource;
    private final UUID _newTemplate;


    /**
     * Constructor.
     */
    public ResourceTemplateChanged(final UUID resource, final UUID template) {
        _newTemplate = template;
        _resource = resource;
    }


    /**
     * Accessor.
     *
     * @return Returns the resource.
     */
    public UUID getResource() { return _resource; }


    /**
     * Accessor.
     *
     * @return Returns the template.
     */
    public UUID getNewTemplate() { return _newTemplate; }


    /**
     * Accessor.
     *
     * @return Returns the type.
     */
    public static Type<ResTemChangedHandler> getType() { return TYPE; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final ResTemChangedHandler handler) {
        handler.onTemlateChanged(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<ResTemChangedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'resource template changed' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface ResTemChangedHandler extends EventHandler {


        /**
         * Handle a 'resource template changed' event.
         *
         * @param event The event to handle.
         */
        void onTemlateChanged(ResourceTemplateChanged event);
    }


    /** TYPE : Type. */
    public static final Type<ResTemChangedHandler> TYPE =
        new Type<ResTemChangedHandler>();
}
