package ccc.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class QueriesTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstructRejectsNull() {

        // ACT
        try {
            new Queries(null);
            fail("Queries failed to reject a NULL connection.");
        } catch (IllegalArgumentException e) {

            //ASSERT
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    public void testSelectResources() throws SQLException {

        // ARRANGE
        Connection connection = new ConnectionAdapter() {

            @Override
            public PreparedStatement prepareStatement(String sql) throws SQLException {
                PreparedStatement ps = new PreparedStatementAdapter() {

                    @Override
                    public ResultSet executeQuery() throws SQLException {
                        ResultSet resultSet = new ResultSetAdapter();
                        return resultSet;
                    }

                    @Override
                    public void setInt(int parameterIndex, int x) throws SQLException {
                    }
                };
                return ps;
            }
        };
        Queries queries = new Queries(connection);

        // ACT
        ResultSet rs = queries.selectResources(0);

        // ASSERT
        assertFalse("ResultSet should not contain any data.", rs.next());
    }

}
