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

package ccc.services.ejb3;

import static org.easymock.EasyMock.*;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.TestCase;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.domain.Template;
import ccc.services.AuditLog;
import ccc.services.ContentManager;
import ccc.services.QueryManager;


/**
 * Tests for the {@link ContentManagerEJB} class.
 *
 * @author Civic Computing Ltd
 */
public final class ContentManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public void testSetDefaultTemplate() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Template defaultTemplate =
            new Template("foo", "bar", "baz", "<fields/>");

        expect(_qm.findContentRoot()).andReturn(contentRoot);
        _al.recordChangeTemplate(contentRoot);
        replay(_em, _qm, _al);


        // ACT
        _cm.setDefaultTemplate(defaultTemplate);


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(
            defaultTemplate,
            contentRoot.template());
    }

    /**
     * Test.
     */
    public void testLookup() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Page bar =
            new Page(new ResourceName("bar"))
                .addParagraph(
                    Paragraph.fromText("default", "<H1>Default</H!>"));
        contentRoot.add(foo);
        foo.add(bar);

        expect(_qm.findContentRoot()).andReturn(contentRoot);
        replay(_em, _qm, _al);


        // ACT
        final Resource resource =
            _cm.lookup(new ResourcePath("/foo/bar"));


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.paragraphs().size());
    }

    /**
     * Test.
     */
    public  void testCreateFolder() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder bar = new Folder(new ResourceName("bar"));
        final Folder baz = new Folder(new ResourceName("baz"));

        _al.recordCreate(isA(Folder.class));
        _al.recordCreate(isA(Folder.class));
        _al.recordCreate(isA(Folder.class));
        expect(_em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        _em.persist(foo);
        expect(_em.find(Resource.class, foo.id())).andReturn(foo);
        _em.persist(bar);
        expect(_em.find(Resource.class, foo.id())).andReturn(foo);
        _em.persist(baz);
        replay(_em, _qm, _al);


        // ACT
        _cm.create(contentRoot.id(), foo);
        _cm.create(foo.id(), bar);
        _cm.create(foo.id(), baz);


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertEquals(2, contentRoot.entries().get(0).as(Folder.class).size());
        assertEquals(
            "baz",
            contentRoot
                .entries().get(0).as(Folder.class)
                .entries().get(1).name().toString());

    }

    /**
     * Test.
     */
    public void testCreateContent() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Page page1 = new Page(new ResourceName("page1"));
        final Page page2 = new Page(new ResourceName("page2"));

        _al.recordCreate(isA(Folder.class));
        _al.recordCreate(isA(Page.class));
        _al.recordCreate(isA(Page.class));
        expect(_em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        _em.persist(foo);
        expect(_em.find(Resource.class, foo.id())).andReturn(foo);
        _em.persist(page1);
        expect(_em.find(Resource.class, foo.id())).andReturn(foo);
        _em.persist(page2);
        replay(_em, _qm, _al);


        // ACT
        _cm.create(contentRoot.id(), foo);
        _cm.create(foo.id(), page1);
        _cm.create(foo.id(), page2);


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertEquals(2, contentRoot.entries().get(0).as(Folder.class).size());

        final List<Resource> entries =
            contentRoot.entries().get(0).as(Folder.class).entries();
        assertEquals(ResourceType.PAGE, entries.get(0).type());
        assertEquals(ResourceType.PAGE, entries.get(1).type());
        assertEquals("page1", entries.get(0).name().toString());
        assertEquals("page2", entries.get(1).name().toString());
    }

    /**
     * Test.
     */
    public void testCreateContentFailsWhenFolderExists() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder page1F = new Folder(new ResourceName("page1"));
        final Page page1P = new Page(new ResourceName("page1"));

        _al.recordCreate(isA(Folder.class));
        _al.recordCreate(isA(Folder.class));
        expect(_em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        _em.persist(foo);
        expect(_em.find(Resource.class, foo.id())).andReturn(foo);
        _em.persist(page1F);
        expect(_em.find(Resource.class, foo.id())).andReturn(foo);
        replay(_em, _qm, _al);


        // ACT
        _cm.create(contentRoot.id(), foo);
        _cm.create(foo.id(), page1F);
        try {
            _cm.create(foo.id(), page1P);
            fail("Creation of page with"
                    + "same name as existing folder should fail.");
        } catch (final CCCException e) {
            assertEquals(
                "Folder already contains a resource with name 'page1'.",
                e.getMessage());
        }


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, contentRoot.size());
        assertEquals(1, contentRoot.entries().size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertEquals(
            "page1",
            contentRoot
                .entries().get(0).as(Folder.class)
                .entries().get(0).name().toString());
    }

    /**
     * Test.
     */
    public void testCreateParagraph() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Page page1 = new Page(new ResourceName("page1"));
        page1.addParagraph(Paragraph.fromText("HEADER", "test text"));

        _al.recordCreate(isA(Folder.class));
        _al.recordCreate(isA(Page.class));
        expect(_em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        _em.persist(foo);
        expect(_em.find(Resource.class, foo.id())).andReturn(foo);
        _em.persist(page1);
        replay(_em, _qm, _al);


        // ACT
        _cm.create(contentRoot.id(), foo);
        _cm.create(foo.id(), page1);


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, contentRoot.size());
        assertEquals(1, contentRoot.entries().size());

        final Folder folder = contentRoot.entries().get(0).as(Folder.class);
        assertEquals("foo", folder.name().toString());

        final Page page = folder.entries().get(0).as(Page.class);
        assertEquals("page1", page.name().toString());

        assertEquals(1, page.paragraphs().size());
        assertEquals("test text", page.paragraph("HEADER").text());
    }

    /**
     * Test.
     */
    public void testLookupFromId() {

        // ARRANGE
        final Page bar =
            new Page(new ResourceName("bar"))
                .addParagraph(
                    Paragraph.fromText("default", "<H1>Default</H1>"));

        expect(_em.find(Resource.class, bar.id())).andReturn(bar);
        replay(_em, _qm, _al);


        // ACT
        final Resource resource =
            _cm.lookup(bar.id());


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.paragraphs().size());
    }

    /**
     * Test.
     */
    public void testSaveContent() {

        // ARRANGE
        final Page page = new Page(new ResourceName("test"));
        page.addParagraph(Paragraph.fromText("abc", "def"));

        _al.recordUpdate(page);
        expect(_em.find(Resource.class, page.id())).andReturn(page);
        replay(_em, _qm, _al);


        // ACT
        _cm.update(
            page.id(),
            "new title",
            Collections.singleton(Paragraph.fromText("foo", "bar")));


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals("new title", page.title());
        assertEquals(1, page.paragraphs().size());
        assertEquals("foo", page.paragraphs().iterator().next().name());
        assertEquals("bar", page.paragraph("foo").text());
    }

    /**
     * Test.
     */
    public void testCreateAlias() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        contentRoot.add(foo);
        final Alias alias = new Alias(new ResourceName("bar"), foo);

        _al.recordCreate(alias);
        expect(_em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        _em.persist(alias);
        replay(_em, _qm, _al);


        // ACT
        _cm.create(contentRoot.id(), alias);


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(2, contentRoot.size());
        assertEquals(alias, contentRoot.entries().get(1));
    }

    /**
     * Test.
     */
    public void testCreateAliasFailsForDuplicates() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        contentRoot.add(foo);
        final Alias alias = new Alias(new ResourceName("bar"), foo);
        contentRoot.add(alias);
        final Alias aliasCopy = new Alias(new ResourceName("bar"), foo);

        expect(_em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        replay(_em, _qm, _al);


        // ACT
        try {
            _cm.create(contentRoot.id(), aliasCopy);
            fail("Alias creation should fail for duplicate name");


        // ASSERT
        } catch (final CCCException e) {
            assertEquals("Folder already contains a resource with name 'bar'.",
                e.getMessage());
        }
        verify(_em, _qm, _al);
    }

    /**
     * Test.
     */
    public void testMove() {
        // ARRANGE
        final Folder oldParent = new Folder(new ResourceName("old"));
        final Folder newParent = new Folder(new ResourceName("new"));
        final Page resource = new Page(new ResourceName("foo"));
        oldParent.add(resource);

        expect(_em.find(Resource.class, resource.id())).andReturn(resource);
        expect(_em.find(Resource.class, newParent.id())).andReturn(newParent);
        expect(_em.find(Resource.class, oldParent.id())).andReturn(oldParent);
        _al.recordMove(resource);
        replay(_em, _qm, _al);

        // ACT
        _cm.move(resource.id(), newParent.id());

        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(newParent, resource.parent());
    }

    /**
     * Test.
     */
    public void testUpdateAlias() {

        // ARRANGE
        final Page resource = new Page(new ResourceName("foo"));
        final Page r2 = new Page(new ResourceName("baa"));
        final Alias alias = new Alias("alias", resource);

        expect(_em.find(Resource.class, r2.id())).andReturn(r2);
        expect(_em.find(Resource.class, alias.id())).andReturn(alias);
        _al.recordUpdate(alias);
        replay(_em, _qm, _al);

        // ACT
        _cm.updateAlias(r2.id(), alias.id());

        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(r2, alias.target());
    }

    /**
     * Test.
     */
    public void testRename() {
        // ARRANGE
        final Page resource = new Page(new ResourceName("foo"));

        expect(_em.find(Resource.class, resource.id())).andReturn(resource);
        _al.recordRename(resource);
        replay(_em, _qm, _al);

        // ACT
        _cm.rename(resource.id(), "baz");

        // ASSERT
        verify(_em, _qm, _al);
        assertEquals("baz", resource.name().toString());
    }

    //--
    /**
     * Test.
     */
    public void testLookupTemplates() {

        // ARRANGE
        final Template expected =
            new Template("title", "description", "body", "<fields/>");

        expect(_qm.list("allTemplates", Template.class))
            .andReturn(Collections.singletonList(expected));
        replay(_qm, _em, _al);


        // ACT
        final List<Template> templates = _cm.lookupTemplates();


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, templates.size());
        assertEquals(expected, templates.get(0));
    }

    /**
     * Test.
     */
    public void testCreateDisplayTemplateCreatesADisplayTemplate() {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final Folder templateFolder = new Folder(new ResourceName("templates"));
        assetRoot.add(templateFolder);
        final Template t =
            new Template("title", "description", "body", "<fields/>");

        expect(_em.find(Resource.class, templateFolder.id()))
            .andReturn(templateFolder);
        _em.persist(t);
        _al.recordCreate(t);
        replay(_em, _qm, _al);


        // ACT
        _cm.createDisplayTemplate(templateFolder.id(), t);

        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, templateFolder.size());
        assertEquals(t, templateFolder.entries().get(0));
    }
    //--


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _qm = createStrictMock(QueryManager.class);
        _al = createStrictMock(AuditLog.class);
        _cm = new ContentManagerEJB(_em, _qm, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _qm = null;
        _al = null;
        _cm = null;
    }

    private EntityManager _em;
    private QueryManager _qm;
    private AuditLog _al;
    private ContentManager _cm;
}
