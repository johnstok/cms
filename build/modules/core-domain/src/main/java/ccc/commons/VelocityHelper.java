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
package ccc.commons;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ccc.domain.ResourcePath;


/**
 * Collection of helper methods for velocity templates.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityHelper {

    /**
     * Returns instance of Random initialised with current time.
     *
     * @return Instance of Random.
     */
    public Random random() {
        return new Random(new Date().getTime());
    }

    /**
     * Create ResourcePath object from the path.
     *
     * @param path The path as a String.
     * @return ResourcePath
     */
    public ResourcePath path(final String path) {
        return new ResourcePath(path);
    }

    /**
     * Returns current year.
     *
     * @return Current year as a String.
     */
    public String currentYear() {
       final Calendar cal = Calendar.getInstance();
       return ""+cal.get(Calendar.YEAR);
    }
}
