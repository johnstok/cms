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

import ccc.api.core.ACL;
import ccc.api.core.ResourceSummary;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.UpdateResourceAcl;


/**
 * Fake implementation of the {@link UpdateResourceAcl} view.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceAclFake implements UpdateResourceAcl {


    private final ACL _acl;
    private ResourceSummary _rs;
    private Editable _presenter;

    /**
     * Constructor.
     *
     * @param acl ACL
     * @param rs ResourceSumamry
     */
    public UpdateResourceAclFake(final ACL acl, final ResourceSummary rs) {
        _acl = acl;
        _rs = rs;
    }

    @Override
    public ACL getAcl() {
        return _acl;
    }

    @Override
    public ResourceSummary getResourceSummary() {
        return _rs;
    }

    @Override
    public void setResourceSummary(final ResourceSummary model) {
        _rs = model;
    }

    @Override
    public void hide() {
        return;
    }

    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
    }

    @Override
    public ValidationResult getValidationResult() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

}
