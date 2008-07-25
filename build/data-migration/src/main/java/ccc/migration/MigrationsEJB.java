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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;

import ccc.domain.CCCException;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationsEJB {

    private final ResourceManager manager;
    private final Queries queries;

    /**
     * Constructor.
     *
     * @param manager
     * @param queries
     */
    public MigrationsEJB(final ResourceManager manager, final Queries queries) {
        this.manager = manager;
        this.queries = queries;
    }

    public void migrate() {

        // Create a root content folder.
        createContentRoot();
        final ResourcePath rootFolder = new ResourcePath();

        // Walk the tree migrating each resource
        migrateChildren(rootFolder, 0, queries);
    }
    private void migrateChildren(final ResourcePath path, final Integer parent, final Queries queries) {
        final ResultSet rs = queries.selectResources(parent);
        try {
            while (rs.next()) {
                final String type = rs.getString("CONTENT_TYPE");
                if (type.equals("FOLDER")) {
                    System.out.println(path.toString()+"/"+rs.getString("NAME"));
                    final ResourcePath childFolder =
                        path.append(ResourceName.escape(rs.getString("NAME")));
                    manager.createFolder(childFolder.toString());
                    migrateChildren(childFolder, rs.getInt("CONTENT_ID"), queries);
                }
                else if (type.equals("PAGE")) {
                    System.out.println(">"+path.toString()+"/"+rs.getString("NAME"));
                    final ResourcePath childContent =
                        path.append(ResourceName.escape(rs.getString("NAME")+"_content"));
                    manager.createContent(childContent.toString());
                    migrateParagraphs(childContent, rs.getInt("CONTENT_ID"));
                }
                else {
                    System.out.println("Unkown resource type");
                }
            }
        }
        catch (final SQLException e) {
            throw new CCCException("Migration failed.", e);
        }
        finally {
            DbUtils.closeQuietly(rs);
        }
    }

    /**
     * Merges paragraphs, creates Paragraph objects and calls manager.
     *
     * @param childContent
     * @param pageId
     * @throws SQLException
     */
    private void migrateParagraphs(final ResourcePath path, final int pageId) throws SQLException {
        System.out.println("#### migrating paragraphs for "+pageId );
        final Map<String, Paragraph> map = new HashMap<String, Paragraph>();

        final ResultSet rs = queries.selectParagraphs(pageId);
        // populate map
        while (rs.next()) {
            final String key = rs.getString("PARA_TYPE");
            final String text = rs.getString("TEXT");
            // ignore empty/null texts
            if (text == null) {
                continue;
            }
            if (map.containsKey(key)) {
                // merge
                final String existingText = map.get(key).body();
                final String newText = existingText + text;
                map.put(key, new Paragraph(newText));
                System.out.println("#### merged texts for Paragraph "+key+
                    "Seq "+rs.getString("SEQ"));
            } else {
                // new item
                map.put(key, new Paragraph(text));
                System.out.println("#### Created Paragraph "+key );
            }
        }
        DbUtils.close(rs);
        manager.createParagraphsForContent(path.toString(), map);
    }

    /**
     * @see ccc.migration.Migrations#createContentRoot()
     */
    public void createContentRoot() {
        manager.createRoot();
    }

}
