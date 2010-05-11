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
package ccc.api.jaxrs;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.core.Group;
import ccc.api.core.Groups;
import ccc.api.core.PagedCollection;
import ccc.api.types.DBC;


/**
 * JAX-RS implementation of the {@link Groups} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class GroupsImpl
    extends
        JaxrsCollection
    implements
        Groups {

    private final Groups _groups;


    /**
     * Constructor.
     *
     * @param groups The groups implementation delegated to.
     */
    public GroupsImpl(final Groups groups) {
        _groups = DBC.require().notNull(groups);
    }


    /** {@inheritDoc} */
    @Override
    public Group create(final Group delta) {
        try {
            return _groups.create(delta);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Group retrieve(final UUID id) {
        try {
            return _groups.retrieve(id);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Group update(final UUID id, final Group group) {
        try {
            return _groups.update(id, group);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }

    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<Group> query(final String name,
        final int pageNo,
        final int pageSize) {
        try {
            return _groups.query(name, pageNo, pageSize);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }
}
