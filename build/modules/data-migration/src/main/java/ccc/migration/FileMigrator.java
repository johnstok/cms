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
 * TODO: Add a description for this type.
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
     * @param fu
     * @param legacyQueries
     * @param props
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

    void migrateManagedFilesAndImages(
                                  final ResourceSummary _filesFolder,
                                  final ResourceSummary _contentImagesFolder) {
        final Map<String,LegacyFile> files =_legacyQueries.selectFiles();
        for (final Map.Entry<String, LegacyFile> legacyFile :
             files.entrySet()) {
            _fu.uploadFile(
                UUID.fromString(_filesFolder.getId().toString()),
                legacyFile.getKey(),
                legacyFile.getValue()._title,
                legacyFile.getValue()._description,
                _filesSourcePath);
        }

        final Map<String,LegacyFile> images = _legacyQueries.selectImages();
        for (final Map.Entry<String, LegacyFile> legacyFile :
             images.entrySet()) {
            _fu.uploadFile(
                UUID.fromString(_contentImagesFolder.getId().toString()),
                legacyFile.getKey(),
                legacyFile.getValue()._title,
                legacyFile.getValue()._description,
                _imagesSourcePath);
        }
    }

    void migrateImages(final ResourceSummary _assetsImagesFolder) {
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
                        UUID.fromString(_assetsImagesFolder.getId().toString()),
                        file.getName(),
                        file.getName(),
                        "Migrated file.",
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
        for (final Map.Entry<String, LegacyFile> legacyFile :
             managedImages.entrySet()) {
            if (file.getName().equals(legacyFile.getKey())) {
                managedImage = true;
            }
        }
        return managedImage;
    }

    void migrateCss(final ResourceSummary _cssFolder) {
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
                        UUID.fromString(_cssFolder.getId().toString()),
                        file.getName(),
                        file.getName(),
                        "",
                        file,
                        false);
                }
            }
        }
        log.info("Migrated non-managed css files.");
    }
}
