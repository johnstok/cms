package ccc.migration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

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
	
	public void testSelectFolders() throws SQLException {
		
		// ARRANGE
		Connection connection = new ConnectionAdapter() {

			@Override
			public Statement createStatement() throws SQLException {
				Statement statement = new StatementAdaptor() {

					@Override
					public ResultSet executeQuery(String sql)
							throws SQLException {
						ResultSet resultSet = new ResultSetAdapter();
						return resultSet;
					}
					
				};
				return statement;
			}
			
		};
		Queries queries = new Queries(connection);
		
		// ACT
		ResultSet rs = queries.selectFolders();
	
		// ASSERT
		assertFalse("ResultSet should not contain any data.", rs.next());
		
	}
	
//	public void testSelectPagesFromFolder() {
//		
//		// ARRANGE
//		Connection connection = new ConnectionAdapter();
//		Queries queries = new Queries(connection);
//		
//		// ACT
//		ResultSet rs = queries.selectPagesFromFolder(null);
//		
//		// ASSERT
//		
//	}
}
