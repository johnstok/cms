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
 * Tests for the {@link ResourceEntity} class.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceTest extends TestCase {

    private final TemplateEntity _default = new TemplateEntity();
    private UserEntity _jill = new UserEntity(new Username("jill"), "password");
    private UserEntity _jack = new UserEntity(new Username("jack"), "password");


    /**
     * Test.
     */
    public void testResourceNotIndexableByDefault() {

        // ARRANGE
        final ResourceEntity r = new PageEntity();

        // ACT

        // ASSERT
        assertFalse(r.isIndexable());
    }


    /**
     * Test.
     */
    public void testResourceIndexingEnabledViaMetadata() {

        // ARRANGE
        final ResourceEntity r = new PageEntity();

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
        final FolderEntity f = new FolderEntity();
        f.addMetadatum("searchable", "true");
        final ResourceEntity r = new PageEntity();
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
        final UserEntity tom = new UserEntity(new Username("tom"), "password");
        tom.addGroup(BAZ);
        final UserEntity dick = new UserEntity(new Username("dick"), "password");
        dick.addGroup(FOO);
        final UserEntity harry = new UserEntity(new Username("harry"), "password");
        harry.addGroup(FOO);

        final FolderEntity f = new FolderEntity();
        f.addUserPermission(new AccessPermission(true, true, tom));
        f.addUserPermission(new AccessPermission(true, true, harry));

        final PageEntity p = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        f.addUserPermission(new AccessPermission(true, true, _jill));
        final PageEntity p = new PageEntity();
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
        final PageEntity p = new PageEntity();
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
        final PageEntity p = new PageEntity();
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
        final PageEntity p = new PageEntity();

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
        final PageEntity p = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        final PageEntity p = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        f.addGroupPermission(new AccessPermission(true, true, FOO));
        final PageEntity p = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        f.addGroupPermission(new AccessPermission(true, true, FOO));
        final PageEntity p = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        final PageEntity p = new PageEntity();
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
        final PageEntity p = new PageEntity();
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
        final PageEntity p = new PageEntity();

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
        final FolderEntity f = new FolderEntity();
        f.addGroupPermission(new AccessPermission(true, true, BAR));
        final ResourceEntity r = new PageEntity();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        f.add(r);

        final UserEntity tom = new UserEntity(new Username("paul"), "password");
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
        final ResourceEntity r = new PageEntity();
        r.clearGroupAcl();
        r.clearUserAcl();
        final UserEntity tom = new UserEntity(new Username("paul"), "password");

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
        final ResourceEntity r = new PageEntity();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        final UserEntity tom = new UserEntity(new Username("paul"), "password");
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
        final ResourceEntity r = new PageEntity();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        r.addGroupPermission(new AccessPermission(true, true, BAR));
        final UserEntity tom = new UserEntity(new Username("paul"), "password");
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
        final FolderEntity f = new FolderEntity();
        f.addGroupPermission(new AccessPermission(true, true, BAR));
        f.addGroupPermission(new AccessPermission(true, true, BAZ));
        final ResourceEntity r = new PageEntity();
        r.addGroupPermission(new AccessPermission(true, true, FOO));
        r.addGroupPermission(new AccessPermission(true, true, FOZ));
        f.add(r);

        final UserEntity tom = new UserEntity(new Username("paul"), "password");
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
        final ResourceEntity r = new PageEntity();
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
        final ResourceEntity r = new PageEntity();

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
        final ResourceEntity r = new PageEntity();
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
        final ResourceEntity r = new PageEntity();

        // ASSERT
        assertEquals(r.getDateCreated(), r.getDateChanged());
    }

    /**
     * Test.
     * @throws InterruptedException If interrupted from sleep.
     */
    public void testDateChangedCanBeSet() throws InterruptedException {

        // ARRANGE
        final ResourceEntity r = new PageEntity();
        Thread.sleep(WAIT_LENGTH); // Wait

        // ACT
        r.setDateChanged(new Date(), new UserEntity());

        // ASSERT
        assertTrue(r.getDateChanged().after(r.getDateCreated()));
    }

    /**
     * Test.
     */
    public void testMetadataCanBeCleared() {

        // ARRANGE
        final ResourceEntity r = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        f.addMetadatum("foo", "bar");
        final ResourceEntity r = new PageEntity();
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
        final ResourceEntity r = new PageEntity();

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
        final ResourceEntity r = new PageEntity();

        // ASSERT
        assertNull(r.getMetadatum("foo"));
    }

    /**
     * Test.
     */
    public void testAddMetadata() {

        // ARRANGE
        final ResourceEntity r = new PageEntity();

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
        final FolderEntity f = new FolderEntity();
        final PageEntity p = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        f.addMetadatum("foo", "bar");
        final PageEntity p = new PageEntity();
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
        final FolderEntity root = new FolderEntity("root");
        final ResourceEntity child = new PageEntity();
        root.add(child);

        // ACT
        final ResourceEntity actual = child.getRoot();

        // ASSERT
        assertEquals(root, actual);
    }

    /**
     * Test.
     */
    public void testRootAccessorReturnsThisForNullParent() {

        // ARRANGE
        final ResourceEntity root = new PageEntity();

        // ACT
        final ResourceEntity actual = root.getRoot();

        // ASSERT
        assertEquals(root, actual);
    }

    /**
     * Test.
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        final ResourceEntity p = new PageEntity();

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
        final ResourceEntity p = new PageEntity();
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
        final ResourceEntity p = new PageEntity();

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
        final ResourceEntity p = new PageEntity();
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
        final ResourceEntity p = new PageEntity();
        p.lock(_jill);

        // ACT
        p.confirmLock(_jill);
    }

    /**
     * Test.
     */
    public void testConfirmLockThrowsUnlockedException() {

        // ARRANGE
        final ResourceEntity p = new PageEntity();

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
        final ResourceEntity p = new PageEntity();
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
        final ResourceEntity p = new PageEntity();

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
        final ResourceEntity p = new PageEntity();

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
        final ResourceEntity p = new PageEntity();

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
        final ResourceEntity p = new PageEntity();

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
            final ResourceEntity p = new PageEntity();
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
        final ResourceEntity r = new PageEntity();

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
        final UserEntity u = new UserEntity(new Username("blat"), "password");
        final ResourceEntity r = new PageEntity();

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
            final ResourceEntity r = new PageEntity();
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
        final ResourceEntity r = new PageEntity();
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
        final UserEntity u = new UserEntity(new Username("blat"), "password");
        final ResourceEntity r = new PageEntity();

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
            final ResourceEntity r = new PageEntity();
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
            new FolderEntity(tooLongTitle);
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
            final ResourceEntity r = new PageEntity();
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
        final ResourceEntity r = new FolderEntity("foo?");

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
            new FolderEntity((String) null);
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
        final FolderEntity f = new FolderEntity("foo");
        final ResourceEntity p = new FolderEntity("bar");
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
        final FolderEntity f1 = new FolderEntity();
        final FolderEntity f2 = new FolderEntity();
        final ResourceEntity r = new PageEntity();

        f2.add(f1);
        f1.add(r);

        // ACT
        final TemplateEntity actual = r.computeTemplate(_default);

        // ASSERT
        assertSame(_default, actual);
        assertSame(_default, new PageEntity().computeTemplate(_default));
    }

    /**
     * Test.
     */
    public void testComputeTemplateLooksInCalleeFirst() {

        // ARRANGE
        final TemplateEntity t1 = new TemplateEntity();
        final TemplateEntity t2 = new TemplateEntity();
        final TemplateEntity t3 = new TemplateEntity();

        final FolderEntity f1 = new FolderEntity();
        f1.setTemplate(t1);
        final FolderEntity f2 = new FolderEntity();
        f2.setTemplate(t2);
        final ResourceEntity r = new PageEntity();
        r.setTemplate(t3);

        f2.add(f1);
        f1.add(r);

        // ACT
        final TemplateEntity actual = r.computeTemplate(_default);

        // ASSERT
        assertEquals(t3, actual);
    }

    /**
     * Test.
     */
    public void testComputeTemplateRecursesToParent() {

        // ARRANGE
        final TemplateEntity t = new TemplateEntity();
        final ResourceEntity r = new PageEntity();
        final FolderEntity f1 = new FolderEntity();
        final FolderEntity f2 = new FolderEntity();
        f2.add(f1);
        f1.add(r);
        f2.setTemplate(t);

        // ACT
        final TemplateEntity actual = r.computeTemplate(_default);

        // ASSERT
        assertEquals(t, actual);
    }

    /**
     * Test.
     */
    public void testParentCanBeChanged() { // FIXME: Test parent index field.

        // ARRANGE
        final ResourceEntity r = new PageEntity();
        final FolderEntity f1 = new FolderEntity();
        final FolderEntity f2 = new FolderEntity();
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
        final ResourceEntity r = new PageEntity();
        final FolderEntity f = new FolderEntity();
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
        final ResourceEntity r = new PageEntity();
        final FolderEntity expected = new FolderEntity();

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
        final ResourceEntity r = new PageEntity();

        // ACT
        final FolderEntity actual = r.getParent();

        // ASSERT
        assertNull("Should be null.", actual);
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsNullTitle() {

        // ACT
        try {
            new FolderEntity(null);
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
            new FolderEntity("");
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
        final UserEntity u = new UserEntity(new Username("user"), "password");
        final ResourceEntity p = new PageEntity();

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
            final ResourceEntity r = new PageEntity();
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
        final UserEntity u = new UserEntity(new Username("user"), "password");

        final FolderEntity f1 = new FolderEntity("parent1");
        f1.publish(u);

        final FolderEntity f2 = new FolderEntity("parent2");
        f2.publish(u);

        final FolderEntity f3 = new FolderEntity("parent3");
        f3.publish(u);

        final ResourceEntity p = new PageEntity();
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
        final UserEntity u = new UserEntity(new Username("user"), "password");

        final FolderEntity f1 = new FolderEntity("parent1");

        final FolderEntity f2 = new FolderEntity("parent2");
        f2.publish(u);

        final FolderEntity f3 = new FolderEntity("parent2");
        f3.publish(u);

        final ResourceEntity p = new PageEntity();
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

        final ResourceEntity r = new PageEntity();
        final ResourceEntity r2 = new PageEntity();
        r2.setCacheDuration(d2);

        final FolderEntity f1 = new FolderEntity();
        final FolderEntity f2 = new FolderEntity();
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
        final ResourceEntity resource = new PageEntity();
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
        final FolderEntity f = new FolderEntity();
        f.addMetadatum("foo", "1");
        f.addMetadatum("bar", "1");
        final PageEntity p = new PageEntity();
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
    private static final GroupEntity FOO = new GroupEntity("foo");
    private static final GroupEntity BAR = new GroupEntity("bar");
    private static final GroupEntity BAZ = new GroupEntity("baz");
    private static final GroupEntity FOZ = new GroupEntity("foz");
}
