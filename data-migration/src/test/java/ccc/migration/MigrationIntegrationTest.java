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
import java.sql.DriverManager;
import java.sql.Statement;

import junit.framework.TestCase;
import ccc.commons.jee.JNDI;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationIntegrationTest extends TestCase {
    private Connection conn = null;
    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection(
            "jdbc:h2:mem:CCC_INTEGRATION;DB_CLOSE_DELAY=-1;MAX_MEMORY_UNDO=500000", "CCC", "CCC");
        final Statement stat = conn.createStatement();

        // import CSV file to memory DB
        stat.execute("CREATE TABLE C3_CONTENT AS SELECT * FROM CSVREAD('src/test/resources/C3_CONTENT.csv')");
        stat.execute("CREATE TABLE C3_PARAGRAPHS AS SELECT * FROM CSVREAD('src/test/resources/C3_PARAGRAPHS.csv')");
        stat.close();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }


    public void testMigration() {
        // JBoss should be running and application deployed

        // ARRANGE
        final Queries queries = new Queries(conn);
        final ResourceManager manager = new JNDI().<ResourceManager>get("ResourceManagerEJB/remote");
        assertNotNull("ResourceManager must not be null", manager);

        final MigrationsEJB migrationsEJB = new MigrationsEJB(manager, queries);

        // ACT
        migrationsEJB.migrate();

        // VERIFY
        Resource resource = manager.lookup(new ResourcePath("/Home/"));
        assertNotNull("Resource /home/ must not be null", resource);
        assertEquals("Resource type must be folder ", ResourceType.FOLDER, resource.type());

        resource = manager.lookup(new ResourcePath("/Home/blue_panel_content/"));
        assertNotNull("Resource /Home/blue_panel_content/ must not be null", resource);
        assertEquals("Resource type must be content ", ResourceType.PAGE, resource.type());
    }
}
