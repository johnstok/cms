/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.plugins.scripting.rhino;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import junit.framework.TestCase;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.Script;


/**
 * Tests for the {@link ScriptRunner} class.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptRunnerTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRunnerProvidesPrintFunction() {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();
        final StringWriter out = new StringWriter();

        // ACT
        runner.eval(
            new Script("print('foo', 'bar');", "test"),
            new Context(),
            out);

        // ASSERT
        assertEquals("foobar", out.toString());
    }

    /**
     * Test.
     */
    public void testContextAddedToScriptEnvironment() {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();
        final StringWriter out = new StringWriter();

        // ACT
        runner.eval(
            new Script("out.write('foo');", "test"),
            new Context().add("out", out),
            new PrintWriter(out));

        // ASSERT
        assertEquals("foo", out.toString());
    }

    /**
     * Test.
     */
    public void testRunnerPropagatesExceptions() {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();
        final StringWriter out = new StringWriter();

        // ACT
        try {
            runner.eval(
                new Script(
                    "throw new java.lang.RuntimeException('Error message');",
                    "test"),
                new Context(),
                new PrintWriter(out));
            fail();

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals(
                "java.lang.RuntimeException: Error message (test#1)",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRunnerCanBlockStaticAccess() {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();
        runner.setWhitelist(Collections.<String>emptyList());

        // ACT
        try {
            runner.eval(
                new Script("java.lang.System.exit(1)", "test"),
                new Context(),
                new PrintWriter(System.out));

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals(
                "Access to class disallowed: java.lang.System",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRunnerCanBlockReflectionAccess() {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();
        runner.setWhitelist(Collections.<String>emptyList());

        // ACT
        try {
            runner.eval(
                new Script(
                    "o.getClass().forName('ccc.commons.MapRegistry')"
                        + ".newInstance()",
                    "test"),
                new Context().add("o", new Object()),
                new PrintWriter(System.out));

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals(
                "Access to class disallowed: java.lang.Class",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRunnerCanBlockStandardAccess() {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();
        runner.setWhitelist(Collections.<String>emptyList());

        // ACT
        try {
            runner.eval(
                new Script(
                    "print(new Packages.ccc.commons.MapRegistry());", "test"),
                new Context(),
                new PrintWriter(System.out));

            // ASSERT
        } catch (final RuntimeException e) {
            assertEquals(
                "Access to class disallowed: ccc.commons.MapRegistry",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRunnerAllowsWhitelistedAccess() {

        // ARRANGE
        final ScriptRunner runner = new ScriptRunner();
        runner.setWhitelist(
            Collections.singletonList("ccc.commons.MapRegistry"));
        final StringWriter out = new StringWriter();

        // ACT
        runner.eval(
            new Script(
                "print(new Packages.ccc.commons.MapRegistry());", "test"),
            new Context(),
            out);

        // ASSERT
        assertTrue(out.toString().startsWith("ccc.commons.MapRegistry@"));
    }
}
