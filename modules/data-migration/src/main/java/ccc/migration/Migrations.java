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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.Decimal;
import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.PageDelta;
import ccc.api.ParagraphDelta;
import ccc.api.ParagraphType;
import ccc.api.Queries;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.UserSummary;
import ccc.commons.Resources;
import ccc.domain.CCCException;
import ccc.domain.PredefinedResourceNames;

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
    private final Properties _paragraphTypes =
        Resources.readIntoProps("paragraph-types.properties");


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
                      final Queries queries,
                      final FileUploader fu) {
        _legacyQueries = legacyQueries;
        _queries = queries;
        _props = props;
        _commands = commands;
        _fu = fu;
    }


    /**
     * Migrate to CCC7.
     */
    public void migrate() {
        try {
            loadSupportingData();
            migrateUsers();
            migrateResources(_contentRoot.getId(), 0);
            migrateManagedFilesAndImages();
            migrateImages();
            migrateCss();
            publishRecursive(_cssFolder);
            publishRecursive(_assetsImagesFolder);
            publishRecursive(_filesFolder);
            publishRecursive(_contentImagesFolder);
        } catch (final CommandFailedException e) {
            log.error("Catastrophic failure.", e);
        }
    }


    public void createDefaultFolderStructure() throws CommandFailedException {
        _assetRoot = _commands.createRoot(PredefinedResourceNames.ASSETS);
        _contentRoot = _commands.createRoot(PredefinedResourceNames.CONTENT);

        _templateFolder =_commands.createFolder(
            _assetRoot.getId(), PredefinedResourceNames.TEMPLATES);
        _cssFolder = _commands.createFolder(
            _assetRoot.getId(), PredefinedResourceNames.CSS);
        _assetsImagesFolder = _commands.createFolder(
            _assetRoot.getId(), PredefinedResourceNames.IMAGES);

        _filesFolder = _commands.createFolder(
            _contentRoot.getId(), PredefinedResourceNames.FILES);
        _contentImagesFolder = _commands.createFolder(
            _contentRoot.getId(), PredefinedResourceNames.IMAGES);
        _commands.createSearch(_contentRoot.getId(), "search");

        // TODO: Remove. Should set 'publish' root via UI
        _commands.lock(_contentRoot.getId());
        _commands.publish(_contentRoot.getId());
        _commands.unlock(_contentRoot.getId());
        _commands.lock(_assetRoot.getId());
        _commands.publish(_assetRoot.getId());
        _commands.unlock(_assetRoot.getId());

        log.info("Created default folder structure.");
    }


    // TODO: Move under command-resourceDao?
    private void publishRecursive(final ResourceSummary resource) throws CommandFailedException {
        _commands.lock(resource.getId());
        _commands.publish(resource.getId());
        if ("FOLDER".equals(resource.getType().name())) {
            final Collection<ResourceSummary> children =
                _queries.getChildren(resource.getId());
            for (final ResourceSummary child : children) {
                publishRecursive(child);
            }
        }
        _commands.unlock(resource.getId());
    }


    private void loadSupportingData() {
        _menuItems = _legacyQueries.selectMenuItems();
    }


    private void migrateUsers() throws CommandFailedException {
        final Map<Integer, ExistingUser> mus = _legacyQueries.selectUsers();
        for (final Map.Entry<Integer, ExistingUser> mu : mus.entrySet()) {
            try {
                // TODO: improve reporting
                final ExistingUser ud = mu.getValue();

                if (null == ud._password) {
                    log.warn("User: "+ud._user.getUsername()+" has null password.");
                } else if (ud._password.equals(ud._user.getUsername())) {
                    log.warn("User: "+ud._user.getUsername()
                        +" has username as a password.");
                } else if (ud._password.length() < 6) {
                    log.warn("User: "+ud._user.getUsername()
                        +" has password with less than 6 characters.");
                }

                final UserSummary u =
                    _commands.createUser(ud._user, ud._password);
                _users.put(mu.getKey(), u);
            } catch (final RuntimeException e) {
                log.warn("Failed to create user: "+e.getMessage());
            }
        }
        log.info("Migrated users.");
    }


    private void migrateResources(final ID parentFolderId,
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
        final Map<String,FileDelta> files =_legacyQueries.selectFiles();
        for (final Map.Entry<String, FileDelta> legacyFile : files.entrySet()) {
            _fu.uploadFile(
                _filesFolder,
                legacyFile.getKey(),
                legacyFile.getValue(),
                _props.getProperty("filesSourcePath"));
        }

        final Map<String,FileDelta> images = _legacyQueries.selectImages();
        for (final Map.Entry<String, FileDelta> legacyFile : images.entrySet()) {
            _fu.uploadFile(
                _contentImagesFolder,
                legacyFile.getKey(),
                legacyFile.getValue(),
                _props.getProperty("imagesSourcePath"));
        }
    }

    private void migrateImages() {
        final Map<String, FileDelta> managedImages = _legacyQueries.selectImages();
        final String imagePath = _props.getProperty("imagesSourcePath");
        final File imageDir = new File(imagePath);
        if (!imageDir.exists()) {
            log.debug("File not found: "+imagePath);
        } else if (!imageDir.isDirectory()) {
            log.warn(imagePath+" is not a directory");
        } else {
            final File[] images = imageDir.listFiles();
            for (final File file : images) {
                final boolean managedImage = isManaged(managedImages, file);

                if (!managedImage && file.isFile()
                        && !(file.getName().startsWith("ccc")
                        || file.getName().startsWith(".")
                        || file.getName().startsWith("um")))  {

                    final FileDelta legacyFile =
                        new FileDelta(
                            file.getName(),
                            "Migrated file.",
                            null,
                            -1);
                    _fu.uploadFile(
                        _assetsImagesFolder, file.getName(), legacyFile, file);
                }
            }
        }
        log.info("Migrated non-managed images.");
    }


    private boolean isManaged(final Map<String, FileDelta> managedImages,
                              final File file) {

        boolean managedImage = false;
        for (final Map.Entry<String, FileDelta> legacyFile : managedImages.entrySet()) {
            if (file.getName().equals(legacyFile.getKey())) {
                managedImage = true;
            }
        }
        return managedImage;
    }

    private void migrateCss() {
        final String cssPath = _props.getProperty("cssSourcePath");
        final File cssDir = new File(cssPath);
        if (!cssDir.exists()) {
            log.debug("File not found: "+cssPath);
        } else if (!cssDir.isDirectory()) {
            log.warn(cssPath+" is not a directory");
        } else {
            final File[] css = cssDir.listFiles();
            for (final File file : css) {
                if (file.isFile() && file.getName().endsWith(".css"))  {
                    final FileDelta legacyFile =
                        new FileDelta(
                            file.getName(),
                            "Migrated file.",
                            null,
                            -1);
                    _fu.uploadFile(_cssFolder, file.getName(), legacyFile, file);
                }
            }
        }
        log.info("Migrated non-managed css files.");
    }

    private void migrateFolder(final ID parentFolderId,
                               final ResourceBean r) {

        try {
            // FIXME: Specify actor & date
            final ResourceSummary rs =
                _commands.createFolder(parentFolderId, r.name(), r.title());
            log.debug("Created folder: "+r.contentId());

            setTemplateForResource(r, rs);

            publish(r, rs);

            showInMainMenu(r, rs);

            setMetadata(r, rs);

            setResourceRoles(r, rs);

            migrateResources(rs.getId(), r.contentId());

        } catch (final Exception e) {
            log.warn("Error migrating folder "
                +r.contentId()+": "+e.getMessage());
        }
    }

    private void migratePage(final ID parentFolderId,
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
                                final ResourceSummary rs) throws CommandFailedException {
        if (_menuItems.contains(Integer.valueOf(r.contentId()))) {
            _commands.lock(rs.getId());
            _commands.includeInMainMenu(rs.getId(), true);
            _commands.unlock(rs.getId());
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
                            final int version) throws CommandFailedException {

        _commands.lock(rs.getId());// FIXME: Specify actor & date
        final PageDelta d = assemblePage(r, version);
//        final String userId =
//            determineActor(r.contentId(), version, "%", "MADE LIVE");
        _commands.updatePage(rs.getId(), d, "Updated.", true); // FIXME: Specify actor & date
        _commands.unlock(rs.getId());  // FIXME: Specify actor & date
        log.debug("Updated page: "+r.contentId());
    }


    private ResourceSummary createPage(final ID parentFolderId,
                                       final ResourceBean r,
                                       final List<Integer> paragraphVersions) throws CommandFailedException {

        final PageDelta delta =
            assemblePage(r, paragraphVersions.remove(0));
//      final String userId =
//          determineActor(r.contentId(), version, "%", "MADE LIVE");
        final ResourceSummary rs =
            _commands.createPage(parentFolderId, delta, r.name(), false, null);  // FIXME: Specify actor & date
        log.debug("Created page: "+r.contentId());
        return rs;
    }


    private void publish(final ResourceBean r, final ResourceSummary rs) throws CommandFailedException {
        if (r.isPublished()) {
            final ID userId =
                determineActor(r.contentId(),
                               r.legacyVersion(),
                               "Changed Status to  PUBLISHED",
                               "CHANGE STATUS");
            _commands.lock(rs.getId());  // FIXME: Specify actor & date
            if (null != userId) {
                _commands.publish(rs.getId() /*, userId, new Date()*/); // FIXME: Specify date
            } else {
                _commands.publish(rs.getId()); // FIXME: Specify actor & date
            }
            _commands.unlock(rs.getId());  // FIXME: Specify actor & date
        }
    }

    private void setMetadata(final ResourceBean r,
                             final ResourceSummary rs) throws CommandFailedException {

        final Map<String, String> metadata =
            new HashMap<String, String>();
        setStyleSheet(r, metadata);
        setFlagged(r, metadata);
        metadata.put("legacyId", ""+r.contentId());
        if (r.useInIndex() != null) {
            metadata.put("useInIndex", ""+r.useInIndex());
        }

        _commands.lock(rs.getId());
        _commands.updateMetadata(rs.getId(), metadata);
        _commands.unlock(rs.getId());
    }

    private void setResourceRoles(final ResourceBean r,
                                  final ResourceSummary rs) throws CommandFailedException {
        if (r.isSecure()) {
            log.info("Resource "+r.contentId()+" has security constraints");
            _commands.lock(rs.getId());
            _commands.changeRoles(
                rs.getId(),
                _legacyQueries.selectRolesForResource(r.contentId()));
            _commands.unlock(rs.getId());
        }
    }

    private void setStyleSheet(final ResourceBean r,
                               final Map<String, String> properties) {
        final String styleSheet = _legacyQueries.selectStyleSheet(r.contentId());
        if (styleSheet != null) {
            properties.put("bodyId", styleSheet);
        }
    }

    private void setFlagged(final ResourceBean r,
                            final Map<String, String> properties) {
        final String flagged = _legacyQueries.selectFlagged(r.contentId());
        if (flagged != null && flagged.equals("Y")) {
            properties.put("flagged", Boolean.TRUE.toString());
        }
    }

    private PageDelta assemblePage(final ResourceBean r,
                                   final int version) {
        final List<ParagraphDelta> paragraphDeltas =
            new ArrayList<ParagraphDelta>();
        final Map<String, StringBuffer> paragraphs =
            assembleParagraphs(r.contentId(), version);
        for (final Map.Entry<String, StringBuffer> para
                : paragraphs.entrySet()) {

            final String name = para.getKey();
            final ParagraphType type = getParagraphType(name);
            String textValue = null;
            String numberValue = null;

            switch (type) {
                case TEXT:
                    textValue = para.getValue().toString();
                    break;

                case NUMBER:
                    numberValue = para.getValue().toString();
                    break;

                default:
                    throw new CCCException("Unsupported paragraph type: "+type);
            }

            final ParagraphDelta pd =
                new ParagraphDelta(
                    name,
                    type,
                    null,
                    textValue,
                    null, // FIXME: Date not supported?!
                    new Decimal(numberValue));

            paragraphDeltas.add(pd);
        }

        final PageDelta delta =
            new PageDelta(
                (null==r.title())?r.name():r.title(),
                paragraphDeltas
            );

        delta.setTitle(new WordCharFixer().fix(delta.getTitle()));

        return delta;
    }


    private ParagraphType getParagraphType(final String paragraphName) {
        final String pType = _paragraphTypes.getProperty(paragraphName, "TEXT");
        return ParagraphType.valueOf(pType);
    }


    private void setTemplateForResource(final ResourceBean r,
                                 final ResourceSummary rs) throws CommandFailedException {

        final String templateName = r.displayTemplate();

        if (null == templateName) { // Resource has no template
            return;
        }

        if (!_templates.containsKey(templateName)) { // Not yet migrated
            createTemplate(templateName);
        }

        final ID templateId = _templates.get(templateName).getId();
        _commands.lock(rs.getId());  // FIXME: Specify actor & date
        _commands.updateResourceTemplate(rs.getId(), templateId);   // FIXME: Specify actor & date
        _commands.unlock(rs.getId());  // FIXME: Specify actor & date
    }


    private void createTemplate(final String templateName) throws CommandFailedException {

        final TemplateDelta t =
            new TemplateDelta(
                templateName,
                "No description.",
                "Empty template!",
                "<fields/>"
            );
        final ResourceSummary ts =
            _commands.createTemplate(_templateFolder.getId(), t, templateName);  // FIXME: Specify actor & date

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

        new LinkFixer(_props.getProperty("link-prefix", "")).extractURLs(map);
        new WordCharFixer().warn(map);

        return map;
    }


    private ID determineActor(final int id,
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

        return user.getId();
    }

}
