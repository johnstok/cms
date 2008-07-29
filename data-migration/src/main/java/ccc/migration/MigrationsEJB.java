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
import org.apache.log4j.Logger;

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

    private static Logger log = Logger.getLogger(ccc.migration.MigrationsEJB.class);
    
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
                final String name = rs.getString("NAME");
                log.debug(path.toString()+name);
                // ignore null/empty name
                if (name == null || name.equals("")) {
                    log.debug("NO NAME");
                    continue;
                }
                if (type.equals("FOLDER")) {
                    log.debug("FOLDER");
                    final ResourcePath childFolder =
                        path.append(ResourceName.escape(name));
                    manager.createFolder(childFolder.toString());
                    migrateChildren(childFolder, rs.getInt("CONTENT_ID"), queries);
                }
                else if (type.equals("PAGE")) {
                    log.debug("PAGE");
                    final ResourcePath childContent =
                        path.append(ResourceName.escape(name+"_content"));
                    manager.createContent(childContent.toString());
                    migrateParagraphs(childContent, rs.getInt("CONTENT_ID"));
                }
                else {
                    log.debug("Unkown resource type");
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
        log.debug("#### migrating paragraphs for "+pageId );
        final Map<String, Paragraph> map = new HashMap<String, Paragraph>();

        final ResultSet rs = queries.selectParagraphs(pageId);
        // populate map
        while (rs.next()) {
            final String key = rs.getString("PARA_TYPE");
            final String text = rs.getString("TEXT");
            // ignore empty/null texts
            if (text == null || text.equals("")) {
                continue;
            }
            if (map.containsKey(key)) {
                // merge
                final String existingText = map.get(key).body();
                final String newText = existingText + text;
                map.put(key, new Paragraph(newText));
                log.debug("#### merged texts for Paragraph "+key+
                    "Seq "+rs.getString("SEQ"));
            } else {
                // new item
                map.put(key, new Paragraph(text));
                log.debug("#### Created Paragraph "+key );
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
