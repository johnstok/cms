/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import junit.framework.TestCase;


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
            "print('foo', 'bar');",
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
            "out.write('foo');",
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
                "throw new java.lang.RuntimeException('Error message');",
                new Context(),
                new PrintWriter(out));
            fail();

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals(
                "java.lang.RuntimeException: Error message (ccc#1)",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRunnerCanBlockStaticAccess() {

        // ARRANGE
        final ScriptRunner runner =
            new ScriptRunner(Collections.<String>emptyList());

        // ACT
        try {
            runner.eval(
                "java.lang.System.exit(1)",
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
        final ScriptRunner runner =
            new ScriptRunner(Collections.<String>emptyList());

        // ACT
        try {
            runner.eval(
                "o.getClass().forName('ccc.commons.MapRegistry').newInstance()",
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
        final ScriptRunner runner =
            new ScriptRunner(Collections.<String>emptyList());

        // ACT
        try {
            runner.eval(
                "print(new Packages.ccc.commons.MapRegistry());",
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
        final ScriptRunner runner =
            new ScriptRunner(
                Collections.singletonList("ccc.commons.MapRegistry"));
        final StringWriter out = new StringWriter();

        // ACT
        runner.eval(
            "print(new Packages.ccc.commons.MapRegistry());",
            new Context(),
            out);

        // ASSERT
        assertTrue(out.toString().startsWith("ccc.commons.MapRegistry@"));
    }
}
