/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static ccc.types.CreatorRoles.*;
import static javax.ejb.TransactionAttributeType.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.CreateFileCommand;
import ccc.commands.PublishCommand;
import ccc.commands.UpdateFileCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.File;
import ccc.domain.User;
import ccc.persistence.QueryNames;
import ccc.rest.Files;
import ccc.rest.RestException;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link Files} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Files.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Local(Files.class)
@RolesAllowed({})
public class FilesEJB
    extends
        AbstractEJB
    implements
        Files {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<FileDto> getAllContentImages() {
        final List<File> list = new ArrayList<File>();
        for (final File file
                : getRepository().list(QueryNames.ALL_IMAGES, File.class)) {
            if (PredefinedResourceNames.CONTENT.equals(
                file.root().name().toString())) {
                list.add(file);
            }
        }
        return mapFiles(list);
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createFile(final UUID parentFolder,
                                      final FileDelta file,
                                      final String resourceName,
                                      final InputStream dataStream,
                                      final String title,
                                      final String description,
                                      final Date lastUpdated,
                                      final boolean publish,
                                      final String comment,
                                      final boolean isMajorEdit)
                                                 throws RestException {
        try {
            final User u = currentUser();

            final File f =
                new CreateFileCommand(
                    getRepository(), getAuditLog(), getFiles()).execute(
                        u,
                        lastUpdated,
                        parentFolder,
                        file,
                        title,
                        description,
                        new ResourceName(resourceName),
                        comment,
                        isMajorEdit,
                        dataStream);

            if (publish) {
                f.lock(u);
                new PublishCommand(getAuditLog()).execute(lastUpdated, u, f);
                f.unlock(u);
            }

            return mapResource(f);
        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void updateFile(final UUID fileId,
                           final FileDelta fileDelta,
                           final String comment,
                           final boolean isMajorEdit,
                           final InputStream dataStream)
                                                 throws RestException {

        try {
            new UpdateFileCommand(
                getRepository(), getAuditLog(), getFiles()).execute(
                    currentUser(),
                    new Date(),
                    UUID.fromString(fileId.toString()),
                    fileDelta,
                    comment,
                    isMajorEdit,
                    dataStream);

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }
}
