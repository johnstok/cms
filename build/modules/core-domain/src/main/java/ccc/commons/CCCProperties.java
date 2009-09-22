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
import java.util.HashMap;
import java.util.Map;
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
                CCCProperties.class.getResourceAsStream("/ccc.properties");
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
     * Look up a ccc property.
     *
     * @param key The key for the property.
     * @return The value of the property, or NULL if it doesn't exist.
     */
    public static String get(final String key) {
        return PROPS.getProperty(key);
    }

    /**
     * Return a map containing all properties allowed to client.
     *
     * @return The map of properties.
     */
    public static Map<String, String> getAll() {
        final Map<String, String> map = new HashMap<String, String>();
        for (final String key : PROPS.stringPropertyNames()) {
            if (key.equals("ccc-version")
                || key.equals("application.base-href")
                || key.equals("buildNumber")
                || key.equals("application.name")) {
                map.put(key, PROPS.getProperty(key));
            }
        }
        return map;
    }
}
