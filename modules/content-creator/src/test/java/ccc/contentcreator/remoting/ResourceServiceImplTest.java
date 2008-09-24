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
package ccc.contentcreator.remoting;

import static java.util.Arrays.*;
import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.Capture;
import org.easymock.IAnswer;

import ccc.commons.MapRegistry;
import ccc.commons.Maybe;
import ccc.contentcreator.api.ResourceService;
import ccc.contentcreator.api.Root;
import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.OptionDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.domain.Alias;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.services.AssetManagerLocal;
import ccc.services.ContentManagerLocal;


/**
 * TODO Add Description for this type.
 * TODO Test getResource().
 *
 * @author Civic Computing Ltd
 */
public final class ResourceServiceImplTest extends TestCase {

    private ContentManagerLocal _cm;
    private AssetManagerLocal _am;
    private ResourceService _rsi;
    private Folder _root;

    /**
     * Test.
     */
    public void testGetAbsolutePath() {

        // ARRANGE
        final Folder f = new Folder(new ResourceName("foo"));
        final Page p = new Page(new ResourceName("bar"));
        f.add(p);

        expect(_cm.lookup(p.id())).andReturn(p);
        replay(_cm);

        // ACT
        final String actual =
            _rsi.getAbsolutePath(DTOs.<ResourceDTO>dtoFrom(p));

        // ASSERT
        assertEquals("/foo/bar", actual);
    }

    /**
     * Test.
     */
    public void testCreateFolder() {

        // ARRANGE
        final Folder parent = new Folder(new ResourceName("parent"));
        final Capture<Folder> actual = new Capture<Folder>();
        expect(_cm.create(eq(parent.id()), capture(actual))).andAnswer(
            new IAnswer<Folder>(){
                public Folder answer() throws Throwable {
                    return actual.getValue();
                }});
        replay(_cm);

        // ACT
        _rsi.createFolder(DTOs.<FolderDTO>dtoFrom(parent), "foo");

        // ASSERT
        verify(_cm);
        assertEquals(new ResourceName("foo"), actual.getValue().name());
    }

    /**
     * Test.
     */
    public void testGetChildren() {

        // ARRANGE
        final Folder parent = new Folder(new ResourceName("parent"));
        parent.add(new Folder(new ResourceName("child")));
        parent.add(new Page(new ResourceName("page")));
        final FolderDTO parentDTO = DTOs.dtoFrom(parent);
        expect(_cm.lookup(parent.id())).andReturn(parent);
        replay(_cm);

        // ACT
        final List<ResourceDTO> children = _rsi.getChildren(parentDTO);

        // ASSERT
        verify(_cm);
        assertEquals(2, children.size());
    }

    /**
     * Test.
     */
    public void testGetFolderChildren() {

        // ARRANGE
        final Folder parent = new Folder(new ResourceName("parent"));
        parent.add(new Folder(new ResourceName("child")));
        parent.add(new Page(new ResourceName("page")));
        final FolderDTO parentDTO = DTOs.dtoFrom(parent);

        expect(_cm.lookup(parent.id())).andReturn(parent);
        replay(_cm);

        // ACT
        final List<FolderDTO> children = _rsi.getFolderChildren(parentDTO);

        // ASSERT
        verify(_cm);
        assertEquals(1, children.size());
    }

    /**
     * Test.
     */
    public void testListOptions() {

        // ARRANGE
        final Template foo = new Template("foo", "foo", "foo");
        final Template bar = new Template("bar", "bar", "bar");
        final Folder root = new Folder(PredefinedResourceNames.CONTENT);
        root.displayTemplateName(foo);

        final AssetManagerLocal am = createStrictMock(AssetManagerLocal.class);
        expect(am.lookupTemplates())
            .andReturn(asList(new Template[]{foo, bar}));

        final ContentManagerLocal cm =
            createStrictMock(ContentManagerLocal.class);
        expect(cm.lookupRoot()).andReturn(root);
        replay(am, cm);

        final ResourceService rs =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("AssetManager/local", am)
                    .put("ContentManager/local", cm)
            );

