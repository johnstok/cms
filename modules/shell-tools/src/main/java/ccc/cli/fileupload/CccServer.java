/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.cli.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.api.core.Files;
import ccc.api.core.Folders;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Resources;
import ccc.api.exceptions.ResourceExistsException;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.cli.FileUpload;


/**
 * Implementation of the {@link Server} interface - calls the CCC server.
 *
 * @author Civic Computing Ltd.
 */
public class CccServer implements Server {
    private static final Logger LOG = Logger.getLogger(FileUpload.class);

    private final ResourcePath _rootPath;
    private final Files _uploader;
    private final Resources _resources;
    private final Folders _folders;



    /**
     * Constructor.
     *
     * @param rootPath The absolute path to the folder where files will be
     *  uploaded.
     * @param files     The files API.
     * @param folders   The folders API.
     * @param resources The resources API.
     */
    public CccServer(final ResourcePath rootPath,
                     final Files files,
                     final Folders folders,
                     final Resources resources) {
        _rootPath = rootPath;
        _uploader = files;
        _folders = folders;
        _resources = resources;
    }


    /** {@inheritDoc} */
    @Override
    public void createFile(final UUID parentFolder,
                           final File localFile,
                           final boolean publish) {

        try {
            final ccc.api.core.File f = new ccc.api.core.File(
                new MimeType("application", "octet-stream"),
                null,
                null,
                ResourceName.escape(localFile.getName()),
                localFile.getName(),
                new HashMap<String, String>()
            );

            f.setDescription(localFile.getName());
            f.setParent(parentFolder);
            f.setInputStream(new FileInputStream(localFile));
            f.setSize(localFile.length());
            f.setPublished(publish);

            _uploader.create(f);
        } catch (final FileNotFoundException e) {
            LOG.warn("Failed to upload file: "+localFile.getAbsolutePath(), e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public UUID createFolder(final UUID parentFolder,
                             final String name,
                             final boolean publish) {

        try {
            final ResourceSummary rs =
                _folders.createFolder(
                    UUID.fromString(parentFolder.toString()),
                    name,
                    name,
                    publish);
            return UUID.fromString(rs.getId().toString());
        } catch (final ResourceExistsException e) {
            LOG.warn("Folder already exists: "+name);
            return e.getResourceId();
        }
    }


    /** {@inheritDoc} */
    @Override
    public UUID getRoot() {
        final ResourceSummary rs =
            _resources.resourceForPath(_rootPath.toString());
        return UUID.fromString(rs.getId().toString());
    }
}
