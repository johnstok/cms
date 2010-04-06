/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.persistence;

import javax.persistence.EntityManager;

import ccc.domain.Setting;
import ccc.rest.exceptions.EntityNotFoundException;


/**
 * A repository for settings objects.
 *
 * @author Civic Computing Ltd.
 */
public class SettingsRepository {

    private final Repository _repo;

    /**
     * Constructor.
     *
     * @param repo The underlying repository implementation.
     */
    public SettingsRepository(final Repository repo) {
        _repo = repo;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public SettingsRepository(final EntityManager em) {
        this(new JpaRepository(em));
    }


    /**
     * Find a setting, given its name.
     *
     * @param settingName The setting's name.
     *
     * @throws EntityNotFoundException If no setting exists.
     *
     * @return The corresponding setting.
     */
    public Setting find(final Setting.Name settingName)
    throws EntityNotFoundException {
        return
            _repo.find(
                QueryNames.SETTINGS_WITH_NAME, Setting.class, settingName);
    }
}
