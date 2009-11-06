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
        try {
            final InputStream in =
                CCCProperties.class.getResourceAsStream("/build.properties");
            try {
                PROPS.load(in);
            } finally {
                in.close();
            }
        } catch (final IOException e) {
            LOG.error("Error loading ccc properties.", e);
        }
    }

    /**
     * Get the app's build number.
     *
     * @return The build number as a string.
     */
    public static String buildNumber() {
        return PROPS.getProperty("buildNumber");
    }

    /**
     * Get the app version.
     *
     * @return The version, as a string.
     */
    public static String version() {
        return PROPS.getProperty("ccc-version");
    }

    /**
     * Get the build timestamp.
     *
     * @return The timestamp, as a string.
     */
    public static String timestamp() {
        return PROPS.getProperty("timestamp");
    }
}
