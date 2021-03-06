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

package ccc.client.presenters;

import java.util.Set;

import ccc.api.core.Group;
import ccc.client.core.AbstractPresenter;
import ccc.client.core.Editable;
import ccc.client.core.Validatable;
import ccc.client.core.ValidationResult;
import ccc.client.core.View;


/**
 * Abstract base class for group presenters.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GroupPresenter
    extends
        AbstractPresenter<GroupPresenter.GroupView, Group>
    implements
        Editable {


    /**
     * Constructor.
     *
     * @param view The MVP view for this presenter.
     */
    public GroupPresenter(final GroupView view) {
        super(view, new Group());
    }


    /**
     * Constructor.
     *
     * @param view The MVP view for this presenter.
     * @param group The group to update.
     */
    public GroupPresenter(final GroupView view, final Group group) {
        super(view, group);
    }

    /**
     * Render this presenter.
     */
    protected final void render() {
        getView().show(this);
    }


    /**
     * Check that the data in the view is valid.
     *
     * @return True if the data is valid; false otherwise.
     */
    protected final boolean valid() {
        final ValidationResult vr = getView().getValidationResult();
        if (!vr.isValid()) {
            getView().alert(vr.getErrors().get(0));
        }
        return vr.isValid();
    }


    /** {@inheritDoc} */
    @Override
    public final void cancel() {
        dispose();
    }


    /**
     * Extract data from a DTO into the view.
     *
     * @param dto The DTO that will provide the view's data.
     */
    protected final void bind(final Group dto) {
        getView().setName(dto.getName());
        getView().setPermissions(dto.getPermissions());
    }


    /**
     * Extract data from the view into a DTO.
     *
     * @param dto The DTO that will receive the view's data.
     */
    protected final void unbind(final Group dto) {
        dto.setName(getView().getName());
        dto.setPermissions(getView().getPermissions());
    }


    /**
     * MVP view of a group.
     */
    public static interface GroupView
        extends
            View<Editable>,
            Validatable {

        /**
         * Display an alert with the specified message.
         *
         * @param message The message to display.
         */
        void alert(String message);

        /**
         * Accessor.
         *
         * @return The group's name.
         */
        String getName();

        /**
         * Mutator.
         *
         * @param name The group's name.
         */
        void setName(String name);

        /**
         * Accessor.
         *
         * @return The group's permissions.
         */
        Set<String> getPermissions();

        /**
         * Mutator.
         *
         * @param permissions The group's permissions.
         */
        void setPermissions(Set<String> permissions);
    }
}
