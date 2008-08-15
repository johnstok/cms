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
    public void testListTemplates() {

        // ARRANGE
        final AssetManager am = createMock(AssetManager.class);
        final Template foo = new Template("foo", "foo", "foo");
        final Template bar = new Template("bar", "bar", "bar");
        expect(am.lookupTemplates())
            .andReturn(asList(new Template[]{foo, bar}));
        replay(am);

        final ResourceServiceImpl rsi =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("AssetManagerEJB/local", am)
            );

        // ACT
        final List<TemplateDTO> templates = rsi.listTemplates();

        // ASSERT
        verify(am);
        assertEquals(2, templates.size());
        assertEquals(templates.get(0).getTitle(), "foo");
        assertEquals(templates.get(0).getId(), foo.id().toString());
        assertEquals(templates.get(1).getBody(), "bar");
        assertEquals(templates.get(1).getId(), bar.id().toString());
    }

    /**
     * Test.
     */
    public void testSetDefaultTemplate() {

        // ARRANGE
        final Template defaultTemplate = new Template("foo", "bar", "baz");

        final ContentManager cm = createMock(ContentManager.class);
        cm.setDefaultTemplate(defaultTemplate);

        final AssetManager am = createMock(AssetManager.class);
        expect(am.lookup(defaultTemplate.id())).andReturn(defaultTemplate);

        replay(cm, am);

        final ResourceServiceImpl rsi =
            new ResourceServiceImpl(
                new MapRegistry()
                    .put("ContentManagerEJB/local", cm)
                    .put("AssetManagerEJB/local", am)
            );

        // ACT
        rsi.setDefaultTemplate(defaultTemplate.id().toString());

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
