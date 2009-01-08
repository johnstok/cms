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
    private User _jill = new User("jill");
    private User _jack = new User("jack");

    /**
     * Test.
     */
    public void testLockFailsWhenAlreadyLocked() {

        // ARRANGE
        final Page p = new Page("myPage");
        p.lock(_jack);

        // ACT
        try {
            p.lock(_jack);
            fail();

        // ASSERT
        } catch (final CCCException e) {
            assertEquals("Resource is already locked.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testUnlockFailsWhenNotLocked() {

        // ARRANGE
        final Page p = new Page("myPage");

        // ACT
        try {
            p.unlock(_jack);
            fail();


        // ASSERT
        } catch (final UnlockedException e) {
            assertEquals(p, e.resource());
        }


    }

    /**
     * Test.
     */
    public void testUnlockFailsWhenUserCannotUnlock() {

        // ARRANGE
        final Page p = new Page("myPage");
        p.lock(_jack);

        // ACT
        try {
            p.unlock(_jill);
            fail();

        // ASSERT
        } catch (final CCCException e) {
            assertEquals(
                "User not allowed to unlock this resource.", e.getMessage());
        }


    }

    /**
     * Test.
     */
    public void testConfirmLockDoesNothingWithCorrectUser() {

        // ARRANGE
        final Page p = new Page("myPage");
        p.lock(_jill);

        // ACT
        p.confirmLock(_jill);
    }

    /**
     * Test.
     */
    public void testConfirmLockThrowsUnlockedException() {

        // ARRANGE
        final Page p = new Page("myPage");

        // ACT
        try {
            p.confirmLock(_jill);
            fail("Should throw UnlockedException.");

        // ASSERT
        } catch (final UnlockedException e) {
            assertEquals(p, e.resource());
        }
    }

    /**
     * Test.
     */
    public void testConfirmLockThrowsLockMismatchException() {

        // ARRANGE
        final Page p = new Page("myPage");
        p.lock(_jack);

        // ACT
        try {
            p.confirmLock(_jill);
            fail("Should throw LockMismatchException.");

        // ASSERT
        } catch (final LockMismatchException e) {
            assertEquals(p, e.resource());
        }
    }

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
        final Resource r = new DummyResource("foo");

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
        final Resource r = new DummyResource("foo");

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
            final Resource r = new DummyResource("foo");
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
        final Resource r = new DummyResource("foo");
        r.lock(_jack);

        // ACT
        r.unlock(_jack);

        // ASSERT
        assertFalse("Should be unlocked.", r.isLocked());
    }

    /**
     * Test.
     */
    public void testQueryForLockedByUser() {

        //ARRANGE
        final User u = new User("blat");
        final Resource r = new DummyResource("foo");

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
        final Folder f = new Folder("foo");
        final Page p = new Page("bar");
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
        f1.template(t1);
        final Folder f2 = new Folder();
        f2.template(t2);
        final Resource r = new Page();
        r.template(t3);

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
        f2.template(t);

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
    public void testResourceConstructorRejectsNullTitle() {

        // ACT
        try {
            new DummyResource(null);
            fail("Resources should reject NULL for the title parameter.");

         // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsEmptyStringForTitle() {

        // ACT
        try {
            new DummyResource("");
            fail("Resources should reject the ZLS for the title parameter.");

         // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testPublish() {

        //ARRANGE
        final User u = new User("user");
        final Page p = new Page("foo");

        // ACT
        p.publish(u);

        // ASSERT
        assertTrue("Should be locked.", p.isPublished());
    }

    /**
     * Test.
     */
    public void testPublishRejectsNullUser() {
        // ACT
        try {
            final Resource r = new DummyResource("foo");
            r.publish(null);
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testIsVisibleTrue() {

        //ARRANGE
        final User u = new User("user");

        final Folder f1 = new Folder("parent1");
        f1.publish(u);

        final Folder f2 = new Folder("parent2");
        f2.publish(u);

        final Folder f3 = new Folder("parent3");
        f3.publish(u);

        final Page p = new Page("foo");
        p.publish(u);

        f1.add(f2);
        f2.add(f3);
        f3.add(p);

        // ASSERT
        assertTrue("Should be visible.", p.isVisible());
    }

    /**
     * Test.
     */
    public void testIsVisibleFalse() {

        //ARRANGE
        final User u = new User("user");

        final Folder f1 = new Folder("parent1");

        final Folder f2 = new Folder("parent2");
        f2.publish(u);

        final Folder f3 = new Folder("parent2");
        f3.publish(u);

        final Page p = new Page("foo");
        p.publish(u);

        f1.add(f2);
        f2.add(f3);
        f2.add(p);

        // ASSERT
        assertFalse("Should not be visible.", p.isVisible());
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
         * @param string
         */
        public DummyResource(final String title) {
            super(title);
        }

        @Override
        public ResourceType type() { return ResourceType.FOLDER; }
    }

}
