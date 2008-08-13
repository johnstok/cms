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
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;
import ccc.services.AssetManager;
import ccc.services.ContentManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Migrations {

    private final Queries _queries;
    private final Registry _registry = new JNDI();

    private static Logger log =
        Logger.getLogger(ccc.migration.Migrations.class);

    /**
     * Constructor.
     *
     * @param manager
     * @param queries
     */
    public Migrations(final Queries queries) {
        this._queries = queries;
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    public void migrate() {

        // Create a root assets folder.
        assetManager().createRoot();

        // Create a root content folder.
        contentManager().createRoot();

        // Walk the tree migrating each resource
        migrateChildren(contentManager().lookupRoot(), 0, _queries);
    }

    private void migrateChildren(final Folder parentFolder,
                                 final Integer parent,
                                 final Queries queries) {

        final ResultSet rs = queries.selectResources(parent);

        try {
            while (rs.next()) {
                final String type = rs.getString("CONTENT_TYPE");
                final String name = rs.getString("NAME");

                log.debug(parentFolder.name().toString());

                // ignore null/empty name
                if (name == null || name.equals("")) {
                    log.debug("NO NAME");
                    continue;
                }

                if (type.equals("FOLDER")) {
                    migrateFolder(parentFolder, queries, rs, name);
                } else if (type.equals("PAGE")) {
                    migratePage(parentFolder, rs, name);
                } else {
                    log.debug("Unkown resource type");
                }
            }
        } catch (final SQLException e) {
            throw new CCCException("Migration failed.", e);
        } finally {
            DbUtils.closeQuietly(rs);
        }
    }

    private void migrateFolder(final Folder parentFolder,
                               final Queries queries,
                               final ResultSet rs,
                               final String name) throws SQLException {

        log.debug("FOLDER");
        Folder child = new Folder(ResourceName.escape(name));

        try {
            contentManager().create(parentFolder.id(), child);
        } catch (final Exception e) {
            log.error("Name conflict : "+e.getMessage());
        }
        migrateChildren(child, rs.getInt("CONTENT_ID"), queries);
    }

    private void migratePage(final Folder parentFolder,
                             final ResultSet rs,
                             final String name) throws SQLException {

        log.debug("PAGE");
        Page childPage = new Page(ResourceName.escape(name));
        final Map<String, Paragraph> paragraphs =
            migrateParagraphs(rs.getInt("CONTENT_ID"));
        for (String key : paragraphs.keySet()) {
            childPage.addParagraph(key, paragraphs.get(key));
        }
        try {
            contentManager().create(parentFolder.id(), childPage);
        } catch (final Exception e) {
            log.error("Name conflict : "+e.getMessage());
        }
    }

    /**
     * Merges paragraphs, creates Paragraph objects and calls manager.
     *
     * @param path
     * @param pageId
     * @throws SQLException
     */
    private Map<String, Paragraph>
        migrateParagraphs(final int pageId) throws SQLException {

        log.debug("#### migrating paragraphs for "+pageId);
        final Map<String, Paragraph> map = new HashMap<String, Paragraph>();

        final ResultSet rs = _queries.selectParagraphs(pageId);
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
                log.debug("#### Created Paragraph "+key);
            }
        }
        DbUtils.close(rs);
        return map;
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
