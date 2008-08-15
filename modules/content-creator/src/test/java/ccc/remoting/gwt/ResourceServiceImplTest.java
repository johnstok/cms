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
package ccc.remoting.gwt;

import static java.util.Arrays.*;
import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.commons.MapRegistry;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.services.AssetManager;
import ccc.services.ContentManager;
import ccc.services.adaptors.ContentManagerAdaptor;
import ccc.view.contentcreator.client.ResourceService;
import ccc.view.contentcreator.dto.DTO;
import ccc.view.contentcreator.dto.OptionDTO;
import ccc.view.contentcreator.dto.TemplateDTO;


/**
 * TODO Add Description for this type.
 * TODO Test getResource().
 *
 * @author Civic Computing Ltd
 */
public final class ResourceServiceImplTest extends TestCase {

    /**
     * Test.
     */
    public void testListOptions() {

        // ARRANGE
        final Template foo = new Template("foo", "foo", "foo");
        final Template bar = new Template("bar", "bar", "bar");
        final Folder root = new Folder(PredefinedResourceNames.CONTENT);
        // TODO: Set a default template for the root.

        final AssetManager am = createStrictMock(AssetManager.class);
        expect(am.lookupTemplates())
            .andReturn(asList(new Template[]{foo, bar}));

        final ContentManager cm = createStrictMock(ContentManager.class);

        replay(am, cm);

        final ResourceService rs =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("AssetManagerEJB/local", am)
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
//        assertEquals(root.displayTemplateName(), current);
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

        final ContentManager cm = createMock(ContentManager.class);
        cm.setDefaultTemplate(t);

        final AssetManager am = createMock(AssetManager.class);
        expect(am.lookup(t.id())).andReturn(t);

        replay(cm, am);

        final ResourceService rsi =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("ContentManagerEJB/local", cm)
                    .put("AssetManagerEJB/local", am)
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
        final ResourceServiceImpl resourceService =
            new ResourceServiceImpl(
                new MapRegistry(
                "ContentManagerEJB/local",
                new ContentManagerAdaptor() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Folder lookup(final ResourcePath path) {
                        return
                            new Folder(PredefinedResourceNames.CONTENT);
                    }
            }));

        // ACT
        final String jsonRoot = resourceService.getContentRoot();

        // ASSERT
        assertEquals(
            "{\"name\": \"content\"," +
            "\"displayTemplateName\": \"null\"," +
            "\"entries\": []}",
            jsonRoot);
    }

    /**
     * Test.
     */
    public void testCreateTemplate() {

        // ARRANGE
        final AssetManager am = createMock(AssetManager.class);
        final Capture<Template> actual = new Capture<Template>();
        am.createDisplayTemplate(capture(actual));
        replay(am);

        final ResourceServiceImpl resourceService =
            new ResourceServiceImpl(
                new MapRegistry("AssetManagerEJB/local", am));

        // ACT
        resourceService.createTemplate(
            new TemplateDTO("title",
                            "description",
                            "body"));

        // ASSERT
        verify(am);
        assertEquals("title", actual.getValue().title());
        assertEquals("description", actual.getValue().description());
        assertEquals("body", actual.getValue().body());
    }
}
