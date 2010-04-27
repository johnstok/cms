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

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import ccc.api.exceptions.InsufficientPrivilegesException;
import ccc.api.exceptions.LockMismatchException;
import ccc.api.exceptions.UnlockedException;
import ccc.api.types.CommandType;
import ccc.api.types.Duration;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.Username;
import ccc.commons.Testing;


/**
 * Tests for the {@link Resource} class.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceTest extends TestCase {

    private final Template _default = new Template();
    private User _jill = new User(new Username("jill"), "password");
    private User _jack = new User(new Username("jack"), "password");


    /**
     * Test.
     */
    public void testResourceNotIndexableByDefault() {

        // ARRANGE
        final Resource r = new Page();

        // ACT

        // ASSERT
        assertFalse(r.isIndexable());
    }


    /**
     * Test.
     */
    public void testResourceIndexingEnabledViaMetadata() {

        // ARRANGE
        final Resource r = new Page();

        // ACT
        r.addMetadatum("searchable", "true");

        // ASSERT
        assertTrue(r.isIndexable());
    }


    /**
     * Test.
     */
    public void testChildrenCanOverrideIndexing() {

        // ARRANGE
        final Folder f = new Folder();
        f.addMetadatum("searchable", "true");
        final Resource r = new Page();
        f.add(r);

        // ACT
        r.addMetadatum("searchable", "other");

        // ASSERT
        assertFalse(r.isIndexable());
    }


    /**
     * Test.
     */
    public void testAccessibilityCheckRespectsAclCombinations() {

        // ARRANGE
        final User tom = new User(new Username("tom"), "password");
        tom.addGroup(BAZ);
        final User dick = new User(new Username("dick"), "password");
        dick.addGroup(FOO);
        final User harry = new User(new Username("harry"), "password");
        harry.addGroup(FOO);

        final Folder f = new Folder();
        f.addUserPermission(new AccessPermission(true, true, tom));
        f.addUserPermission(new AccessPermission(true, true, harry));

        final Page p = new Page();
        p.addGroupPermission(new AccessPermission(true, true, FOO));
        p.addGroupPermission(new AccessPermission(true, true, BAR));
        f.add(p);

        // ACT

        // ASSERT
        assertTrue(p.isSecure());
        assertTrue(p.isReadableBy(harry));
        assertFalse(p.isReadableBy(tom)); // Doesn't have FOO or BAR.
        assertFalse(p.isReadableBy(dick)); // Isn't tom or harry.
    }


    /**
     * Test.
     *
     */
    public void testAccessibilityCheckRespectsAclUsersFromParent() {

        // ARRANGE
        final Folder f = new Folder();
        f.addUserPermission(new AccessPermission(true, true, _jill));
        final Page p = new Page();
        f.add(p);

        // ACT
        final boolean isAccessible = p.isReadableBy(_jill);
        final boolean isNotAccessible = p.isReadableBy(_jack);

        // ASSERT
        assertTrue(p.isSecure());
        assertTrue(isAccessible);
        assertFalse(isNotAccessible);
    }


    /**
     * Test.
     */
    public void testAccessibilityCheckAllowsAclUser() {

        // ARRANGE
        final Page p = new Page();
        p.addUserPermission(new AccessPermission(true, true, _jill));

        // ACT
        final boolean isAccessible = p.isReadableBy(_jill);
        final boolean isNotAccessible = p.isReadableBy(_jack);

        // ASSERT
        assertTrue(p.isSecure());
        assertTrue(isAccessible);
        assertFalse(isNotAccessible);
    }


    /**
     * Test.
     */
    public void testAccessibilityCheckAllowsAclUserWithGroupsInAcl() {

        // ARRANGE
        final Page p = new Page();
        p.addGroupPermission(new AccessPermission(true, true, FOO));
        p.addUserPermission(new AccessPermission(true, true, _jill));

        // ACT
        final boolean isAccessible = p.isReadableBy(_jill);
        final boolean isNotAccessible = p.isReadableBy(_jack);

        // ASSERT
        assertTrue(p.isSecure());
        assertTrue(isAccessible);
        assertFalse(isNotAccessible);
    }


    /**
     * Test.
     */
    public void testAccessibilityCheckHandlesNullUser() {

        // ARRANGE
        final Page p = new Page();

        // ACT
        final boolean isAccessible = p.isReadableBy(null);

        // ASSERT
        assertTrue(isAccessible);
    }


    /**
     * Test.
     */
    public void testSecureResourceInaccessibleToNullUser() {

        // ARRANGE
        final Page p = new Page();
        p.addGroupPermission(new AccessPermission(true, true, FOO));

        // ACT
        final boolean isAccessible = p.isReadableBy(null);

        // ASSERT
        assertFalse(isAccessible);
    }


    /**
     * Test.
     */
    public void testSecureResourcesWithInsecureParentsAreSecure() {

        // ARRANGE
        final Folder f = new Folder();
        final Page p = new Page();
        p.addGroupPermission(new AccessPermission(true, true, FOO));
        f.add(p);

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     */
    public void testSecureResourcesWithSecureParentsAreSecure() {

        // ARRANGE
        final Folder f = new Folder();
        f.addGroupPermission(new AccessPermission(true, true, FOO));
        final Page p = new Page();
        p.addGroupPermission(new AccessPermission(true, true, BAR));
        f.add(p);

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     */
    public void testInsecureResourcesWithSecureParentsAreSecure() {

        // ARRANGE
        final Folder f = new Folder();
        f.addGroupPermission(new AccessPermission(true, true, FOO));
        final Page p = new Page();
        f.add(p);

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     */
    public void testInsecureResourcesWithInsecureParentsAreInsecure() {

        // ARRANGE
        final Folder f = new Folder();
        final Page p = new Page();
        f.add(p);

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertFalse(secure);
    }


    /**
     * Test.
     */
    public void testResourcesWithGroupsAreSecure() {

        // ARRANGE
        final Page p = new Page();
        p.addGroupPermission(new AccessPermission(true, true, FOO));

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     */
    public void testResourcesWithoutAclArentSecure() {

        // ARRANGE
        final Page p = new Page();

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertFalse(secure);
    }


    /**
     * Test.
     */
    public void testResourceAccessibilityRespectsParentalAcl() {

        // ARRANGE
        final Folder f = new Folder();
        f.addGroupPermission(new AccessPermission(true, true, BAR));
        final Resource r = new Page();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        f.add(r);

        final User tom = new User(new Username("paul"), "password");
        tom.addGroup(FOO);

        // ACT
        final boolean isAccessible = r.isReadableBy(tom);

        // ASSERT
        assertFalse(isAccessible);
    }

    /**
     * Test.
     */
    public void testResourcesWithEmptyAclAreAccessible() {

        // ARRANGE
        final Resource r = new Page();
        r.clearGroupAcl();
        r.clearUserAcl();
        final User tom = new User(new Username("paul"), "password");

        // ACT
        final boolean isAccessible = r.isReadableBy(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     */
    public void testResourceIsAccessibleToUser() {

        // ARRANGE
        final Resource r = new Page();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        final User tom = new User(new Username("paul"), "password");
        tom.addGroup(FOO);

        // ACT
        final boolean isAccessible = r.isReadableBy(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     */
    public void testGroupsOnResourceUseOrLogic() {

        // ARRANGE
        final Resource r = new Page();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        r.addGroupPermission(new AccessPermission(true, true, BAR));
        final User tom = new User(new Username("paul"), "password");
        tom.addGroup(FOO);

        // ACT
        final boolean isAccessible = r.isReadableBy(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     */
    public void testGroupsOnParentUseAndLogic() {

        // ARRANGE
        final Folder f = new Folder();
        f.addGroupPermission(new AccessPermission(true, true, BAR));
        f.addGroupPermission(new AccessPermission(true, true, BAZ));
        final Resource r = new Page();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        r.addGroupPermission(new AccessPermission(true, true, FOZ));
        f.add(r);

        final User tom = new User(new Username("paul"), "password");
        tom.addGroup(FOO);
        tom.addGroup(BAR);

        // ACT
        final boolean isAccessible = r.isReadableBy(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     */
    public void testResourceIsNotAccessibleToUser() {

        // ARRANGE
        final Resource r = new Page();
        r.addGroupPermission(new AccessPermission(true, true, FOO));

        // ACT
        final boolean isAccessible = r.isReadableBy(_jack);

        // ASSERT
        assertFalse(isAccessible);
    }

    /**
     * Test.
     */
    public void testGroupsProperty() {

        // ARRANGE
        final AccessPermission foo = new AccessPermission(true, true, FOO);
        final AccessPermission bar = new AccessPermission(true, true, BAR);
        final Resource r = new Page();

        // ACT
        r.addGroupPermission(foo);
        r.addGroupPermission(bar);

        // ASSERT
        assertEquals(2, r.getGroupAcl().size());
        assertTrue(r.getGroupAcl().contains(foo.createEntry()));
        assertTrue(r.getGroupAcl().contains(bar.createEntry()));
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
        assertTrue(before.getTime()<=r.getDateCreated().getTime());
        assertTrue(after.getTime()>=r.getDateCreated().getTime());
    }

    /**
     * Test.
     */
    public void testAtConstructionDateChangedIsSetToDateCreated() {

        // ARRANGE

        // ACT
        final Resource r = new Page();

        // ASSERT
        assertEquals(r.getDateCreated(), r.getDateChanged());
    }

    /**
     * Test.
     * @throws InterruptedException If interrupted from sleep.
     */
    public void testDateChangedCanBeSet() throws InterruptedException {

        // ARRANGE
        final Resource r = new Page();
        Thread.sleep(WAIT_LENGTH); // Wait

        // ACT
        r.setDateChanged(new Date(), new User());

        // ASSERT
        assertTrue(r.getDateChanged().after(r.getDateCreated()));
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
     */
    public void testRootAccessorReturnParent() {

        // ARRANGE
        final Folder root = new Folder("root");
        final Resource child = new Page();
        root.add(child);

        // ACT
        final Resource actual = child.getRoot();

        // ASSERT
        assertEquals(root, actual);
    }

    /**
     * Test.
     */
    public void testRootAccessorReturnsThisForNullParent() {

        // ARRANGE
        final Resource root = new Page();

        // ACT
        final Resource actual = root.getRoot();

        // ASSERT
        assertEquals(root, actual);
    }

    /**
     * Test.
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        final Resource p = new Page();

        // ACT
        assertEquals(false, p.isIncludedInMainMenu());
        p.setIncludedInMainMenu(true);

        // ASSERT
        assertEquals(true, p.isIncludedInMainMenu());

    }

    /**
     * Test.
     */
    public void testLockFailsWhenAlreadyLocked() {

        // ARRANGE
        final Resource p = new Page();
        p.lock(_jack);

        // ACT
        try {
            p.lock(_jack);
            fail();

        // ASSERT
        } catch (final LockMismatchException e) {
            assertEquals(p.getId(), e.getResource());
        }
    }

    /**
     * Test.
     *  the operation.
     */
    public void testUnlockFailsWhenNotLocked() {

        // ARRANGE
        final Resource p = new Page();

        // ACT
        try {
            p.unlock(_jack);
            fail();


        // ASSERT
        } catch (final UnlockedException e) {
            assertEquals(p.getId(), e.getResource());
        }


    }

    /**
     * Test.
     */
    public void testUnlockFailsWhenUserCannotUnlock() {

        // ARRANGE
        final Resource p = new Page();
        p.lock(_jack);

        // ACT
        try {
            p.unlock(_jill);
            fail();

        // ASSERT
        } catch (final InsufficientPrivilegesException e) {
            assertEquals(CommandType.RESOURCE_UNLOCK, e.getAction());
            assertEquals(_jill.getId(), e.getUser());
        }


    }

    /**
     * Test.
     */
    public void testConfirmLockDoesNothingWithCorrectUser() {

        // ARRANGE
        final Resource p = new Page();
        p.lock(_jill);

        // ACT
        p.confirmLock(_jill);
    }

    /**
     * Test.
     */
    public void testConfirmLockThrowsUnlockedException() {

        // ARRANGE
        final Resource p = new Page();

        // ACT
        try {
            p.confirmLock(_jill);
            fail("Should throw UnlockedException.");

        // ASSERT
        } catch (final UnlockedException e) {
            assertEquals(p.getId(), e.getResource());
        }
    }

    /**
     * Test.
     */
    public void testConfirmLockThrowsLockMismatchException() {

        // ARRANGE
        final Resource p = new Page();
        p.lock(_jack);

        // ACT
        try {
            p.confirmLock(_jill);
            fail("Should throw LockMismatchException.");

        // ASSERT
        } catch (final LockMismatchException e) {
            assertEquals(p.getId(), e.getResource());
        }
    }

    /**
     * Test.
     */
    public void testSetTags() {

        // ARRANGE
        final Set<String> in = new HashSet<String>() {{
            add("foo");
            add("bar");
            add("baz");
        }};
        final Resource p = new Page();

        // ACT
        p.setTags(in);

        // ASSERT
        final Set<String> tags = p.getTags();
        assertEquals(3, tags.size());
        assertTrue(tags.contains("foo"));
        assertTrue(tags.contains("bar"));
        assertTrue(tags.contains("baz"));
    }

    /**
     * Test.
     */
    public void testSetTagsTrimsWhitespace() {

        // ARRANGE
        final Set<String> in = new HashSet<String>() {{
            add("foo");
            add(" bar ");
            add("baz");
        }};
        final Resource p = new Page();

        // ACT
        p.setTags(in);

        // ASSERT
        final Set<String> tags = p.getTags();
        assertEquals(3, tags.size());
        assertTrue(tags.contains("foo"));
        assertTrue(tags.contains("bar"));
        assertTrue(tags.contains("baz"));
    }

    /**
     * Test.
     */
    public void testSetTagsEmptySetClearsTheList() {

        // ARRANGE
        final Resource p = new Page();

        // ACT
        p.setTags(new HashSet<String>());

        // ASSERT
        final Set<String> tags = p.getTags();
        assertEquals(0, tags.size());
    }

    /**
     * Test.
     */
    public void testSetTagsIgnoresEmptyTags() {

        // ARRANGE
        final Set<String> in = new HashSet<String>() {{
            add("foo");
            add("");
            add(" ");
            add("baz");
        }};
        final Resource p = new Page();

        // ACT
        p.setTags(in);

        // ASSERT
        final Set<String> tags = p.getTags();
        assertEquals(2, tags.size());
        assertTrue(tags.contains("foo"));
        assertTrue(tags.contains("baz"));
    }

    /**
     * Test.
     */
    public void testSetTagsToNullIsRejected() {

        // ACT
        try {
            final Resource p = new Page();
            p.setTags(null);
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
        final Resource r = new Page();

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
        final User u = new User(new Username("blat"), "password");
        final Resource r = new Page();

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
            final Resource r = new Page();
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
        final Resource r = new Page();
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
        final User u = new User(new Username("blat"), "password");
        final Resource r = new Page();

        // ACT
        r.lock(u);

        // ASSERT
        assertEquals(u, r.getLockedBy());
    }

    /**
     * Test.
     */
    public void testNameMutatorRejectsNull() {
        // ACT
        try {
            final Resource r = new Page();
            r.setName((ResourceName) null);
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
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
            new Folder(tooLongTitle);
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
            final Resource r = new Page();
            r.setTitle(tooLongTitle);
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
        final Resource r = new Folder("foo?");

        // ASSERT
        assertEquals(new ResourceName("foo_"), r.getName());
        assertEquals("foo?", r.getTitle());
    }

    /**
     * Test.
     */
    public void testTitleOnlyConstructorRejectsNull() {

        // ACT
        try {
            new Folder((String) null);
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
        final Resource p = new Folder("bar");
        f.add(p);

        // ACT
        final ResourcePath actual = p.getAbsolutePath();

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
        f1.setTemplate(t1);
        final Folder f2 = new Folder();
        f2.setTemplate(t2);
        final Resource r = new Page();
        r.setTemplate(t3);

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
        f2.setTemplate(t);

        // ACT
        final Template actual = r.computeTemplate(_default);

        // ASSERT
        assertEquals(t, actual);
    }

    /**
     * Test.
     */
    public void testParentCanBeChanged() { // FIXME: Test parent index field.

        // ARRANGE
        final Resource r = new Page();
        final Folder f1 = new Folder();
        final Folder f2 = new Folder();
        r.setParent(f1, Integer.valueOf(0));

        // ACT
        r.setParent(f2, Integer.valueOf(0));

        // ASSERT
        assertEquals(f2, r.getParent());
    }

    /**
     * Test.
     */
    public void testParentCanBeCleared() { // FIXME: Test parent index field.

        // ARRANGE
        final Resource r = new Page();
        final Folder f = new Folder();
        r.setParent(f, Integer.valueOf(0));

        // ACT
        r.setParent(null, null);

        // ASSERT
        assertNull("Should be null.", r.getParent());
    }


    /**
     * Test.
     */
    public void testParentMutator() { // FIXME: Test parent index field.

        // ARRANGE
        final Resource r = new Page();
        final Folder expected = new Folder();

        // ACT
        r.setParent(expected, Integer.valueOf(0));

        // ASSERT
        assertEquals(expected, r.getParent());
    }

    /**
     * Test.
     */
    public void testParentAccessor() {

        // ARRANGE
        final Resource r = new Page();

        // ACT
        final Folder actual = r.getParent();

        // ASSERT
        assertNull("Should be null.", actual);
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsNullTitle() {

        // ACT
        try {
            new Folder(null);
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
            new Folder("");
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
        final User u = new User(new Username("user"), "password");
        final Resource p = new Page();

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
            final Resource r = new Page();
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
        final User u = new User(new Username("user"), "password");

        final Folder f1 = new Folder("parent1");
        f1.publish(u);

        final Folder f2 = new Folder("parent2");
        f2.publish(u);

        final Folder f3 = new Folder("parent3");
        f3.publish(u);

        final Resource p = new Page();
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
        final User u = new User(new Username("user"), "password");

        final Folder f1 = new Folder("parent1");

        final Folder f2 = new Folder("parent2");
        f2.publish(u);

        final Folder f3 = new Folder("parent2");
        f3.publish(u);

        final Resource p = new Page();
        p.publish(u);

        f1.add(f2);
        f2.add(f3);
        f2.add(p);

        // ASSERT
        assertFalse("Should not be visible.", p.isVisible());
    }

    /**
     * Test.
     */
    public void testComputeCache() {

        // ARRANGE
        final Duration d = new Duration(650);
        final Duration d2 = new Duration(352);

        final Resource r = new Page();
        final Resource r2 = new Page();
        r2.setCacheDuration(d2);

        final Folder f1 = new Folder();
        final Folder f2 = new Folder();
        f2.add(f1);
        f1.add(r);
        f1.add(r2);

        f2.setCacheDuration(d);
        // ACT
        final Duration actual = r.computeCache();
        final Duration actual2 = r2.computeCache();

        // ASSERT
        assertEquals(d, actual);
        assertEquals(d2, actual2);
    }

    /**
     * Test.
     */
    public void testTitleContainsNoBadChars() {

        // ARRANGE
        final StringBuffer bad =
            new StringBuffer("before\u0096middle\u0092end\u0086");

        // ACT
        final Resource resource = new Page();
        resource.setTitle(bad.toString());

        // ASSERT
        assertFalse("Title must no contain bad characters",
            resource.getTitle().equals(bad.toString()));
        assertEquals("before–middle’end†", resource.getTitle());
    }


    /**
     * Test.
     */
    public void testComputeMetadata() {

        // ARRANGE
        final Folder f = new Folder();
        f.addMetadatum("foo", "1");
        f.addMetadatum("bar", "1");
        final Page p = new Page();
        f.addMetadatum("foo", "2");
        f.add(p);

        // ACT
        final Map<String, String> metadata = p.computeMetadata();

        // ASSERT
        assertEquals(2, metadata.size());
        assertTrue(metadata.get("foo").equals("2"));
        assertTrue(metadata.get("bar").equals("1"));
    }

    private static final int WAIT_LENGTH = 100;
    private static final Group FOO = new Group("foo");
    private static final Group BAR = new Group("bar");
    private static final Group BAZ = new Group("baz");
    private static final Group FOZ = new Group("foz");
}
