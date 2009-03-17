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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void testResourceResourceAccessibilityRespectsParentalRoles() {

        // ARRANGE
        final Folder f = new Folder();
        f.roles(Arrays.asList("bar"));
        final Resource r = new Page();
        r.roles(Arrays.asList("foo"));
        f.add(r);

        final User tom = new User("paul");
        tom.addRole("foo");

        // ACT
        final boolean isAccessible = r.isAccessibleTo(tom);

        // ASSERT
        assertFalse(isAccessible);
    }

    /**
     * Test.
     */
    public void testResourceIsAccessibleToUser() {

        // ARRANGE
        final Resource r = new Page();
        r.roles(Arrays.asList("foo"));
        final User tom = new User("paul");
        tom.addRole("foo");

        // ACT
        final boolean isAccessible = r.isAccessibleTo(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     */
    public void testResourceIsNotAccessibleToUser() {

        // ARRANGE
        final Resource r = new Page();
        r.roles(Arrays.asList("foo"));

        // ACT
        final boolean isAccessible = r.isAccessibleTo(_jack);

        // ASSERT
        assertFalse(isAccessible);
    }

    /**
     * Test.
     */
    public void testComputeRoles() {

        // ARRANGE
        final Folder f = new Folder();
        f.roles(Arrays.asList("foo"));
        final Page r = new Page();
        r.roles(Arrays.asList("bar"));
        f.add(r);


        // ACT
        final Collection<String> roles = r.computeRoles();

        // ASSERT
        assertEquals(2, roles.size());
        assertTrue(roles.contains("foo"));
        assertTrue(roles.contains("bar"));
    }

    /**
     * Test.
     */
    public void testRolesProperty() {

        // ARRANGE
        final Resource r = new Page();

        // ACT
        r.roles(Arrays.asList("foo", "bar"));

        // ASSERT
        assertEquals(2, r.roles().size());
        assertTrue(r.roles().contains("foo"));
        assertTrue(r.roles().contains("bar"));
    }

    /**
     * Test.
     */
    public void testCreatedOnIsSetDuringConstruction() {

        // ARRANGE
        final Date before = new Date();

        // ACT
        final Resource r = new Page();
        final Date after = new Date();

        // ASSERT
        assertTrue(before.getTime()<=r.dateCreated().getTime());
        assertTrue(after.getTime()>=r.dateCreated().getTime());
    }

    /**
     * Test.
     */
    public void testAtConstructionDateChangedIsSetToDateCreated() {

        // ARRANGE

        // ACT
        final Resource r = new Page();

        // ASSERT
        assertEquals(r.dateCreated(), r.dateChanged());
    }

    /**
     * Test.
     * @throws InterruptedException If interrupted from sleep.
     */
    public void testDateChangedCanBeSet() throws InterruptedException {

        // ARRANGE
        final Resource r = new Page();
        Thread.sleep(100); // Wait

        // ACT
        r.dateChanged(new Date());

        // ASSERT
        assertTrue(r.dateChanged().after(r.dateCreated()));
    }

    /**
     * Test.
     */
    public void testMetadataCanBeCleared() {

        // ARRANGE
        final Resource r = new Page();
        r.addMetadatum("foo", "bar");

        // ACT
        r.clearMetadatum("foo");

        // ASSERT
        assertNull(r.getMetadatum("foo"));
    }

    /**
     * Test.
     */
    public void testClearingMetadataDoesNotAffectParents() {

        // ARRANGE
        final Folder f = new Folder();
        f.addMetadatum("foo", "bar");
        final Resource r = new Page();
        f.add(r);

        // ACT
        r.clearMetadatum("foo");

        // ASSERT
        assertEquals("bar", r.getMetadatum("foo"));
    }

    /**
     * Test.
     */
    public void testNullIsAnInvalidMetadatum() {

        // ARRANGE
        final Resource r = new Page();

        // ACT
        try {
            r.addMetadatum("foo", null);
            fail();

        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }

        // ASSERT

    }

    /**
     * Test.
     */
    public void testGettingMissingMetadataReturnsNull() {

        // ARRANGE

        // ACT
        final Resource r = new Page();

        // ASSERT
        assertNull(r.getMetadatum("foo"));
    }

    /**
     * Test.
     */
    public void testAddMetadata() {

        // ARRANGE
        final Resource r = new Page();

        // ACT
        r.addMetadatum("foo", "bar");

        // ASSERT
        assertEquals("bar", r.getMetadatum("foo"));
    }

    /**
     * Test.
     */
    public void testMetadataIsInheritedFromParents() {

        // ARRANGE
        final Folder f = new Folder();
        final Page p = new Page();
        f.add(p);

        // ACT
        f.addMetadatum("foo", "bar");

        // ASSERT
        assertEquals("bar", p.getMetadatum("foo"));
    }

    /**
     * Test.
     */
    public void testLocalMetadataIsChosenOverParentMetadata() {

        // ARRANGE
        final Folder f = new Folder();
        f.addMetadatum("foo", "bar");
        final Page p = new Page();
        f.add(p);

        // ACT
        p.addMetadatum("foo", "baz");

        // ASSERT
        assertEquals("baz", p.getMetadatum("foo"));
        assertEquals("bar", f.getMetadatum("foo"));
    }

    /**
     * Test.
     * @throws JSONException
     */
    public void testCreateSnapshot() throws JSONException {

        // ARRANGE
        final String expected =
            new JSONObject().put("title", "Foo")
                            .put("paragraphs", new JSONArray())
                            .toString();
        final Resource r = new Page("Foo");

        // ACT
        final Snapshot s = r.createSnapshot();

        // ASSERT
        assertEquals(expected, s.getDetail());
    }

    /**
     * Test.
     */
    public void testRootAccessorReturnParent() {

        // ARRANGE
        final Folder root = new Folder("root");
        final Resource child = new Page("child");
        root.add(child);

        // ACT
        final Resource actual = child.root();

        // ASSERT
        assertEquals(root, actual);
    }

    /**
     * Test.
     */
    public void testRootAccessorReturnsThisForNullParent() {

        // ARRANGE
        final Resource root = new Page("root");

        // ACT
        final Resource actual = root.root();

        // ASSERT
        assertEquals(root, actual);
    }

    /**
     * Test.
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        final Resource p = new Page("myPage");

        // ACT
        assertEquals(false, p.includeInMainMenu());
        p.includeInMainMenu(true);

        // ASSERT
        assertEquals(true, p.includeInMainMenu());

    }

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
