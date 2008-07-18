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

import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.easymock.EasyMock.*;
import org.h2.tools.Csv;

import ccc.services.ResourceManager;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationsEJBTest extends TestCase {

    public void testMigrateFolders() throws SQLException, IOException {

        // ARRANGE
        final ResultSet rs = Csv.getInstance().read(
            new StringReader("testName, 1, 0"),
            new String[]{"NAME", "CONTENT_ID", "PARENT_ID"});
        
        ResourceManager manager = createMock(ResourceManager.class);
        manager.createFolder("/testName");
        replay(manager);
        
        // ACT
        MigrationsEJB migrationsEJB = new MigrationsEJB(manager);
        migrationsEJB.migrateFolders(rs);
        
        // VERIFY
        verify(manager);
    }
    
}
