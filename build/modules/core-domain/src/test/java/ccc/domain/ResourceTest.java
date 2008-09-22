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
 * Tests for the {@link Resource} class.
 *
 * TODO: Test titles > 256 are disallowed.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceTest extends TestCase {

    /**
     * Test.
     */
    public void testAbsolutePath() {

        // ARRANGE
        final Folder f = new Folder(new ResourceName("foo"));
        final Page p = new Page(new ResourceName("bar"));
        f.add(p);

        // ACT
        final ResourcePath actual = p.absolutePath();

        // ASSERT
        assertEquals(new ResourcePath("/foo/bar"), actual);
    }

    /**
     * Test.
     */
    public void testComputeTemplateReturnsNullWhenNoTemplateIsFound() {

        // ARRANGE
        final Folder f1 = new Folder();
        final Folder f2 = new Folder();
        final Resource r = new Page();

        f2.add(f1);
        f1.add(r);

        // ACT
        final Template actual = r.computeTemplate();

        // ASSERT
        assertNull("Should be null.", actual);
        assertNull("Should be null.", new Page().computeTemplate());
    }

    /**
     * Test.
     */
    public void testComputeTemplateLooksInCalleeFirst() {

        // ARRANGE
        final Template t1 = new Template();
        final Template t2 = new Template();
        final Template t3 = new Template();

        final Folder f1 = new Folder();
        f1.displayTemplateName(t1);
        final Folder f2 = new Folder();
        f2.displayTemplateName(t2);
        final Resource r = new Page();
        r.displayTemplateName(t3);

        f2.add(f1);
        f1.add(r);

        // ACT
        final Template actual = r.computeTemplate();

        // ASSERT
        assertEquals(t3, actual);
    }

    /**
     * Test.
     */
    public void testComputeTemplateRecursesToParent() {

        // ARRANGE
        final Template t = new Template();
        final Resource r = new Page();
        final Folder f1 = new Folder();
        final Folder f2 = new Folder();
        f2.add(f1);
        f1.add(r);
        f2.displayTemplateName(t);

        // ACT
        final Template actual = r.computeTemplate();

        // ASSERT
        assertEquals(t, actual);
    }

    /**
     * Test.
     */
    public void testParentCanBeChanged() {

        // ARRANGE
        final Resource r = new Page();
        final Folder f1 = new Folder();
        final Folder f2 = new Folder();
        r.parent(f1);

        // ACT
        r.parent(f2);

        // ASSERT
        assertEquals(f2, r.parent());
    }

    /**
     * Test.
     */
    public void testParentCanBeCleared() {

        // ARRANGE
        final Resource r = new Page();
        final Folder f = new Folder();
        r.parent(f);

        // ACT
        r.parent(null);

        // ASSERT
        assertNull("Should be null.", r.parent());
    }


    /**
     * Test.
     */
    public void testParentMutator() {

        // ARRANGE
        final Resource r = new Page();
        final Folder expected = new Folder();

        // ACT
        r.parent(expected);

        // ASSERT
        assertEquals(expected, r.parent());
    }

    /**
     * Test.
     */
    public void testParentAccessor() {

        // ARRANGE
        final Resource r = new Page();

        // ACT
        final Folder actual = r.parent();

        // ASSERT
        assertNull("Should be null.", actual);
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsNullUrl() {

        // ACT
        try {
            new DummyResource((ResourceName) null);
            fail("Resources should reject NULL for the url parameter.");

         // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsEmptyStringForName() {

        // ACT
        try {
            new DummyResource(new ResourceName("foo"), "");
            fail("Resources should reject the ZLS for the url parameter.");

         // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.", e.getMessage());
        }
    }


    /**
     * Dummy resource for testing only.
     *
     * @author Civic Computing Ltd
     */
    private static final class DummyResource extends Resource {


        /**
         * Constructor.
         *
         * @param object
         */
        public DummyResource(final ResourceName url) {
            super(url);
        }

        /**
         * Constructor.
         *
         * @param string
         */
        public DummyResource(final ResourceName url, final String name) {
            super(url, name);
        }

        @Override
        public ResourceType type() { return ResourceType.FOLDER; }
    }

}
