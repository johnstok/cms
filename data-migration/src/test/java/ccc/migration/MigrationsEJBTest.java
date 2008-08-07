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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;
import ccc.services.ContentManager;


/**
 * Tests for the {@link MigrationsEJB}.
 *
 * @author Civic Computing Ltd
 */
public final class MigrationsEJBTest extends TestCase {


    /**
     * Test.
     */
    public void testCreateContentRoot() {

        // ARRANGE
        ContentManager manager = createMock(ContentManager.class);
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
        Queries queries = new Queries(connection);
        return queries; 
    }

}
