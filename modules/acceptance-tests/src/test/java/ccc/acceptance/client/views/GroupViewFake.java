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
package ccc.acceptance.client.views;

import java.util.Set;

import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.presenters.GroupPresenter.GroupView;


/**
 * Fake implementation of the {@link GroupView} view.
 *
 * @author Civic Computing Ltd.
 */
public class GroupViewFake implements GroupView {

    private Set<String> _permissions;
    private String _name;
    private Object _presenter;
    private boolean _showing;
    private final ValidationResult _validationResult = new ValidationResult();

    @Override
    public void alert(String message) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Set<String> getPermissions() {
        return _permissions;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }

    @Override
    public void setPermissions(Set<String> permissions) {
        _permissions = permissions;
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
