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
package ccc.commands.anonymous;

import ccc.persistence.DataRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.SettingsRepository;
import ccc.search.SearchHelper;


/**
 * Command: index all resources.
 * N.B. This can time out with a large number of resources / slow server.
 *
 * @author Civic Computing Ltd.
 */
class IndexAllCommand
    implements
        AnonymousCommand {

    private final ResourceRepository _resRepo;
    private final DataRepository     _dataRepo;
    private final SettingsRepository _settingsRepo;


    /**
     * Constructor.
     *
     * @param resRepo
     * @param dataRepo
     * @param settingsRepo
     */
    public IndexAllCommand(final ResourceRepository resRepo,
                           final DataRepository dataRepo,
                           final SettingsRepository settingsRepo) {
        _resRepo = resRepo;
        _dataRepo = dataRepo;
        _settingsRepo = settingsRepo;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        new SearchHelper(_resRepo, _dataRepo, _settingsRepo).index();
    }

}
