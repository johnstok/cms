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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;
import ccc.domain.Template;
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
    private static final Map<String, Template> templates =
        new HashMap<String, Template>();

    private static Logger log =
        Logger.getLogger(ccc.migration.Migrations.class);

    /**
     * Constructor.
     *
     * @param manager
     * @param queries Queries
     */
    public Migrations(final Queries queries) {
        _queries = queries;
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
        migrateChildren(
            contentManager().lookupRoot().id().toString(), 0, _queries);
    }

    private void migrateChildren(final String parentFolderId,
                                 final Integer parent,
                                 final Queries queries) {

        final List<ResourceBean> resources = queries.selectResources(parent);

            for (final ResourceBean r : resources) {

                log.debug("type "+r.type());
                log.debug("name "+r.name());

                // ignore null/empty name
                if (r.name() == null || r.name().trim().equals("")) {
                    log.debug("NO NAME");
                    continue;
                }

                if (r.type().equals("FOLDER")) {
                    migrateFolder(
                        parentFolderId,
                        r.contentId(),
                        r.name(),
                        r.displayTemplate());
                } else if (r.type().equals("PAGE")) {
                    migratePage(
                        parentFolderId,
                        r.contentId(),
                        r.name(),
                        r.displayTemplate());
                } else {
                    log.debug("Unkown resource type");
                }
            }
    }

    private void migrateFolder(final String parentFolderId,
                               final int contentId,
                               final String name,
                               final String displayTemplate) {

        try {
            log.debug("FOLDER");
            Folder child = new Folder(ResourceName.escape(name));

            if (null!=displayTemplate) {
                Template template =
                    (templates.containsKey(displayTemplate))
                        ? templates.get(displayTemplate)
                        : new Template(displayTemplate, "", "");
                template = assetManager().createOrRetrieve(template);
                child.displayTemplateName(template);
                if (!templates.containsKey(displayTemplate)) {
                    templates.put(displayTemplate, template);
                }
            }

            contentManager().create(UUID.fromString(parentFolderId), child);

            final String childId = child.id().toString();
            child = null;
            migrateChildren(childId, contentId, _queries);

        } catch (final Exception e) {
            log.error(e.getMessage());
        }
    }

    private void migratePage(final String parentFolderId,
                             final int contentId,
                             final String name,
                             final String displayTemplate) {

        try {
            log.debug("PAGE");
            final Page childPage = new Page(ResourceName.escape(name));

            if (null!=displayTemplate) {
                Template template =
                    (templates.containsKey(displayTemplate))
                        ? templates.get(displayTemplate)
                        : new Template(displayTemplate, "", "");
                template = assetManager().createOrRetrieve(template);
                childPage.displayTemplateName(template);
                if (!templates.containsKey(displayTemplate)) {
                    templates.put(displayTemplate, template);
                }
            }

            final Map<String, StringBuffer> paragraphs =
                migrateParagraphs(contentId);
            for (final String key : paragraphs.keySet()) {
                childPage.addParagraph(key,
                    new Paragraph(paragraphs.get(key).toString()));
            }

            contentManager().create(UUID.fromString(parentFolderId), childPage);

        } catch (final Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Merges paragraphs, returns map of joined paragraph text.
     *
     * @param path
     * @param pageId
     * @throws SQLException
     */
    private Map<String, StringBuffer>
        migrateParagraphs(final int pageId) {

        log.debug("#### migrating paragraphs for "+pageId);
        final Map<String, StringBuffer> map =
            new HashMap<String, StringBuffer>();

        final List<ParagraphBean> paragraphs =
            _queries.selectParagraphs(pageId);
        // populate map
        for (final ParagraphBean p : paragraphs) {
            // ignore empty/null texts
            if (p.text() == null || p.text().trim().equals("")) {
                continue;
            }
            if (map.containsKey(p.key())) {
                // merge
                final StringBuffer sb = map.get(p.key());
                map.put(p.key(), sb.append(p.text()));
                log.debug("#### merged texts for Paragraph "+p.key());
            } else {
                // new item
                map.put(p.key(), new StringBuffer(p.text()));
                log.debug("#### Created Paragraph "+p.key());
            }
        }
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
