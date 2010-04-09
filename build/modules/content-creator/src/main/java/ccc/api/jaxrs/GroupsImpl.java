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

import java.util.Collection;
import java.util.UUID;

import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.Groups;
import ccc.api.dto.GroupDto;


/**
 * JAX-RS implementation of the {@link Groups} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/groups")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class GroupsImpl
    extends
        JaxrsCollection
    implements
        Groups {


    /** {@inheritDoc} */
    @Override
    public GroupDto create(final GroupDto delta) {
        try {
            return getGroups().create(delta);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public GroupDto find(final UUID id) {
        try {
            return getGroups().find(id);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<GroupDto> list(final String name) {
        try {
            return getGroups().list(name);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public GroupDto update(final UUID id, final GroupDto group) {
        try {
            return getGroups().update(id, group);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }
}
