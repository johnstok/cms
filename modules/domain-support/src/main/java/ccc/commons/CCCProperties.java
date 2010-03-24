/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * Application properties for CCC.
 *
 * @author Civic Computing Ltd.
 */
public final class CCCProperties {
    private static final Logger LOG = Logger.getLogger(CCCProperties.class);
    private static final Properties PROPS = new Properties();

    private CCCProperties() { super(); }

    static {
            final InputStream in =
                CCCProperties.class.getResourceAsStream("/build.properties");

            if (null==in) {
                LOG.error("Missing build.properties file.");

            } else {
                try {
                    try {
                        PROPS.load(in);
                    } finally {
                        in.close();
                    }
                } catch (final IOException e) {
                    LOG.error("Error loading ccc properties.", e);
                }
            }

    }

    /**
     * Get the app's build number.
     *
     * @return The build number as a string.
     */
    public static String buildNumber() {
        return PROPS.getProperty("buildNumber", "-1");
    }

    /**
     * Get the app version.
     *
     * @return The version, as a string.
     */
    public static String version() {
        return PROPS.getProperty("ccc-version", "unknown");
    }

    /**
     * Get the build timestamp.
     *
     * @return The timestamp, as a string.
     */
    public static String timestamp() {
        return PROPS.getProperty("timestamp", "unknown");
    }
}
