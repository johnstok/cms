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

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.h2.tools.Csv;

import ccc.services.ResourceManager;


/**
 * Tests for the {@link MigrationsEJB}.
 *
 * @author Civic Computing Ltd
 */
public final class MigrationsEJBTest extends TestCase {

    /**
     * Test.
     *
     * @throws IOException If an error occurs in the {@link ResultSet}.
     * @throws SQLException If an error occurs in the {@link ResultSet}.
     */
    public void testMigrateFolders() throws SQLException, IOException {

        // ARRANGE
        final ResultSet rs = Csv.getInstance().read(
            new StringReader("testFolder, 1, 0"),
            new String[]{"NAME", "CONTENT_ID", "PARENT_ID"});

        ResourceManager manager = createMock(ResourceManager.class);
        manager.createFolder("/testFolder");
        replay(manager);

        // ACT
        MigrationsEJB migrationsEJB = new MigrationsEJB(manager, getQueries());
        List<Integer> results = migrationsEJB.migrateFolders(rs);

        // VERIFY
        verify(manager);

        // ASSERT
        List<Integer> expectedList = new ArrayList<Integer>();
        expectedList.add(1);
        assertEquals(expectedList, results);
    }
    
    public void testMigratePages() throws SQLException, IOException {
        
        // ARRANGE
        final ResultSet rs = Csv.getInstance().read(
            new StringReader("testPage, 1, 0"),
            new String[]{"NAME", "CONTENT_ID", "PARENT_ID"});

        ResourceManager manager = createMock(ResourceManager.class);
        manager.createContent("/testPage");
        replay(manager);

        MigrationsEJB migrationsEJB = new MigrationsEJB(manager, getQueries());

        // ACT
        migrationsEJB.migratePages(rs);

        // VERIFY
        verify(manager);
        
    }

    /**
     * Test.
     */
    public void testCreateContentRoot() {

        // ARRANGE
        ResourceManager manager = createMock(ResourceManager.class);
        manager.createRoot();
        replay(manager);

        MigrationsEJB migrationsEJB = new MigrationsEJB(manager, getQueries());

        // ACT
        migrationsEJB.createContentRoot();

        // VERFIY
        verify(manager);
    }

    private Queries getQueries() {
        Connection connection = new ConnectionAdapter() {
            @Override
            public Statement createStatement() throws SQLException {
                Statement statement = new StatementAdaptor() {

                    @Override
                    public ResultSet executeQuery(final String sql)
                    throws SQLException {
                        ResultSet resultSet = new ResultSetAdapter();
                        return resultSet;
                    }

                };
                return statement;
            }
        };
        return new Queries(connection);
    }
    
}
