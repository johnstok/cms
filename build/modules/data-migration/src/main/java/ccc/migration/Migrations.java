/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev: 220 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2008-08-07 15:22:12 +0100 (Thu, 07 Aug 2008) $
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

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.CCCException;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.AssetManager;
import ccc.services.ContentManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Migrations {

    private final Queries queries;
    private Registry _registry = new JNDI();

    private static Logger log = Logger.getLogger(ccc.migration.Migrations.class);

    /**
     * Constructor.
     *
     * @param manager
     * @param queries
     */
    public Migrations(final Queries queries) {
        this.queries = queries;
    }

    public void migrate() {

        // Create a root assets folder.
        assetManager().createRoot();

        // Create a root content folder.
    	contentManager().createRoot();
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
                    ResourcePath childFolder =
                        path.append(ResourceName.escape(name));
                    try {
                    	contentManager().createFolder(childFolder.toString());
                    }
                    catch (final Exception e) {
                    	log.error("Name conflict : "+e.getMessage());
                    	childFolder = path.append(ResourceName.escape(name+"_renamed"));
                    	contentManager().createFolder(childFolder.toString());
					}
                    migrateChildren(childFolder, rs.getInt("CONTENT_ID"), queries);
                }
                else if (type.equals("PAGE")) {
                    log.debug("PAGE");
                    ResourcePath childContent =
                        path.append(ResourceName.escape(name));
                    try {
                    	contentManager().createContent(childContent.toString(), name);
                    }
                    catch (final Exception e) {
                    	log.error("Name conflict : "+e.getMessage());
                    	childContent =
                            path.append(ResourceName.escape(name+"_renamed"));
                    	contentManager().createContent(childContent.toString(), name);
					}
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
        contentManager().createParagraphsForContent(path.toString(), map);
    }

    /**
     * Accessor for the content manager.
     *
     * @return A {@link ContentManager}.
     */
    ContentManager contentManager() {
        return _registry.get("ContentManagerEJB/remote");
    }

    /**
     * Accessor for the asset manager.
     *
     * @return An {@link AssetManager}.
     */
    AssetManager assetManager() {
        return _registry.get("AssetManagerEJB/remote");
    }
}
