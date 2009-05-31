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
import java.util.Properties;
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
    private final Properties _props;



    /**
     * Constructor.
     *
     * @param fu
     * @param legacyQueries
     * @param props
     */
    public FileMigrator(final FileUploader fu,
                        final LegacyDBQueries legacyQueries,
                        final Properties props) {
        _fu = fu;
        _legacyQueries = legacyQueries;
        _props = props;
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
                _props.getProperty("filesSourcePath"));
        }

        final Map<String,LegacyFile> images = _legacyQueries.selectImages();
        for (final Map.Entry<String, LegacyFile> legacyFile :
             images.entrySet()) {
            _fu.uploadFile(
                UUID.fromString(_contentImagesFolder.getId().toString()),
                legacyFile.getKey(),
                legacyFile.getValue()._title,
                legacyFile.getValue()._description,
                _props.getProperty("imagesSourcePath"));
        }
    }

    void migrateImages(final ResourceSummary _assetsImagesFolder) {
        final Map<String, LegacyFile> managedImages =
            _legacyQueries.selectImages();
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
