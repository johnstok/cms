package ccc.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static ccc.commons.jee.DBC.*;

public class Queries {
	private final Connection connection;

	public Queries(Connection conn) {
		require().notNull(conn);
		connection = conn;
	}

	public ResultSet selectFolders() {
		try {
			Statement statement;
			statement = connection.createStatement();
			return statement.executeQuery("SELECT * FROM C3_FOLDERS, C3_CONTENT WHERE " +
					"C3_CONTENT.VERSION_ID=0 AND C3_FOLDERS.VERSION_ID=0 AND C3_FOLDERS.FOLDER_ID=C3_CONTENT.CONTENT_ID");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ResultSet selectPagesFromFolder(Long contentId) {
		try {
			PreparedStatement ps;
			ps = connection.prepareStatement("SELECT * FROM C3_PAGES, C3_CONTENT WHERE " +
			" C3_CONTENT.PARENT_ID = ? AND C3_CONTENT.VERSION_ID=0 AND " +
			" C3_PAGES.VERSION_ID=0 AND C3_PAGES.PAGE_ID=C3_CONTENT.CONTENT_ID");
			
			ps.setLong(1, contentId);
			return ps.executeQuery();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
}
