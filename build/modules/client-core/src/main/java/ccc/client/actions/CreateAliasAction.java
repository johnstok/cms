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
package ccc.client.actions;

import ccc.api.core.Alias;
import ccc.client.callbacks.AliasCreatedCallback;
import ccc.client.commands.CreateAliasCommand;
import ccc.client.core.Action;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;


/**
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateAliasAction
    implements
        Action {

    private final Alias _alias;


    /**
     * Constructor.
     *
     * @param alias The new alias to create.
     */
    public CreateAliasAction(final Alias alias) {
        _alias = alias;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        new CreateAliasCommand(_alias).invoke(
            InternalServices.api,
            new AliasCreatedCallback(I18n.uiConstants.createAlias()));
    }
}
