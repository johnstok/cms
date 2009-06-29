/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.api.ResourceSummary;


/**
 * Migrate files, images and CSS from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class FileMigrator {
    private static Logger log = Logger.getLogger(Migrations.class);


    private final FileUploader _fu;
    private final LegacyDBQueries _legacyQueries;
    private final String _filesSourcePath;
    private final String _imagesSourcePath;
    private final String _cssSourcePath;


    /**
     * Constructor.
     *
     * @param fu The file up-loader to use.
     * @param legacyQueries The query API to use.
     * @param filesSourcePath The local path for non-image files.
     * @param imagesSourcePath The local path for image files.
     * @param cssSourcePath The local path for css files.
     */
    public FileMigrator(final FileUploader fu,
                        final LegacyDBQueries legacyQueries,
                        final String filesSourcePath,
                        final String imagesSourcePath,
                        final String cssSourcePath) {
        _fu = fu;
        _legacyQueries = legacyQueries;
        _filesSourcePath = filesSourcePath;
        _imagesSourcePath = imagesSourcePath;
        _cssSourcePath = cssSourcePath;
    }

    /**
     * Migrate client files to the new system.
     *
     * @param filesFolder The folder that non-images should be uploaded to.
     * @param contentImagesFolder The folder that images should be uploaded to.
     */
    void migrateManagedFilesAndImages(
                                  final ResourceSummary filesFolder,
                                  final ResourceSummary contentImagesFolder) {
        final Map<String, LegacyFile> files =_legacyQueries.selectFiles();
        for (final Map.Entry<String, LegacyFile> legacyFile
            : files.entrySet()) {
            _fu.uploadFile(
                UUID.fromString(filesFolder.getId().toString()),
                legacyFile.getKey(),
                legacyFile.getValue().getTitle(),
                legacyFile.getValue().getDescription(),
                legacyFile.getValue().getLastUpdate(),
                _filesSourcePath);
        }

        final Map<String, LegacyFile> images = _legacyQueries.selectImages();
        for (final Map.Entry<String, LegacyFile> legacyFile
            : images.entrySet()) {
            _fu.uploadFile(
                UUID.fromString(contentImagesFolder.getId().toString()),
                legacyFile.getKey(),
                legacyFile.getValue().getTitle(),
                legacyFile.getValue().getDescription(),
                legacyFile.getValue().getLastUpdate(),
                _imagesSourcePath);
        }
    }

    /**
     * Migrate asset images to the new system.
     *
     * @param assetsImagesFolder The folder that files should be uploaded to.
     */
    void migrateImages(final ResourceSummary assetsImagesFolder) {
        final Map<String, LegacyFile> managedImages =
            _legacyQueries.selectImages();
        final File imageDir = new File(_imagesSourcePath);
        if (!imageDir.exists()) {
            log.debug("File not found: "+_imagesSourcePath);
        } else if (!imageDir.isDirectory()) {
            log.warn(_imagesSourcePath+" is not a directory");
        } else {
            final File[] images = imageDir.listFiles();
            for (final File file : images) {
                final boolean managedImage = isManaged(managedImages, file);

                if (!managedImage && file.isFile()
                        && !(file.getName().startsWith("ccc")
                        || file.getName().startsWith(".")
                        || file.getName().startsWith("um")))  {
                    _fu.uploadFile(
                        UUID.fromString(assetsImagesFolder.getId().toString()),
                        file.getName(),
                        file.getName(),
                        "Migrated file.",
                        null,
                        file,
                        false);
                }
            }
        }
        log.info("Migrated non-managed images.");
    }


    private boolean isManaged(final Map<String, LegacyFile> managedImages,
                              final File file) {
        boolean managedImage = false;
        for (final Map.Entry<String, LegacyFile> legacyFile
            : managedImages.entrySet()) {
            if (file.getName().equals(legacyFile.getKey())) {
                managedImage = true;
            }
        }
        return managedImage;
    }

    /**
     * Migrate css files to the new system.
     *
     * @param cssFolder The folder that css files should be uploaded to.
     */
    void migrateCss(final ResourceSummary cssFolder) {
        final String cssPath = _cssSourcePath;
        final File cssDir = new File(cssPath);
        if (!cssDir.exists()) {
            log.debug("File not found: "+cssPath);
        } else if (!cssDir.isDirectory()) {
            log.warn(cssPath+" is not a directory");
        } else {
            final File[] css = cssDir.listFiles();
            for (final File file : css) {
                if (file.isFile() && file.getName().endsWith(".css"))  {
                    _fu.uploadFile(
                        UUID.fromString(cssFolder.getId().toString()),
                        file.getName(),
                        file.getName(),
                        "",
                        null,
                        file,
                        false);
                }
            }
        }
        log.info("Migrated non-managed css files.");
    }
}
