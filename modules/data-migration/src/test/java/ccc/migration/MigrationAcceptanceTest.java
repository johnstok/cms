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

import junit.framework.TestCase;
import ccc.commons.JNDI;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.ContentManagerRemote;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationAcceptanceTest extends TestCase {

    /**
     * Test.
     *
     */
    public void testFolderMigration() {

        // ARRANGE
        final ContentManagerRemote manager =
            new JNDI().<ContentManagerRemote>get("ContentManager/remote");

        // ACT
        final Resource resource =
            manager.lookup(new ResourcePath("/Home")).get();

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
        final ContentManagerRemote manager =
            new JNDI().<ContentManagerRemote>get("ContentManager/remote");
        final String path = "/Home/ASH_Scotland_Manifesto_2007";

        // ACT
        final Resource resource =
            manager.lookup(new ResourcePath(path)).get();

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
        final ContentManagerRemote manager =
            new JNDI().<ContentManagerRemote>get("ContentManager/remote");

        // ACT
        final Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());
        assertEquals("Resource title must be content ",
            resource.title(), "Introduction");
        assertNotNull(
            "Paragraph HEADER must not be null",
            resource.paragraph("HEADER"));
        assertEquals(
            resource.paragraph("HEADER").text(),
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
        final ContentManagerRemote manager =
            new JNDI().<ContentManagerRemote>get("ContentManager/remote");

        // ACT
        final Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Display template should be null",
            null, resource.template());

    }

    /**
     * Test.
     *
     */
    public void testDisplayNameNotNullMigration() {
        // ARRANGE
        // old ID: 3391
        final String path = "/Quit_Smoking/Quit_smoking";
        final ContentManagerRemote manager =
            new JNDI().<ContentManagerRemote>get("ContentManager/remote");

        // ACT
        final Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Display template should be ",
            "ash_display_sectionhome.jsp",
            resource.template().title());
    }
}
