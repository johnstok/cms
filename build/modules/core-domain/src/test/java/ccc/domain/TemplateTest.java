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
package ccc.domain;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public final class TemplateTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructor(){

        // ARRANGE

        // ACT
        final Template t =
            new Template("foo!", "bar", "Hello world", "<fields/>");

        // ASSERT
        assertEquals(new ResourceName("foo_"), t.name());
        assertEquals("foo!", t.title());
        assertEquals("bar", t.description());
        assertEquals("Hello world", t.body());
        assertEquals("<fields/>", t.definition());
        assertEquals(ResourceType.TEMPLATE, t.type());
    }

    /**
     * Test.
     */
    public void testConstructorWithName(){

        // ARRANGE

        // ACT
        final Template t = new Template(new ResourceName("testName"),
            "foo!",
            "bar",
            "Hello world",
            "<fields/>");

        // ASSERT
        assertEquals(new ResourceName("testName"), t.name());
        assertEquals("foo!", t.title());
        assertEquals("bar", t.description());
        assertEquals("Hello world", t.body());
        assertEquals("<fields/>", t.definition());
        assertEquals(ResourceType.TEMPLATE, t.type());
    }

}
