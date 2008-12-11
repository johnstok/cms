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
import ccc.services.ServiceNames;
import ccc.services.StatefulReader;


/**
 * TODO Add Description for this type.
 * FIXME These tests are never run!!!
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
        final StatefulReader manager =
            new JNDI().get(ServiceNames.STATEFUL_READER);

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
        final StatefulReader manager =
            new JNDI().get(ServiceNames.STATEFUL_READER);
        final String path = "/Home/ASH_Scotland_Manifesto_2007";

        // ACT
        final Resource resource = manager.lookup(new ResourcePath(path));

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
        final StatefulReader manager =
            new JNDI().get(ServiceNames.STATEFUL_READER);

        // ACT
        final Page resource =
            manager.lookup(new ResourcePath(path)).as(Page.class);

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
        final StatefulReader manager =
            new JNDI().get(ServiceNames.STATEFUL_READER);

        // ACT
        final Page resource =
            manager.lookup(new ResourcePath(path)).as(Page.class);

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
        final StatefulReader manager =
            new JNDI().get(ServiceNames.STATEFUL_READER);

        // ACT
        final Page resource =
            manager.lookup(new ResourcePath(path)).as(Page.class);

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Display template should be ",
            "ash_display_sectionhome.jsp",
            resource.template().title());
    }
}
