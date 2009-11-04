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

import java.util.Date;

import junit.framework.TestCase;
import ccc.rest.dto.TemplateDelta;
import ccc.types.MimeType;
import ccc.types.ResourceName;
import ccc.types.ResourceType;


/**
 * Tests for the {@link Template} class.
 *
 * @author Civic Computing Ltd
 */
public final class TemplateTest extends TestCase {

    /**
     * Test.
     */
    public void testSnapshot() {

        // ARRANGE
        final Template t = new Template(
            "foo!",
            "bar",
            "Hello world",
            "<fields/>",
            MimeType.HTML,
            _rm);

        // ACT
        final TemplateDelta o  = t.createSnapshot();

        // ASSERT
        assertEquals("<fields/>", o.getDefinition());
        assertEquals("Hello world", o.getBody());
        assertEquals(MimeType.HTML, o.getMimeType());
    }

    /**
     * Test.
     */
    public void testConstructor(){

        // ARRANGE

        // ACT
        final Template t = new Template(
            "foo!",
            "bar",
            "Hello world",
            "<fields/>",
            MimeType.HTML,
            _rm);

        // ASSERT
        assertEquals(new ResourceName("foo_"), t.name());
        assertEquals("foo!", t.getTitle());
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
            "<fields/>",
            MimeType.HTML,
            _rm);

        // ASSERT
        assertEquals(new ResourceName("testName"), t.name());
        assertEquals("foo!", t.getTitle());
        assertEquals("bar", t.description());
        assertEquals("Hello world", t.body());
        assertEquals("<fields/>", t.definition());
        assertEquals(ResourceType.TEMPLATE, t.type());
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