        // ACT
        final List<OptionDTO<? extends DTO>> options = rs.listOptions();

        // ASSERT
        verify(am, cm);

        final OptionDTO<TemplateDTO> templateOption =
            options.get(0).makeTypeSafe();
        final List<TemplateDTO> templates = templateOption.getChoices();
        final TemplateDTO current = templateOption.getCurrentValue();

        assertEquals(2, templates.size());
        assertEquals(templates.get(0).getTitle(), "foo");
        assertEquals(templates.get(0).getId(), foo.id().toString());
        assertEquals(templates.get(1).getBody(), "bar");
        assertEquals(templates.get(1).getId(), bar.id().toString());
        assertEquals(root.displayTemplateName(), DTOs.templateFrom(current));
    }

    /**
     * Test.
     */
    public void testSetDefaultTemplate() {

        // ARRANGE
        final List<OptionDTO<? extends DTO>> options =
            new ArrayList<OptionDTO<? extends DTO>>();
        final Template t = new Template("foo", "bar", "baz");
        final OptionDTO<TemplateDTO> defaultTemplate =
            new OptionDTO<TemplateDTO>(null,
                                       new ArrayList<TemplateDTO>(),
                                       OptionDTO.Type.CHOICES);
        defaultTemplate.setCurrentValue(DTOs.dtoFrom(t));
        options.add(defaultTemplate);

        final ContentManagerLocal cm = createMock(ContentManagerLocal.class);
        cm.setDefaultTemplate(t);

        final AssetManagerLocal am = createMock(AssetManagerLocal.class);
        expect(am.lookup(t.id())).andReturn(t);

        replay(cm, am);

        final ResourceService rsi =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("ContentManager/local", cm)
                    .put("AssetManager/local", am)
            );

        // ACT
        rsi.updateOptions(options);

        // ASSERT
        verify(cm, am);
    }

    /**
     * Test.
     */
    public void testGetContentRoot() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);

        final ContentManagerLocal cm =
            createStrictMock(ContentManagerLocal.class);
        expect(cm.lookup(new ResourcePath("")))
            .andReturn(new Maybe<Resource>(contentRoot));
        replay(cm);

        final ResourceServiceImpl resourceService =
            new ResourceServiceImpl(
                new MapRegistry(
                "ContentManager/local",
                cm
            ));

        // ACT
        final FolderDTO jsonRoot = resourceService.getRoot(Root.CONTENT);

        // ASSERT
        verify(cm);
        assertEquals(
            contentRoot.id().toString(),
            jsonRoot.getId());
    }

    /**
     * Test.
     */
    public void testGetResource() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);

        final ContentManagerLocal cm =
            createStrictMock(ContentManagerLocal.class);
        expect(cm.lookup(contentRoot.id()))
            .andReturn(contentRoot);
        replay(cm);

        final ResourceServiceImpl resourceService =
            new ResourceServiceImpl(
                new MapRegistry(
                "ContentManager/local",
                cm
            ));

        // ACT
        final FolderDTO jsonRoot =
            (FolderDTO) resourceService.getResource(contentRoot
                                                        .id()
                                                        .toString());

        // ASSERT
        verify(cm);
        assertEquals(
            contentRoot.id().toString(),
            jsonRoot.getId());
    }

    /**
     * Test.
     */
    public void testCreateTemplate() {

        // ARRANGE
        final AssetManagerLocal am = createMock(AssetManagerLocal.class);
        final Capture<Template> actual = new Capture<Template>();
        am.createDisplayTemplate(capture(actual));
        replay(am);

        final ResourceServiceImpl resourceService =
            new ResourceServiceImpl(
                new MapRegistry("AssetManager/local", am));

        // ACT
        resourceService.createTemplate(
            new TemplateDTO(
                null,
                0,
                "name",
                "title",
                "description",
                "body"));

        // ASSERT
        verify(am);
        assertEquals("title", actual.getValue().title());
        assertEquals("description", actual.getValue().description());
        assertEquals("body", actual.getValue().body());
    }

    /**
     * Test.
     */
    public void testSetTemplateForResource() {

        // ARRANGE
        final Folder testFolder = new Folder(new ResourceName("testFolder"));

        final List<OptionDTO<? extends DTO>> options =
            new ArrayList<OptionDTO<? extends DTO>>();
        final Template t = new Template("foo", "bar", "baz");
        final OptionDTO<TemplateDTO> templateDTO =
            new OptionDTO<TemplateDTO>(null,
                                       new ArrayList<TemplateDTO>(),
                                       OptionDTO.Type.CHOICES);
        templateDTO.setCurrentValue(DTOs.dtoFrom(t));
        options.add(templateDTO);

        final ContentManagerLocal cm = createMock(ContentManagerLocal.class);
        cm.updateTemplateForResource(testFolder.id(), t);

        final AssetManagerLocal am = createMock(AssetManagerLocal.class);
        expect(am.lookup(t.id())).andReturn(t);

        replay(cm, am);

        final ResourceService rsi =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("ContentManager/local", cm)
                    .put("AssetManager/local", am)
            );

        // ACT
        rsi.updateResourceTemplate(options, DTOs.dtoFrom(testFolder));

        // ASSERT
        verify(cm, am);
    }

    /**
     * Test.
     */
    public void testCreateAlias() {

        // ARRANGE
        final Folder root = new Folder(PredefinedResourceNames.CONTENT);
        final Folder target = new Folder(new ResourceName("target"));
        final ContentManagerLocal cm = createMock(ContentManagerLocal.class);
        final Capture<Alias> actual = new Capture<Alias>();

        expect(cm.lookup(target.id())).andReturn(target);
        expect(cm.lookup(root.id())).andReturn(root);

        cm.create(eq(root.id()), capture(actual));
        replay(cm);

        final ResourceServiceImpl resourceService =
            new ResourceServiceImpl(
                new MapRegistry("ContentManager/local", cm));

        // ACT
        resourceService.createAlias(
           DTOs.<FolderDTO>dtoFrom(root),
            new AliasDTO(
                null,
                0,
                "name",
                "title",
                target.id().toString()));

        // ASSERT
        verify(cm);
        assertEquals("name", actual.getValue().title());
        assertEquals(target.id(), actual.getValue().target().id());
    }

    /**
     * Test.
     */
    public void testNameExistsInFolder() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"));
        _root.add(p);

        expect(_cm.lookup(_root.id())).andReturn(_root);
        replay(_cm);

        // ACT
        final boolean shouldBeTrue =
            _rsi.nameExistsInFolder(DTOs.<FolderDTO>dtoFrom(_root), "foo");

        // ASSERT
        verify(_cm);
        assertTrue("Name should exist", shouldBeTrue);
    }

    /**
     * Test.
     */
    public void testNameNotUsedInFolder() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"));
        _root.add(p);

        expect(_cm.lookup(_root.id())).andReturn(_root);
        replay(_cm);

        // ACT
        final boolean shouldBeFalse =
            _rsi.nameExistsInFolder(DTOs.<FolderDTO>dtoFrom(_root), "bar");


        // ASSERT
        verify(_cm);
        assertFalse("Name shouldn't exist", shouldBeFalse);
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _cm = createStrictMock(ContentManagerLocal.class);
        _am = createStrictMock(AssetManagerLocal.class);
        _rsi =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("ContentManager/local", _cm)
                    .put("AssetManager/local", _am)
            );
        _root = new Folder(PredefinedResourceNames.CONTENT);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _cm = null;
        _am = null;
        _rsi = null;
        _root = null;
    }

}
