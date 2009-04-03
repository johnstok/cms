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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import ccc.domain.PredefinedResourceNames;
import ccc.services.api.Commands;
import ccc.services.api.FileDelta;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.Queries;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

/**
 * Data migration from CCC6 to CCC7.
 *
 * @author Civic Computing Ltd
 */
public class Migrations {
    private static final boolean DEBUG = true;
    private static Logger log = Logger.getLogger(Migrations.class);

    private final Map<String, ResourceSummary> _templates =
        new HashMap<String, ResourceSummary>();

    private final Map<Integer, UserSummary> _users =
        new HashMap<Integer, UserSummary>();

    private ResourceSummary _assetRoot;
    private ResourceSummary _contentRoot;
    private ResourceSummary _templateFolder;
    private ResourceSummary _filesFolder;
    private ResourceSummary _contentImagesFolder;
    private ResourceSummary _assetsImagesFolder;
    private ResourceSummary _cssFolder;
    private Set<Integer>    _menuItems;

    private final LegacyDBQueries _legacyQueries;
    private final Properties _props;
    private final Commands _commands;
    private final Queries _queries;
    private final FileUploader _fu;


    /**
     * Constructor.
     *
     * @param legacyQueries Queries
     * @param props Properties of the migration
     * @param commands The available commands for CCC7.
     * @param queries The available queries for CCC7.
     */
    public Migrations(final LegacyDBQueries legacyQueries,
                      final Properties props,
                      final Commands commands,
                      final Queries queries) {
        _legacyQueries = legacyQueries;
        _queries = queries;
        _props = props;
        _commands = commands;
        _fu =
            new FileUploader(
                _props.getProperty("targetUploadURL"),
                _props.getProperty("targetApplicationURL"));
    }


    /**
     * Migrate to CCC7.
     */
    public void migrate() {
        loadSupportingData();
        migrateUsers();
        migrateResources(_contentRoot._id, 0);
        migrateManagedFilesAndImages();
        migrateImages();
        migrateCss();
        publishRecursive(_cssFolder);
        publishRecursive(_assetsImagesFolder);
        publishRecursive(_filesFolder);
        publishRecursive(_contentImagesFolder);
    }


    public void createDefaultFolderStructure() {
        _assetRoot = _commands.createRoot(PredefinedResourceNames.ASSETS);
        _contentRoot = _commands.createRoot(PredefinedResourceNames.CONTENT);

        _templateFolder =_commands.createFolder(_assetRoot._id,
            PredefinedResourceNames.TEMPLATES);
        _cssFolder = _commands.createFolder(_assetRoot._id,
            PredefinedResourceNames.CSS);
        _assetsImagesFolder = _commands.createFolder(_assetRoot._id,
            PredefinedResourceNames.IMAGES);

        _filesFolder = _commands.createFolder(_contentRoot._id,
            PredefinedResourceNames.FILES);
        _contentImagesFolder = _commands.createFolder(_contentRoot._id,
            PredefinedResourceNames.IMAGES);
        _commands.createSearch(_filesFolder._id, "SiteSearch");

        // TODO: Remove. Should set 'publish' root via UI
        _commands.lock(_contentRoot._id);
        _commands.publish(_contentRoot._id);
        _commands.unlock(_contentRoot._id);
        _commands.lock(_assetRoot._id);
        _commands.publish(_assetRoot._id);
        _commands.unlock(_assetRoot._id);

        log.info("Created default folder structure.");
    }


    // TODO: Move under command-resourceDao?
    private void publishRecursive(final ResourceSummary resource) {
        _commands.lock(resource._id);
        _commands.publish(resource._id);
        if ("FOLDER".equals(resource._type)) {
            final Collection<ResourceSummary> children =
                _queries.getChildren(resource._id);
            for (final ResourceSummary child : children) {
                publishRecursive(child);
            }
        }
        _commands.unlock(resource._id);
    }


    private void loadSupportingData() {
        _menuItems = _legacyQueries.selectMenuItems();
    }


