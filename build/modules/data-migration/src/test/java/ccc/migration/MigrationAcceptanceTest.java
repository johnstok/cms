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
package ccc.migration;

import java.util.Map;

import junit.framework.TestCase;
import ccc.commons.JNDI;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.ContentManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationAcceptanceTest extends TestCase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test.
     *
     */
    public void testFolderMigration() {

        // ARRANGE
        final ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        final Resource resource = manager.lookup(new ResourcePath("/Home"));

        // VERIFY
        assertNotNull("Resource /home/ must not be null", resource);
        assertEquals("Resource type must be folder ",
            ResourceType.FOLDER, resource.type());
    }

    /**
     * Test.
     *
     */
    public void testPageMigration() {

        // ARRANGE
        final ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");
        final String path = "/Home/ASH_Scotland_Manifesto_2007";

        // ACT
        final Resource resource = manager.lookup(
            new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());
    }

    /**
     * Test.
     *
     */
    public void testParagraphMigration() {

        // ARRANGE
        // old ID: 3391
        final String path = "/Information_Service/Key_topics/Smoking_Cessation/"
            +"A_Smoking_Cessation_Policy_for_Scotland/Introduction";
        final ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        final Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Resource title must be content ",
            resource.title(), "Introduction");

        final Map<String, Paragraph> paragraphs = resource.paragraphs();
        assertNotNull("Paragraphs must not be null", paragraphs);

        assertNotNull("Paragraph HEADER must not be null",
            paragraphs.get("HEADER"));
        assertEquals("Paragraph HEADER body ",
            paragraphs.get("HEADER").body(),
        "A Smoking Cessation Policy for Scotland");
    }

    /**
     * Test.
     *
     */
    public void testDisplayNameNullMigration() {
        // ARRANGE
        // old ID: 3391
        final String path = "/Information_Service/Key_topics/Smoking_Cessation/"
            +"A_Smoking_Cessation_Policy_for_Scotland/Introduction";
        final ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        final Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Display template should be null",
            null, resource.displayTemplateName());

    }

    /**
     * Test.
     *
     */
    public void testDisplayNameNotNullMigration() {
        // ARRANGE
        // old ID: 3391
        final String path = "/Quit_Smoking/Quit_smoking";
        final ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        final Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Display template should be ",
            "ash_display_sectionhome.jsp",
            resource.displayTemplateName().title());
    }
}
