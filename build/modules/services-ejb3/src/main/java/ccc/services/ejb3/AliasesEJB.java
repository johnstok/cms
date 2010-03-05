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

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.UpdateAliasCommand;
import ccc.domain.Alias;
import ccc.domain.CccCheckedException;
import ccc.rest.Aliases;
import ccc.rest.RestException;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.ResourceSummary;


/**
 * EJB implementation of the {@link Aliases} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Aliases.NAME)
@TransactionAttribute(REQUIRED)
@Local(Aliases.class)
@RolesAllowed({})
public class AliasesEJB
    extends
        AbstractEJB
    implements
        Aliases {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ALIAS_UPDATE})
    public void updateAlias(final UUID aliasId,
                            final AliasDelta delta)
                                                 throws RestException {
        try {
            new UpdateAliasCommand(
                getResources(),
                getAuditLog(),
                delta.getTargetId(),
                aliasId)
            .execute(
                currentUser(),
                new Date());

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ALIAS_CREATE})
    public ResourceSummary createAlias(final AliasDto alias)
                                                 throws RestException {
        try {
            return commands()
                    .createAliasCommand(alias)
                    .execute(currentUser(), new Date())
                    .mapResource();

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ALIAS_READ})
    public String aliasTargetName(final UUID aliasId) throws RestException {
        try {
            final Alias alias = getResources().find(Alias.class, aliasId);
            if (alias != null) {
                return alias.target().getName().toString();
            }
            return null;

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
