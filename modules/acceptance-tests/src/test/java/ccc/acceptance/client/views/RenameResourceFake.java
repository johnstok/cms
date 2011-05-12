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

import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.RenameResource;


/**
 * Fake implementation of the {@link RenameResource} view.
 *
 * @author Civic Computing Ltd.
 */
public class RenameResourceFake implements RenameResource {

    private String _name;
    private boolean _showing;
    private Object _presenter;
    private final ValidationResult _validationResult = new ValidationResult();

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(final String name) {
        _name = name;
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
