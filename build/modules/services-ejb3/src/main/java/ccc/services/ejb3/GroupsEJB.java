/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.CccCheckedException;
import ccc.domain.Group;
import ccc.rest.Groups;
import ccc.rest.RestException;
import ccc.rest.dto.GroupDto;


/**
 * EJB implementation of the {@link Groups} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Groups.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Groups.class)
@RolesAllowed({})
public class GroupsEJB
    extends
        AbstractEJB
    implements
        Groups {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public GroupDto create(final GroupDto comment) {
            final Group g = new Group(comment.getName());
            g.setPermissions(comment.getPermissions());

            getGroups().create(g);

            return g.createDto();
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public GroupDto find(final UUID id) throws RestException {
        try {
            return getGroups().find(id).createDto();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public Collection<GroupDto> list(final String name) {
        final Collection<Group> groups = getGroups().list(name);
        return Group.map(groups);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({"ADMINISTRATOR"})
    public GroupDto update(final UUID id, final GroupDto group)
    throws RestException {
        try {
            final Group g = getGroups().find(id);
            g.setName(group.getName());
            g.setPermissions(group.getPermissions());

            return g.createDto();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
