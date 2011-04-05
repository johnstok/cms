/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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
package ccc.plugins.search.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import junit.framework.TestCase;
import ccc.api.types.DBC;
import ccc.commons.Testing;


/**
 * Tests for Lucene behaviour if a process crashes.
 *
 * @author Civic Computing Ltd.
 */
public class CrashTest
    extends
        TestCase {

    /**
     * Consumes text from an input stream, printing it to std out.
     *
     * @author Civic Computing Ltd.
     */
    public static final class TextConsumer
        implements
            Runnable {

        private final InputStream _is;
        private final Charset     _charset;

        /**
         * Constructor.
         *
         * @param is      The input stream to consume.
         * @param charset The charset of the input stream.
         */
        public TextConsumer(final InputStream is, final Charset charset) {
            _is      = DBC.require().notNull(is);
            _charset = DBC.require().notNull(charset);
        }

        @Override
        public void run() {
            try {
                final BufferedReader br =
                    new BufferedReader(
                        new InputStreamReader(_is, _charset));
                String s = br.readLine();
                while (null!=s) {
                    System.out.println(s);
                    s = br.readLine();
                }
            } catch (final IOException e) {
                throw new RuntimeException("Error consuming content.", e);
            }
        }
    }


    private static final int ONE_SECOND = 1000;

    /**
     * Test.
     */
    public void testCrashDuringWriteCleansUpLocks() {

        // ARRANGE
        forkAndKill();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/crash-test/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();

        // ASSERT

    }


    private void forkAndKill() {
        try {
            // Start a process working on a lucene index.
            final ProcessBuilder pb =
                new ProcessBuilder(
                    "java",
                    "-cp", getClasspath(),
                    getClass().getName());
            pb.redirectErrorStream(true);
            final Process p = pb.start();
            consumeStdOut(p.getInputStream());

            Testing.sleep(ONE_SECOND);

            // Kill the process before commit -> will leave an open lock.
            p.destroy();

        } catch (final IOException e) {
            throw new RuntimeException("Error forking process.", e);
        }
    }


    private void consumeStdOut(final InputStream is) {
        new Thread(new TextConsumer(is, Charset.defaultCharset())).start();
    }


    private String getClasspath() {
        return System.getProperty("java.class.path", "");
    }


    /**
     * Application entry point.
     *
     * @param args Arguments for the application.
     */
    public static void main(final String[] args) {
        System.out.println("Starting");
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/crash-test/lucene").getAbsolutePath());

        searchEngine.startUpdate();
        Testing.sleep(Integer.MAX_VALUE);
        System.out.println("Finishing");

        // Update isn't completed so that Lucene fails to release its lock.
    }
}
