/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.util.Date;


/**
 * A file from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class LegacyFile {
    private final String _title;
    private final String _description;
    private final Date _lastUpdate;

    /**
     * Constructor.
     *
     * @param title The file's title.
     * @param description The file's description.
     * @param lastUpdate The file's last update.
     */
    public LegacyFile(final String title,
                      final String description,
                      final Date lastUpdate) {
        _title = title;
        _description = description;
        _lastUpdate =
            (lastUpdate != null) ? new Date(lastUpdate.getTime()) : null;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    String getTitle() {
        return _title;
    }


    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    String getDescription() {
        return _description;
    }

    /**
     * Accessor.
     *
     * @return Returns the last update.
     */
    Date getLastUpdate() {
        return _lastUpdate;
    }
}
