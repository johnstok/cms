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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;


/**
 * A dummy server to allow dry runs of file uploads.
 *
 * @author Civic Computing Ltd.
 */
public class DryRunServer implements Server {
    private static final Logger LOG = Logger.getLogger(DryRunServer.class);

    private final Map<UUID, ResourcePath> _folders =
        new HashMap<UUID, ResourcePath>();
    private Set<ResourcePath> _files = new HashSet<ResourcePath>();
    private UUID _rootId = UUID.randomUUID();

    private ResourcePath _rootPath;

    /**
     * Constructor.
     *
     * @param rootPath The path to the root folder.
     */
    public DryRunServer(final ResourcePath rootPath) {
        _rootPath = rootPath;
        _folders.put(_rootId, _rootPath);
    }

    /** {@inheritDoc} */
    @Override
    public UUID createFolder(final UUID parentFolder,
                             final String name,
                             final boolean publish) {
        final UUID newFolderId = UUID.randomUUID();
        final ResourcePath path =
            _folders.get(parentFolder).append(ResourceName.escape(name));
        _folders.put(newFolderId, path);
        LOG.info("Created (Folder): "+path);
        return newFolderId;
    }

    /** {@inheritDoc} */
    @Override
    public void createFile(final UUID parentFolder,
                           final File f,
                           final boolean publish) {
        final ResourcePath path =
            _folders.get(parentFolder).append(ResourceName.escape(f.getName()));
        _files.add(path);
        LOG.info("Created (File  ): "+path);
    }

    /**
     * Look up a folder's path.
     *
     * @param folderId The folder's id.
     * @return The folder's path as a string.
     */
    String getFolderPath(final UUID folderId) {
        return _folders.get(folderId).toString();
    }

    /**
     * Accessor.
     *
     * @return A set of paths created for this server.
     */
    Set<ResourcePath> getFiles() {
        return _files;
    }

    /** {@inheritDoc} */
    @Override
    public UUID getRoot() {
        return _rootId;
    }
}
