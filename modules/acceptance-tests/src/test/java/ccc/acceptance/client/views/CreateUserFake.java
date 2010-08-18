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
 * Revision      $Rev: 2979 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-07-12 10:12:56 +0100 (Mon, 12 Jul 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.acceptance.client.views;

import java.util.Set;
import java.util.UUID;

import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.CreateUser;

/**
 * Fake implementation of the {@link CreateUser} view.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserFake implements CreateUser {


    private final String _password2;
    private final String _password1;
    private final String _username;
    private final String _name;
    private final Set<UUID> _groups;
    private Editable _presenter;
    private boolean _showing;
    private final ValidationResult _validationResult = new ValidationResult();
    private final String _email;


    /**
     * Constructor.
     *
     * @param name The name
     * @param username The username
     * @param password1 The password
     * @param password2 The password confirm
     * @param groups The set of groups
     * @param email The email
     */
    public CreateUserFake(final String name,
                          final String username,
                          final String password1,
                          final String password2,
                          final Set<UUID> groups,
                          final String email) {
        _name = name;
        _username = username;
        _password1 = password1;
        _password2 = password2;
        _groups = groups;
        _email = email;
    }

    @Override
    public void alert(final String message) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getEmail() {
        return _email;
    }

    @Override
    public Set<UUID> getGroups() {
        return _groups;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public String getPassword1() {
        return _password1;
    }

    @Override
    public String getPassword2() {
        return _password2;
    }

    @Override
    public String getUsername() {
        return _username;
    }

    @Override
    public void hide() {
        _presenter = null;
        _showing   = false;
    }

    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        _showing   = true;
    }

    @Override
    public ValidationResult getValidationResult() {
        return _validationResult;
    }

}
