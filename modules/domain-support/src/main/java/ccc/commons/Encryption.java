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
package ccc.commons;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Encryption support methods.
 *
 * @author Civic Computing Ltd.
 */
public final class Encryption {
    private static final Logger LOG = Logger.getLogger(Encryption.class);

    public static final int SHA_HASH_LENGTH = 32;
    public static final int HASH_REPETITIONS = 1000;


    /**
     * Constructor.
     */
    private Encryption() { super(); }


    /**
     * Hash a string.
     *
     * @param string The string to hash.
     * @param salt   The salt to use for hashing.
     *
     * @return The hashed string as a byte array.
     */
    public static byte[] hash(final String string,
                              final String salt) {

        try {
            // Prepare
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final Charset utf8 = Charset.forName("UTF-8");
            final byte[] saltBytes = salt.getBytes(utf8);
            final byte[] stringBytes = string.getBytes(utf8);

            // Compute initial hash
            digest.reset();
            digest.update(saltBytes);
            digest.update(stringBytes);
            byte[] hash = digest.digest();

            // Perform 1000 repetitions
            for (int i = 0; i < HASH_REPETITIONS; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Calculated hash: "+bytesToList(hash));
            }

            return hash;

        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to compute password digest.", e);
        }
    }

    private static List<Integer> bytesToList(final byte[] bytes) {
        final List<Integer> integers = new ArrayList<Integer>();
        for (final byte b : bytes) {
            integers.add(Integer.valueOf(b));
        }
        return integers;
    }
}
