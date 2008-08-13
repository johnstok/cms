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

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.commons.MapRegistry;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.services.AssetManager;
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
