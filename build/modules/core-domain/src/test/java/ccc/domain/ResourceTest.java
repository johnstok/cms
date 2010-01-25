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
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.types.CommandType;
import ccc.types.Duration;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;
import ccc.types.Username;


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
     *
     * @throws Exception If the test fails.
     */
    public void testAccessibilityCheckRespectsAclCombinations()
    throws Exception {

        // ARRANGE
        final User tom = new User(new Username("tom"), "password");
        tom.addRole(BAZ);
        final User dick = new User(new Username("dick"), "password");
        dick.addRole(FOO);
        final User harry = new User(new Username("harry"), "password");
        harry.addRole(FOO);

        final Folder f = new Folder();
        f.setUserAcl(Arrays.asList(new User[]{tom, harry}));
        final Page p = new Page();
        p.roles(Arrays.asList(new Group[]{FOO, BAR}));
        f.add(p);

        // ACT

        // ASSERT
        assertTrue(p.isSecure());
        assertTrue(p.isAccessibleTo(harry));
        assertFalse(p.isAccessibleTo(tom)); // Doesn't have FOO or BAR.
        assertFalse(p.isAccessibleTo(dick)); // Isn't tom or harry.
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testAccessibilityCheckRespectsAclUsersFromParent()
    throws Exception {

        // ARRANGE
        final Folder f = new Folder();
        f.setUserAcl(Collections.singleton(_jill));
        final Page p = new Page();
        f.add(p);

        // ACT
        final boolean isAccessible = p.isAccessibleTo(_jill);
        final boolean isNotAccessible = p.isAccessibleTo(_jack);

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
        p.setUserAcl(Collections.singleton(_jill));

        // ACT
        final boolean isAccessible = p.isAccessibleTo(_jill);
        final boolean isNotAccessible = p.isAccessibleTo(_jack);

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
        p.roles(Collections.singleton(FOO));
        p.setUserAcl(Collections.singleton(_jill));

        // ACT
        final boolean isAccessible = p.isAccessibleTo(_jill);
        final boolean isNotAccessible = p.isAccessibleTo(_jack);

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
        final boolean isAccessible = p.isAccessibleTo(null);

        // ASSERT
        assertTrue(isAccessible);
    }


    /**
     * Test.
     */
    public void testSecureResourceInaccessibleToNullUser() {

        // ARRANGE
        final Page p = new Page();
        p.roles(Collections.singleton(FOO));

        // ACT
        final boolean isAccessible = p.isAccessibleTo(null);

        // ASSERT
        assertFalse(isAccessible);
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testSecureResourcesWithInsecureParentsAreSecure()
    throws Exception {

        // ARRANGE
        final Folder f = new Folder();
        final Page p = new Page();
        p.roles(Arrays.asList(FOO));
        f.add(p);

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testSecureResourcesWithSecureParentsAreSecure()
    throws Exception {

        // ARRANGE
        final Folder f = new Folder();
        f.roles(Arrays.asList(FOO));
        final Page p = new Page();
        p.roles(Arrays.asList(BAR));
        f.add(p);

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testInsecureResourcesWithSecureParentsAreSecure()
    throws Exception {

        // ARRANGE
        final Folder f = new Folder();
        f.roles(Arrays.asList(FOO));
        final Page p = new Page();
        f.add(p);

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testInsecureResourcesWithInsecureParentsAreInsecure()
    throws Exception {

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
    public void testResourcesWithRolesAreSecure() {

        // ARRANGE
        final Page p = new Page();
        p.roles(Collections.singleton(FOO));

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertTrue(secure);
    }


    /**
     * Test.
     */
    public void testResourcesWithoutRolesArentSecure() {

        // ARRANGE
        final Page p = new Page();

        // ACT
        final boolean secure = p.isSecure();

        // ASSERT
        assertFalse(secure);
    }


    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testResourceAccessibilityRespectsParentalRoles()
    throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder();
        f.roles(Arrays.asList(BAR));
        final Resource r = new Page();
        r.roles(Arrays.asList(FOO));
        f.add(r);

        final User tom = new User(new Username("paul"), "password");
        tom.addRole(FOO);

        // ACT
        final boolean isAccessible = r.isAccessibleTo(tom);

        // ASSERT
        assertFalse(isAccessible);
    }

    /**
     * Test.
     */
    public void testResourcesWithNoRolesAreAccessible() {

        // ARRANGE
        final Resource r = new Page();
        r.roles(Arrays.asList(new Group[]{}));
        final User tom = new User(new Username("paul"), "password");

        // ACT
        final boolean isAccessible = r.isAccessibleTo(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     */
    public void testResourceIsAccessibleToUser() {

        // ARRANGE
        final Resource r = new Page();
        r.roles(Arrays.asList(FOO));
        final User tom = new User(new Username("paul"), "password");
        tom.addRole(FOO);

        // ACT
        final boolean isAccessible = r.isAccessibleTo(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     */
    public void testRolesOnResourceUseOrLogic() {

        // ARRANGE
        final Resource r = new Page();
        r.roles(Arrays.asList(FOO, BAR));
        final User tom = new User(new Username("paul"), "password");
        tom.addRole(FOO);

        // ACT
        final boolean isAccessible = r.isAccessibleTo(tom);

        // ASSERT
        assertTrue(isAccessible);
    }

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testRolesOnParentUseAndLogic() throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder();
        f.roles(Arrays.asList(BAR, BAZ));
        final Resource r = new Page();
        r.roles(Arrays.asList(FOO, FOZ));
        f.add(r);

        final User tom = new User(new Username("paul"), "password");
        tom.addRole(FOO);
        tom.addRole(BAR);

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
        r.roles(Arrays.asList(FOO));

        // ACT
        final boolean isAccessible = r.isAccessibleTo(_jack);

        // ASSERT
        assertFalse(isAccessible);
    }

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testComputeRoles() throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder();
        f.roles(Arrays.asList(FOO));
        final Page r = new Page();
        r.roles(Arrays.asList(BAR));
        f.add(r);


        // ACT
        final Collection<Group> roles = r.computeRoles();

        // ASSERT
        assertEquals(2, roles.size());
        assertTrue(roles.contains(FOO));
        assertTrue(roles.contains(BAR));
    }

    /**
     * Test.
     */
    public void testRolesProperty() {

        // ARRANGE
        final Resource r = new Page();

        // ACT
        r.roles(Arrays.asList(FOO, BAR));

        // ASSERT
        assertEquals(2, r.roles().size());
        assertTrue(r.roles().contains(FOO));
        assertTrue(r.roles().contains(BAR));
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
        Thread.sleep(WAIT_LENGTH); // Wait

        // ACT
        r.dateChanged(new Date(), new User());

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
     * @throws CccCheckedException If the test fails.
     */
    public void testClearingMetadataDoesNotAffectParents()
    throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testMetadataIsInheritedFromParents()
    throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testLocalMetadataIsChosenOverParentMetadata()
    throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testRootAccessorReturnParent()
    throws CccCheckedException {

        // ARRANGE
        final Folder root = new Folder("root");
        final Resource child = new Page();
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
        final Resource root = new Page();

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
        final Resource p = new Page();

        // ACT
        assertEquals(false, p.includeInMainMenu());
        p.includeInMainMenu(true);

        // ASSERT
        assertEquals(true, p.includeInMainMenu());

    }

    /**
     * Test.
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void testLockFailsWhenAlreadyLocked() throws LockMismatchException {

        // ARRANGE
        final Resource p = new Page();
        p.lock(_jack);

        // ACT
        try {
            p.lock(_jack);
            fail();

        // ASSERT
        } catch (final LockMismatchException e) {
            assertEquals(p, e.resource());
        }
    }

    /**
     * Test.
     * @throws InsufficientPrivilegesException If user is not allowed to perform
     *  the operation.
     */
    public void testUnlockFailsWhenNotLocked()
    throws InsufficientPrivilegesException {

        // ARRANGE
        final Resource p = new Page();

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
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is not locked.
     */
    public void testUnlockFailsWhenUserCannotUnlock()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        final Resource p = new Page();
        p.lock(_jack);

        // ACT
        try {
            p.unlock(_jill);
            fail();

        // ASSERT
        } catch (final InsufficientPrivilegesException e) {
            assertEquals(
                "User jill[] may not perform action: "
                + CommandType.RESOURCE_UNLOCK,
                e.getMessage());
        }


    }

    /**
     * Test.
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is not locked.
     */
    public void testConfirmLockDoesNothingWithCorrectUser()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        final Resource p = new Page();
        p.lock(_jill);

        // ACT
        p.confirmLock(_jill);
    }

    /**
     * Test.
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void testConfirmLockThrowsUnlockedException()
    throws LockMismatchException {

        // ARRANGE
        final Resource p = new Page();

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
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is not locked.
     */
    public void testConfirmLockThrowsLockMismatchException()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        final Resource p = new Page();
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
        final Resource p = new Page();

        // ACT
        p.tags(tagString);

        // ASSERT
        final Set<String> tags = p.tags();
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
        final String tagString = "foo, bar ,baz";
        final Resource p = new Page();

        // ACT
        p.tags(tagString);

        // ASSERT
        final Set<String> tags = p.tags();
        assertEquals(3, tags.size());
        assertTrue(tags.contains("foo"));
        assertTrue(tags.contains("bar"));
        assertTrue(tags.contains("baz"));
    }

    /**
     * Test.
     */
    public void testSetTagsToZlsClearsTheList() {

        // ARRANGE
        final String tagString = "";
        final Resource p = new Page();

        // ACT
        p.tags(tagString);

        // ASSERT
        final Set<String> tags = p.tags();
        assertEquals(0, tags.size());
    }

    /**
     * Test.
     */
    public void testSetTagsIgnoresEmptyTags() {

        // ARRANGE
        final String tagString = "foo,, ,baz";
        final Resource p = new Page();

        // ACT
        p.tags(tagString);

        // ASSERT
        final Set<String> tags = p.tags();
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
        final Resource r = new Page();

        // ACT
        final boolean isLocked = r.isLocked();

        // ASSERT
        assertFalse("Should be unlocked.", isLocked);
    }

    /**
     * Test.
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void testLockResource() throws LockMismatchException {

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
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void testLockResourceRejectsNull() throws LockMismatchException {
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
     * @throws LockMismatchException If the resource is locked by another user.
     * @throws UnlockedException If the resource is not locked.
     * @throws InsufficientPrivilegesException If user is not allowed to perform
     *  the operation.
     */
    public void testUnlockResource() throws LockMismatchException,
                                            InsufficientPrivilegesException,
                                            UnlockedException {

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
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void testQueryForLockedByUser() throws LockMismatchException {

        //ARRANGE
        final User u = new User(new Username("blat"), "password");
        final Resource r = new Page();

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
            final Resource r = new Page();
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
        final Resource r = new Folder("foo?");

        // ASSERT
        assertEquals(new ResourceName("foo_"), r.name());
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
     * @throws CccCheckedException If the test fails.
     */
    public void testAbsolutePath() throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder("foo");
        final Resource p = new Folder("bar");
        f.add(p);

        // ACT
        final ResourcePath actual = p.absolutePath();

        // ASSERT
        assertEquals(new ResourcePath("/foo/bar"), actual);
    }

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testComputeTemplateReturnsDefaultWhenNoTemplateIsFound()
    throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testComputeTemplateLooksInCalleeFirst()
    throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testComputeTemplateRecursesToParent()
    throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testIsVisibleTrue() throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testIsVisibleFalse() throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testComputeCache() throws CccCheckedException {

        // ARRANGE
        final Duration d = new Duration(650);
        final Duration d2 = new Duration(352);

        final Resource r = new Page();
        final Resource r2 = new Page();
        r2.cache(d2);

        final Folder f1 = new Folder();
        final Folder f2 = new Folder();
        f2.add(f1);
        f1.add(r);
        f1.add(r2);

        f2.cache(d);
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
        resource.title(bad.toString());

        // ASSERT
        assertFalse("Title must no contain bad characters",
            resource.getTitle().equals(bad.toString()));
        assertEquals("before–middle’end†", resource.getTitle());
    }

    private static final int WAIT_LENGTH = 100;
    private static final Group FOO = new Group("foo");
    private static final Group BAR = new Group("bar");
    private static final Group BAZ = new Group("baz");
    private static final Group FOZ = new Group("foz");
}
