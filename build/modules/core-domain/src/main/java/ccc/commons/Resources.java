/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import static ccc.commons.Exceptions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import ccc.domain.CCCException;
import ccc.types.DBC;


/**
 * Helper methods for handling external resources.
 *
 * @author Civic Computing Ltd
 */
public final class Resources {

    private Resources() { /* NO-OP */ }

    /**
     * Read a URL resource into memory as a string.
     *
     * @param url The url to read from.
     * @param charset The character set to use when reading the url.
     * @return The resource as a String.
     */
    public static String readIntoString(final URL url,
                                        final Charset charset) {
        DBC.require().notNull(url);
        DBC.require().notNull(charset);

        InputStreamReader isr = null;
        try {
            final StringBuffer sb = new StringBuffer();
            final char[] buffer = new char[BUFFER_SIZE];
            isr = new InputStreamReader(url.openStream(), charset);
            int bytesRead = isr.read(buffer, 0, buffer.length);
            while (bytesRead>0) {
                sb.append(buffer, 0, bytesRead);
                bytesRead = isr.read(buffer, 0, buffer.length);
            }
            isr.close();
            return sb.toString();
        } catch (final IOException e) {
            throw new CCCException(e);
        } finally {
            if (null!=isr) {
                try {
                    isr.close();
                } catch (final IOException e) {
                    swallow(e);
                }
            }
        }
    }

    /**
     * Read a resource path into memory as properties.
     *
     * <p><b>"/"-separated names; no leading "/" (all names are absolute)</b>
     *
     * @param resourcePath The path to the resource.
     * @return The resource as a properties object.
     */
    public static Properties readIntoProps(final String resourcePath) {
        final Properties p = new Properties();
        final InputStream is =
            Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);
        if (null!=is) {
            try {
                p.load(is);
            } catch (final IOException e) {
                throw new CCCException(e);
            } finally {
                try {
                    is.close();
                } catch (final IOException e) {
                    swallow(e);
                }
            }
        }
        return p;
    }

    /**
     * Read a resource path into memory as a list of strings.
     * <p>One string per line in the resource.
     *
     * @param resourcePath The path to the resource.
     * @param charset The character set to use when reading the resource.
     * @return The resource as a list of strings.
     */
    public static List<String> readIntoList(final String resourcePath,
                                            final Charset charset) {
        final List<String> strings = new ArrayList<String>();
        final InputStream is =
            Resources.class.getResourceAsStream(resourcePath);
        if (null!=is) {
            try {
                BufferedReader r = null;
                try {
                    r = new BufferedReader(new InputStreamReader(is, charset));
                    for (String l = r.readLine(); null!=l; l=r.readLine()) {
                        strings.add(l);
                    }
                } catch (final IOException e) {
                    throw new CCCException(e);
                } finally {
                    try {
                        if (r != null) {
                            r.close();
                        }
                    } catch (final IOException e) {
                        swallow(e);
                    }
                }
            } finally {
                try {
                    is.close();
                } catch (final IOException e) {
                    swallow(e);
                }
            }
        }
        return strings;
    }

    private static final int BUFFER_SIZE = 1024;
}
