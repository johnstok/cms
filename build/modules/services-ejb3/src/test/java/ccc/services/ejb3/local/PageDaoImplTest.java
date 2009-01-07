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
package ccc.services.ejb3.local;

import static org.easymock.EasyMock.*;

import java.util.Collections;

import javax.persistence.EntityManager;

import junit.framework.TestCase;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.PageDao;
import ccc.services.UserManager;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testUpdatePage() {

        // ARRANGE
        final Page page = new Page("test");
        page.lock(_user);
        page.addParagraph(Paragraph.fromText("abc", "def"));

        _al.recordUpdate(page);
        expect(_em.find(Page.class, page.id())).andReturn(page);
        expect(_um.loggedInUser()).andReturn(_user);
        replay(_em, _al, _um);


        // ACT
        _cm.update(
            page.id(),
            page.version(),
            "new title",
            Collections.singleton(Paragraph.fromText("foo", "bar")));


        // ASSERT
        verify(_em, _al, _um);
        assertEquals("new title", page.title());
        assertEquals(1, page.paragraphs().size());
        assertEquals("foo", page.paragraphs().iterator().next().name());
        assertEquals("bar", page.paragraph("foo").text());
    }

    /**
     * Test.
     */
    public void testCreatePage() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Page foo = new Page("foo");

        _al.recordCreate(isA(Page.class));
        expect(_em.find(Folder.class, contentRoot.id())).andReturn(contentRoot);
        _em.persist(foo);
        replay(_em, _al);


        // ACT
        _cm.create(contentRoot.id(), foo);


        // ASSERT
        verify(_em, _al);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertSame(foo, contentRoot.entries().get(0));
    }

    /**
     * Test.
     */
    public void testCreatePageFailsWhenFolderExists() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder fooFolder = new Folder("foo");
        contentRoot.add(fooFolder);
        final Page fooPage = new Page("foo");

        expect(_em.find(Folder.class, contentRoot.id())).andReturn(contentRoot);
        replay(_em, _al);


        // ACT
        try {
            _cm.create(contentRoot.id(), fooPage);
            fail("Creation of page with"
                    + "same name as existing folder should fail.");
        } catch (final CCCException e) {
            assertEquals(
                "Folder already contains a resource with name 'foo'.",
                e.getMessage());
        }


        // ASSERT
        verify(_em, _al);
        assertEquals(1, contentRoot.size());
        assertSame(fooFolder, contentRoot.entries().get(0));
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _al = createStrictMock(AuditLog.class);
        _um = createStrictMock(UserManager.class);
        _cm = new PageDaoImpl(_em, _al, _um);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _al = null;
        _um = null;
        _cm = null;
    }

    private EntityManager _em;
    private AuditLog _al;
    private UserManager _um;
    private PageDao _cm;

    private final User _user = new User("dummy");
}