    private void migrateUsers() {
        final Map<Integer, UserDelta> mus = _legacyQueries.selectUsers();
        for (final Map.Entry<Integer, UserDelta> mu : mus.entrySet()) {
            try {
                final UserSummary u = _commands.createUser(mu.getValue());
                _users.put(mu.getKey(), u);
            } catch (final RuntimeException e) {
                log.warn("Failed to create user: "+e.getMessage());
            }
        }
        log.info("Migrated users.");
    }


    private void migrateResources(final String parentFolderId,
                                  final int parent) {

        final List<ResourceBean> resources = _legacyQueries.selectResources(parent);

        for (final ResourceBean r : resources) {
            if (r.name() == null || r.name().trim().equals("")) {
                log.warn("Ignoring resource with missing name: "+r.contentId());
                continue;
            }

            if (r.type().equals("FOLDER")) {
                migrateFolder(parentFolderId, r);
            } else if (r.type().equals("PAGE")) {
                migratePage(parentFolderId, r);
            } else {
                log.warn("Ignoring unsupported type "
                    +r.type()+" for resource "+r.contentId());
            }
        }
    }


    private void migrateManagedFilesAndImages() {
        final List<FileDelta> files =_legacyQueries.selectFiles();
        for (final FileDelta legacyFile : files) {
            _fu.uploadFile(_filesFolder, legacyFile,
                _props.getProperty("filesSourcePath"));
        }

        final List<FileDelta> images = _legacyQueries.selectImages();
        for (final FileDelta legacyFile : images) {
            _fu.uploadFile(_contentImagesFolder, legacyFile,
                _props.getProperty("imagesSourcePath"));
        }
    }

    private void migrateImages() {
        final String imagePath = _props.getProperty("imagesSourcePath");
        final File imageDir = new File(imagePath);
        if (!imageDir.exists()) {
            log.debug("File not found: "+imagePath);
        } else if (!imageDir.isDirectory()) {
            log.warn(imagePath+" is not a directory");
        } else {
            final File[] images = imageDir.listFiles();
            for (final File file : images) {
                boolean managedImage = false;
                final List<FileDelta> managedImages = _legacyQueries.selectImages();
                for (final FileDelta legacyFile : managedImages) {
                    if (file.getName().equals(legacyFile._name)) {
                        managedImage = true;
                    }
                }

                if (!managedImage && file.isFile()
                        && !(file.getName().startsWith("ccc")
                        || file.getName().startsWith(".")
                        || file.getName().startsWith("um")))  {

                    final FileDelta legacyFile = new FileDelta();
                    legacyFile._name = file.getName();
                    legacyFile._description = "migrated file";
                    _fu.uploadFile(_assetsImagesFolder, legacyFile, file);
                }
            }
        }
        log.info("Migrated non-managed images.");
    }

    private void migrateCss() {
        final String cssPath = _props.getProperty("cssSourcePath");
        final File cssDir = new File(cssPath);
        if (!cssDir.exists()) {
            log.debug("File not found: "+cssPath);
        } else if (!cssDir.isDirectory()) {
            log.warn(cssPath+" is not a directory");
        } else {
            final File[] images = cssDir.listFiles();
            for (final File file : images) {
                if (file.isFile() && file.getName().endsWith(".css"))  {
                    final FileDelta legacyFile = new FileDelta();
                    legacyFile._name = file.getName();
                    legacyFile._description = "migrated file";
                    _fu.uploadFile(_cssFolder, legacyFile, file);
                }
            }
        }
        log.info("Migrated non-managed css files.");
    }

    private void migrateFolder(final String parentFolderId,
                               final ResourceBean r) {

        try {
            // FIXME: Specify actor & date
            // FIXME: Set a title for folders?
            final ResourceSummary rs =
                _commands.createFolder(parentFolderId, r.name());
            log.debug("Created folder: "+r.contentId());

            setTemplateForResource(r, rs);

            publish(r, rs);

            showInMainMenu(r, rs);

            setMetadata(r, rs);

            setResourceRoles(r, rs);

            migrateResources(rs._id, r.contentId());

        } catch (final Exception e) {
            log.warn("Error migrating folder "
                +r.contentId()+": "+e.getMessage());
        }
    }

