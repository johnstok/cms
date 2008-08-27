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

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.persistence.NoResultException;

import junit.framework.TestCase;
import oracle.jdbc.pool.OracleDataSource;
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
    private Connection _conn = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");
        final String driverName = "oracle.jdbc.driver.OracleDriver";
        Class.forName(driverName);

        // Create a connection to the database
        final String serverName = "poseidon";
        final String portNumber = "1521";
        final String sid = "DEV";
        final String url =
            "jdbc:oracle:thin:@"
            + serverName + ":"
            + portNumber + ":"
            + sid;
        final String username = "ccc_migration";
        final String password = "d3ccc_migration";

        OracleDataSource ods = new OracleDataSource();
        Properties props = new Properties();
        props.put("user", username);
        props.put("password", password);
        props.put("oracle.jdbc.FreeMemoryOnEnterImplicitCache", true);
        ods.setConnectionProperties(props);
        ods.setURL(url);
        _conn = ods.getConnection();

        final Queries queries = new Queries(_conn);

        /*
         * Rather stupid way to check whether migration is done already.
         * Maybe TestNG would be a better choice.
         */
        try {
            manager.lookupRoot();
        } catch (EJBException e) {
            if (e.getCausedByException().getClass()
                    == NoResultException.class) {
                final Migrations migrationsEJB = new Migrations(queries);
                migrationsEJB.migrate();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
        _conn.close();
    }

    /**
     * Test.
     *
     */
    public void testFolderMigration() {

        // ARRANGE
        ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        Resource resource = manager.lookup(new ResourcePath("/Home/"));

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
        ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");
        String path = "/Home/ASH_Scotland_Manifesto_2007/";

        // ACT
        Resource resource = manager.lookup(
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
        String path = "/Information_Service/Key_topics/Smoking_Cessation/"
            +"A_Smoking_Cessation_Policy_for_Scotland/Introduction/";
        ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Resource title must be content ",
            resource.title(), "Introduction");

        Map<String, Paragraph> paragraphs = resource.paragraphs();
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
        String path = "/Information_Service/Key_topics/Smoking_Cessation/"
            +"A_Smoking_Cessation_Policy_for_Scotland/Introduction/";
        ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        Page resource = manager.eagerPageLookup(new ResourcePath(path));

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
        String path = "/Quit_Smoking/Quit_smoking/";
        ContentManager manager = new JNDI().<ContentManager>get(
        "ContentManagerEJB/remote");

        // ACT
        Page resource = manager.eagerPageLookup(new ResourcePath(path));

        // VERIFY
        assertNotNull("Resource "+path+" must not be null", resource);
        assertEquals("Resource type must be content ",
            ResourceType.PAGE, resource.type());

        assertEquals("Display template should be ",
            "ash_display_sectionhome.jsp",
            resource.displayTemplateName().title());
    }
}
