/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.CreateAliasCommand;
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
@TransactionAttribute(REQUIRES_NEW)
@Remote(Aliases.class)
@RolesAllowed({})
public class AliasesEJB
    extends
        AbstractEJB
    implements
        Aliases {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateAlias(final UUID aliasId,
                            final AliasDelta delta)
                                                 throws RestException {
        try {
            new UpdateAliasCommand(getResources(), getAuditLog()).execute(
                currentUser(),
                new Date(),
                delta.getTargetId(),
                aliasId);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createAlias(final AliasDto alias)
                                                 throws RestException {
        try {
            return mapResource(
                new CreateAliasCommand(getResources(), getAuditLog()).execute(
                    currentUser(),
                    new Date(),
                    alias.getParentId(),
                    alias.getTargetId(),
                    alias.getName()));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public String aliasTargetName(final UUID aliasId) throws RestException {
        try {
            final Alias alias = getResources().find(Alias.class, aliasId);
            if (alias != null) {
                return alias.target().name().toString();
            }
            return null;

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
