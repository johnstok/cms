/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.util.List;

import junit.framework.TestCase;
import ccc.commons.Testing;


/**
 * Tests for the {@link Resource} class.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceTest extends TestCase {

    private final Template _default = new Template();

    /**
     * Test.
     */
    public void testSetTagsParsesCsvCorrectly() {

        // ARRANGE
        final String tagString = "foo,bar,baz";
        final Page p = new Page("myPage");

        // ACT
        p.tags(tagString);

        // ASSERT
        final List<String> tags = p.tags();
        assertEquals(3, tags.size());
        assertEquals("foo", tags.get(0));
        assertEquals("bar", tags.get(1));
        assertEquals("baz", tags.get(2));
    }

    /**
     * Test.
     */
    public void testSetTagsTrimsWhitespace() {

        // ARRANGE
        final String tagString = "foo, bar ,baz";
        final Page p = new Page("myPage");

        // ACT
        p.tags(tagString);

        // ASSERT
        final List<String> tags = p.tags();
        assertEquals(3, tags.size());
        assertEquals("foo", tags.get(0));
        assertEquals("bar", tags.get(1));
        assertEquals("baz", tags.get(2));
    }

    /**
     * Test.
     */
    public void testSetTagsToZlsClearsTheList() {

        // ARRANGE
        final String tagString = "";
        final Page p = new Page("myPage");

        // ACT
        p.tags(tagString);

        // ASSERT
        final List<String> tags = p.tags();
        assertEquals(0, tags.size());
    }

    /**
     * Test.
     */
    public void testSetTagsIgnoresEmptyTags() {

        // ARRANGE
        final String tagString = "foo,, ,baz";
        final Page p = new Page("myPage");

        // ACT
        p.tags(tagString);

        // ASSERT
        final List<String> tags = p.tags();
        assertEquals(2, tags.size());
        assertEquals("foo", tags.get(0));
        assertEquals("baz", tags.get(1));
    }

    /**
     * Test.
     */
    public void testSetTagsToNullIsRejected() {

        // ACT
        try {
            final Page p = new Page("myPage");
            p.tags(null);
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testIsLockedReturnsFalseByDefault() {

        // ARRANGE
        final Resource r = new DummyResource(new ResourceName("foo"));

        // ACT
        final boolean isLocked = r.isLocked();

        // ASSERT
        assertFalse("Should be unlocked.", isLocked);
    }

    /**
     * Test.
     */
    public void testLockResource() {

        //ARRANGE
        final User u = new User("blat");
        final Resource r = new DummyResource(new ResourceName("foo"));

        // ACT
        r.lock(u);

        // ASSERT
        assertTrue("Should be locked.", r.isLocked());
    }

    /**
     * Test.
     */
    public void testLockResourceRejectsNull() {
        // ACT
        try {
            final Resource r = new DummyResource(new ResourceName("foo"));
            r.lock(null);
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testUnlockResource() {

        //ARRANGE
        final User u = new User("blat");
        final Resource r = new DummyResource(new ResourceName("foo"));
        r.lock(u);

        // ACT
        r.unlock();

        // ASSERT
        assertFalse("Should be unlocked.", r.isLocked());
    }

    /**
     * Test.
     */
    public void testQueryForLockedByUser() {

        //ARRANGE
        final User u = new User("blat");
        final Resource r = new DummyResource(new ResourceName("foo"));

        // ACT
        r.lock(u);

        // ASSERT
        assertEquals(u, r.lockedBy());
    }

    /**
     * Test.
     */
    public void testNameMutatorRejectsNull() {
        // ACT
        try {
            final Resource r =
                new Resource("foo"){
                    @Override public ResourceType type() { return null; }
                };
            r.name((ResourceName) null);
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testTitleslongerThan256AreRejectedByFullConstructor() {

        // ARRANGE
        final String tooLongTitle = Testing.dummyString('a', 257);

        // ACT
        try {
            new Resource(new ResourceName("x"), tooLongTitle) {
                @Override public ResourceType type() { return null; }
            };
            fail("Title should be rejected - too long.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string exceeds max length of 256.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testTitleslongerThan256AreRejected() {

        // ARRANGE
        final String tooLongTitle = Testing.dummyString('a', 257);

        // ACT
        try {
            new Resource(tooLongTitle){
                @Override public ResourceType type() { return null; }
            };
            fail("Title should be rejected - too long.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string exceeds max length of 256.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testTitleslongerThan256AreRejectedByMutator() {

        // ARRANGE
        final String tooLongTitle = Testing.dummyString('a', 257);

        // ACT
        try {
            final Resource r =
                new Resource("foo"){
                    @Override public ResourceType type() { return null; }
                };
            r.title(tooLongTitle);
            fail("Title should be rejected - too long.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string exceeds max length of 256.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testTitleOnlyConstructor() {
        // ACT
        final Resource r =
            new Resource("foo?"){
                @Override public ResourceType type() { return null; }
            };

        // ASSERT
        assertEquals(new ResourceName("foo_"), r.name());
        assertEquals("foo?", r.title());
    }

    /**
     * Test.
     */
    public void testTitleOnlyConstructorRejectsNull() {
        // ACT
        try {
            new Resource((String) null){
                @Override public ResourceType type() { return null; }
            };
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

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
    public void testComputeTemplateReturnsDefaultWhenNoTemplateIsFound() {

        // ARRANGE
        final Folder f1 = new Folder();
        final Folder f2 = new Folder();
        final Resource r = new Page();

        f2.add(f1);
        f1.add(r);

        // ACT
        final Template actual = r.computeTemplate(_default);

        // ASSERT
        assertSame(_default, actual);
        assertSame(_default, new Page().computeTemplate(_default));
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
        final Template actual = r.computeTemplate(_default);

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
        final Template actual = r.computeTemplate(_default);

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
