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

import ccc.api.core.ResourceSummary;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.CreateAlias;


/**
 * Fake implementation of the {@link CreateAlias} view.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasFake
    implements
        CreateAlias {

    private String           _targetName;
    private Editable         _presenter;
    private boolean          _showing;
    private ValidationResult _validationResult = new ValidationResult();
    private ResourceSummary  _parent;
    private String           _aliasName;


    /**
     * Constructor.
     *
     * @param aliasName The name of the alias.
     * @param parent The parent for the alias.
     */
    public CreateAliasFake(final String aliasName,
                           final ResourceSummary parent) {
        _aliasName = aliasName;
        _parent    = parent;
    }


    /** {@inheritDoc} */
    @Override
    public void alert(final String message) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public String getAliasName() {
        return _aliasName;
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary getParent2() {
        return _parent;
    }


    /** {@inheritDoc} */
    @Override
    public String getTargetName() {
        return _targetName;
    }


    /** {@inheritDoc} */
    @Override
    public void setTargetName(final String targetName) {
        _targetName = targetName;
    }


    /** {@inheritDoc} */
    @Override
    public void hide() {
        _presenter = null;
        _showing   = false;
    }


    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        _showing   = true;
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        return _validationResult;
    }
}
