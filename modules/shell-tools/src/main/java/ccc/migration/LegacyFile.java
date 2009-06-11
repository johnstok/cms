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


/**
 * A file from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class LegacyFile {
    private final String _title;
    private final String _description;

    /**
     * Constructor.
     *
     * @param title The file's title.
     * @param description The file's description.
     */
    public LegacyFile(final String title, final String description) {
        _title = title;
        _description = description;
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
}
