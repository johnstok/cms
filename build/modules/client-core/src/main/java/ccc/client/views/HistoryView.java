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

package ccc.client.views;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.client.core.LegacyView;


/**
 * MVP view to display a list of revisions.
 *
 * @author Civic Computing Ltd.
 */
public interface HistoryView
    extends
        LegacyView {

    /**
     * Returns selected {@link LogEntrySummaryModelData}.
     *
     * @return The selected item.
     */
    Revision selectedItem();

    /**
     * Updates selection model for a working copy.
     *
     */
    @Deprecated // TODO Move to presenter.
    void workingCopyCreated();

    /**
     * Return boolean value of the selected resource's lock status.
     *
     * @return True is selection is locked.
     */
    @Deprecated // TODO Move to presenter.
    boolean hasLock();

    /**
     * Accessor.
     *
     * @return The id for the resource.
     */
    @Deprecated // TODO Move to presenter.
    ResourceSummary getResource();

}