    private void migratePage(final String parentFolderId,
                             final ResourceBean r) {

        try {
            // Query the versions of a page
            final List<Integer> paragraphVersions = determinePageVersions(r);

            // Create the page
            final ResourceSummary rs =
                createPage(parentFolderId, r, paragraphVersions);

            // Apply all updates
            if (DEBUG) {
                for (final Integer version : paragraphVersions) {
                    updatePage(r, rs, version);
                }
            }

            // Set the template
            setTemplateForResource(r, rs);

            // Publish, if necessary
            publish(r, rs);

            showInMainMenu(r, rs);

            setMetadata(r, rs);

            setResourceRoles(r, rs);

            log.info("Migrated page "+r.contentId());

        } catch (final Exception e) {
            log.warn("Error migrating page "
                +r.contentId()+": "+e.getMessage());
        }
    }


    private void showInMainMenu(final ResourceBean r,
                                final ResourceSummary rs) {
        if (_menuItems.contains(Integer.valueOf(r.contentId()))) {
            _commands.lock(rs._id);
            _commands.includeInMainMenu(rs._id, true);
            _commands.unlock(rs._id);
        }
    }


    private List<Integer> determinePageVersions(final ResourceBean r) {

        if (DEBUG) {
            return new ArrayList<Integer>(){{ add(Integer.valueOf(0)); }};
        }

        final List<Integer> paragraphVersions =
            _legacyQueries.selectParagraphVersions(r.contentId());
        log.debug("Page versions available: "+paragraphVersions);

        if (-1 == paragraphVersions.get(0)) { // Discard working version
            paragraphVersions.remove(0);
            log.debug("Ignoring working copy for page: "+r.contentId());
        }

        if (0 != paragraphVersions.get(0)) { // Do version 0 last
            throw new MigrationException(
                "No 'current version' for page "+r.contentId());
        }

        paragraphVersions.remove(0);
        paragraphVersions.add(Integer.valueOf(0));

        log.debug(
            "Page "+r.contentId()
            +" contains "+paragraphVersions.size()+" versions.");
        return paragraphVersions;
    }


    private void updatePage(final ResourceBean r,
                            final ResourceSummary rs,
                            final Integer version) {

        _commands.lock(rs._id);// FIXME: Specify actor & date
        final PageDelta d = assemblePage(r, rs._id, version);
//        final String userId =
//            determineActor(r.contentId(), version, "%", "MADE LIVE");
        _commands.updatePage(d, "Updated.", true); // FIXME: Specify actor & date
        _commands.unlock(rs._id);  // FIXME: Specify actor & date
        log.debug("Updated page: "+r.contentId());
    }


    private ResourceSummary createPage(final String parentFolderId,
                                       final ResourceBean r,
                                       final List<Integer> paragraphVersions) {

        final PageDelta delta =
            assemblePage(r, null, paragraphVersions.remove(0));
//      final String userId =
//          determineActor(r.contentId(), version, "%", "MADE LIVE");
        final ResourceSummary rs =
            _commands.createPage(parentFolderId, delta, null);  // FIXME: Specify actor & date
        log.debug("Created page: "+r.contentId());
        return rs;
    }


    private void publish(final ResourceBean r, final ResourceSummary rs) {
        if (r.isPublished()) {
            final String userId =
                determineActor(r.contentId(),
                               r.legacyVersion(),
                               "Changed Status to  PUBLISHED",
                               "CHANGE STATUS");
            _commands.lock(rs._id);  // FIXME: Specify actor & date
            if (null != userId) {
                _commands.publish(rs._id, userId, new Date()); // FIXME: Specify date
            } else {
                _commands.publish(rs._id); // FIXME: Specify actor & date
            }
            _commands.unlock(rs._id);  // FIXME: Specify actor & date
        }
    }

    private void setMetadata(final ResourceBean r,
                             final ResourceSummary rs) {

        final Map<String, String> metadata =
            new HashMap<String, String>();
        setStyleSheet(r, metadata);
        metadata.put("legacyId", ""+r.contentId());
        if (r.useInIndex() != null) {
            metadata.put("useInIndex", ""+r.useInIndex());
        }

        _commands.lock(rs._id);
        _commands.updateMetadata(rs._id, metadata);
        _commands.unlock(rs._id);
    }

