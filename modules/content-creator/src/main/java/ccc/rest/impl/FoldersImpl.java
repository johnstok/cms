/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.impl;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.CommandFailedException;
import ccc.rest.FoldersBasic;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderNew;
import ccc.rest.dto.ResourceSummary;


/**
 * Implementation of the {@link FoldersBasic} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class FoldersImpl
    extends
        JaxrsCollection
    implements
        FoldersBasic {


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final UUID folderId) {
        return getFolderCommands().getChildren(folderId);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildrenManualOrder(
                                                        final UUID folderId) {
        return getFolderCommands().getChildrenManualOrder(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(final UUID folderId) {
        return getFolderCommands().getFolderChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public boolean nameExistsInFolder(final UUID folderId, final String name) {
        return getFolderCommands().nameExistsInFolder(folderId, name);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        return getFolderCommands().roots();
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final FolderNew folder)
    throws CommandFailedException {
        return getFolderCommands().createFolder(folder);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFolder(final UUID folderId, final FolderDelta delta)
    throws CommandFailedException {
        getFolderCommands().updateFolder(folderId, delta);
    }
}
