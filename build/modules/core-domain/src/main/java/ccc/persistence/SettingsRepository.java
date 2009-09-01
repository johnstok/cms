/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.persistence;

import ccc.domain.Setting;


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
     * Find a setting, given its name.
     *
     * @param settingName The setting's name.
     *
     * @return The corresponding setting.
     */
    public Setting find(final Setting.Name settingName) {
        return
            _repo.find(
                QueryNames.SETTINGS_WITH_NAME, Setting.class, settingName);
    }
}
