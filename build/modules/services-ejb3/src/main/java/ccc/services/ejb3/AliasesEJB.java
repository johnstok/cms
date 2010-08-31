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

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.Alias;
import ccc.api.core.Aliases;
import ccc.commands.UpdateAliasCommand;
import ccc.domain.AliasEntity;


/**
 * EJB implementation of the {@link Aliases} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Aliases.NAME)
@TransactionAttribute(REQUIRED)
@Local(Aliases.class)
public class AliasesEJB
    extends
        AbstractEJB
    implements
        Aliases {


    /** {@inheritDoc} */
    @Override
    public Alias update(final UUID aliasId,
                       final Alias delta) {
        checkPermission(ALIAS_UPDATE);

        return
            execute(
                new UpdateAliasCommand(
                    getRepoFactory(),
                    delta.getTargetId(),
                    aliasId));
    }


    /** {@inheritDoc} */
    @Override
    public Alias create(final Alias alias) {
        checkPermission(ALIAS_CREATE);

        return execute(commands().createAliasCommand(alias));
    }


    /** {@inheritDoc} */
    @Override
    public String aliasTargetName(final UUID aliasId) {
        checkPermission(ALIAS_READ);

        final AliasEntity alias =
            getRepoFactory()
                .createResourceRepository()
                .find(AliasEntity.class, aliasId);
        if (alias != null) {
            return alias.target().getName().toString();
        }
        return null;
    }
}