    private void setResourceRoles(final ResourceBean r,
                                  final ResourceSummary rs) {
        if (r.isSecure()) {
            log.info("Resource "+r.contentId()+" has security constraints");
            _commands.lock(rs._id);
            _commands.changeRoles(
                rs._id,
                _legacyQueries.selectRolesForResource(r.contentId()));
            _commands.unlock(rs._id);
        }
    }

    private void setStyleSheet(final ResourceBean r,
                               final Map<String, String> properties) {
        final String styleSheet = _legacyQueries.selectStyleSheet(r.contentId());
        if (styleSheet != null) {
            properties.put("bodyId", styleSheet);
        }
    }

    private PageDelta assemblePage(final ResourceBean r,
                                         final String id,
                                         final int version) {

        final PageDelta delta = new PageDelta();

        delta._name = r.name();
        delta._title = (null==r.title())?r.name():r.title();
        delta._id = id;

        delta._paragraphs.clear();
        final Map<String, StringBuffer> paragraphs =
            assembleParagraphs(r.contentId(), version);
        for (final Map.Entry<String, StringBuffer> para
                : paragraphs.entrySet()) {
            final ParagraphDelta pd = new ParagraphDelta();
            pd._name = para.getKey();
            pd._textValue = para.getValue().toString();
            pd._type = "TEXT";
            delta._paragraphs.add(pd);
        }
        return delta;
    }


    private void setTemplateForResource(final ResourceBean r,
                                 final ResourceSummary rs) {

        final String templateName = r.displayTemplate();

        if (null == templateName) { // Resource has no template
            return;
        }

        if (!_templates.containsKey(templateName)) { // Not yet migrated
            createTemplate(templateName);
        }

        final String templateId = _templates.get(templateName)._id;
        _commands.lock(rs._id);  // FIXME: Specify actor & date
        _commands.updateResourceTemplate(rs._id, templateId);   // FIXME: Specify actor & date
        _commands.unlock(rs._id);  // FIXME: Specify actor & date
    }


    private void createTemplate(final String templateName) {

        final TemplateDelta t = new TemplateDelta();
        t._body = "Empty template!";
        t._definition = "<fields/>";
        t._description = "No description.";
        t._name = templateName;
        t._title = templateName;

        final ResourceSummary ts =
            _commands.createTemplate(_templateFolder._id, t);  // FIXME: Specify actor & date

        _templates.put(templateName, ts);
    }


    private Map<String, StringBuffer> assembleParagraphs(final int pageId,
                                                         final int version) {
        log.debug("Assembling paragraphs for "+pageId+" v."+version);

        final Map<String, StringBuffer> map =
            new HashMap<String, StringBuffer>();
        final List<ParagraphBean> paragraphs =
            _legacyQueries.selectParagraphs(pageId, version);

        for (final ParagraphBean p : paragraphs) {
            if (p.text() == null) { // ignore empty/null texts
                log.debug("Ignoring empty part for paragraph "+p.key());

            } else if (map.containsKey(p.key())) { // merge
                final StringBuffer sb = map.get(p.key());
                map.put(p.key(), sb.append(p.text()));
                log.debug("Appended to paragraph "+p.key());

            } else { // new item
                map.put(p.key(), new StringBuffer(p.text()));
                log.debug("Created paragraph "+p.key());
            }
        }
        log.debug("Assembly done.");

//        new LinkFixer(_props.getProperty("link-prefix", "")).extractURLs(map);

        return map;
    }


    private String determineActor(final int id,
                                  final int version,
                                  final String comment,
                                  final String action) {

        final Integer userId =
            _legacyQueries.selectUserFromLog(id, version, action, comment);

        log.debug("Actor for "+action+" on "+id+" v."+version+" is "+userId);

        final UserSummary user =_users.get(userId);

        if (null==user) {
            log.warn("User missing: "+userId);
            // TODO: select 'unknown' user.
            return null;
        }

        return user._id;
    }

}
