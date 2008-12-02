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

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.commons.XHTML;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AssetManagerRemote;
import ccc.services.ContentManagerRemote;
import ccc.services.ServiceNames;
import ccc.services.UserManagerRemote;

/**
 * Data migration from CCC6 to CCC7.
 * TODO: Factor out duplicate template creation code.
 *
 * @author Civic Computing Ltd
 */
public class Migrations {

    private final LegacyDBQueries _queries;
    private final Registry _registry = new JNDI();
    /** _templates : Map. */
    private final Map<String, Template> _templates =
        new HashMap<String, Template>();

    private final Map<Integer, User> _users = new HashMap<Integer, User>();

    private static Logger log =
        Logger.getLogger(ccc.migration.Migrations.class);

    /**
     * Constructor.
     *
     * @param manager
     * @param queries Queries
     */
    public Migrations(final LegacyDBQueries queries) {
        _queries = queries;
    }

    /**
     * Creates root and migrates all children under it.
     *
     */
    public void migrate() {
        // Create a root assets folder.
        assetManager().createRoot();

        // Create a root content folder.
        contentManager().createRoot();

        // Migrate users
        migrateUsers(_queries);

        // Walk the tree migrating each resource
        migrateChildren(
            contentManager().lookupRoot().id().toString(), 0, _queries);
    }

    private void migrateUsers(final LegacyDBQueries queries) {
        final List<UserBean> mus = queries.selectUsers();
        for (final UserBean mu : mus) {
            final User u = userManager().createUser(mu.user(), mu.password());
            _users.put(mu.legacyId(), u);
        }
    }

    private void migrateChildren(final String parentFolderId,
                                 final Integer parent,
                                 final LegacyDBQueries queries) {

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
                migrateFolder(parentFolderId, r);
            } else if (r.type().equals("PAGE")) {
                migratePage(parentFolderId, r);
            } else {
                log.debug("Unkown resource type");
            }
        }
    }

    private void migrateFolder(final String parentFolderId,
                               final ResourceBean r) {

        try {
            log.debug("FOLDER");
            Folder child = new Folder(r.name());
            migrateTemplate(r, child);
            migratePublish(r, child);
            contentManager().create(UUID.fromString(parentFolderId), child);

            final String childId = child.id().toString();
            child = null;
            migrateChildren(childId, r.contentId(), _queries);

        } catch (final Exception e) {
            log.error(e.getMessage());
        }
    }

    private void migratePage(final String parentFolderId,
                             final ResourceBean r) {

        try {
            log.debug("PAGE");
            final Page childPage = new Page(r.name());
            migrateTemplate(r, childPage);
            migratePublish(r, childPage);

            final Map<String, StringBuffer> paragraphs =
                migrateParagraphs(r.contentId());
            for (final Map.Entry<String, StringBuffer> para
                : paragraphs.entrySet()) {
                childPage.addParagraph(para.getKey(),
                    Paragraph.fromText(para.getValue().toString()));
            }

            contentManager().create(UUID.fromString(parentFolderId), childPage);

        } catch (final Exception e) {
            log.error(e.getMessage());
        }
    }

    private void migrateTemplate(final ResourceBean r, final Resource child) {
        final String templateName = r.displayTemplate();
        if (null != templateName) {
            Template template =
                (_templates.containsKey(templateName))
                ? _templates.get(templateName)
                    : new Template(templateName,
                        "No description.", "Empty template!", "<fields/>");
                template = assetManager().createOrRetrieve(template);
                child.displayTemplateName(template);
                if (!_templates.containsKey(templateName)) {
                    _templates.put(templateName, template);
                }
        }
    }

    /**
     * Merges paragraphs, returns map of joined paragraph text.
     *
     * @param path
     * @param pageId
     */
    private Map<String, StringBuffer> migrateParagraphs(final int pageId) {

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


    private void migratePublish(final ResourceBean r, final Resource child) {
        // set publish user
        if (r.isPublished()) {
            final Integer legacyUserId =
                _queries.selectUserFromLog(r.contentId(),
                                           r.legacyVersion(),
                                           "CHANGE STATUS",
                                           "Changed Status to  PUBLISHED");
            if (legacyUserId != null) {
                final User user =_users.get(legacyUserId);
                if (user != null) {
                    child.publish(user);
                }
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param map
     */
    private void extractURLs(final Map<String, StringBuffer> map) {

        for (final Map.Entry<String, StringBuffer> para : map.entrySet()) {
            final String html =
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head><title>Title goes here</title></head>"
                + "<body>"
                + para.getValue()
                + "</body>"
                + "</html>";
            log.debug(para.getValue());
            try {
                final NodeList l =
                    XHTML.evaluateXPath_(
                        new ByteArrayInputStream(html.getBytes()),
                        "//xhtml:a");
                for(int i=0; i<l.getLength(); i++) {
                    log.error(l.item(i).getAttributes().getNamedItem("href"));
                }
            } catch (final RuntimeException e) {
                log.error("Error parsing page.");
            }
        }
    }

    /**
     * Accessor for the content manager.
     *
     * @return A {@link ContentManager}.
     */
    ContentManagerRemote contentManager() {
        return _registry.get(ServiceNames.CONTENT_MANAGER_REMOTE);
    }

    /**
     * Accessor for the asset manager.
     *
     * @return An {@link AssetManager}.
     */
    AssetManagerRemote assetManager() {
        return _registry.get(ServiceNames.ASSET_MANAGER_REMOTE);
    }

    /**
     * Accessor for the user manager.
     *
     * @return An {@link UserManager}.
     */
    UserManagerRemote userManager() {
        return _registry.get(ServiceNames.USER_MANAGER_REMOTE);
    }
}
