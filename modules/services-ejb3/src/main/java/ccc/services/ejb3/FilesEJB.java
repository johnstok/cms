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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.rest.Files;
import ccc.rest.RestException;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.dto.TextFileDto;
import ccc.rest.extensions.FilesExt;
import ccc.types.FilePropertyNames;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link Files} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Files.NAME)
@TransactionAttribute(REQUIRED)
@Local(FilesExt.class)
@RolesAllowed({})
public class FilesEJB
    extends
        AbstractEJB
    implements
        FilesExt {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<FileDto> getAllContentImages() {
        final List<File> list = new ArrayList<File>();
        for (final File file : getResources().images()) {
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

            final RevisionMetadata rm =
                new RevisionMetadata(
                    lastUpdated,
                    u,
                    isMajorEdit,
                    comment == null || comment.isEmpty() ? "Created." : comment
                );

            final File f =
                new CreateFileCommand(
                    getResources(), getAuditLog(), getFiles()).execute(
                        parentFolder,
                        file,
                        title,
                        description,
                        new ResourceName(resourceName),
                        rm,
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
                getResources(), getAuditLog(), getFiles()).execute(
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


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public TextFileDelta get(final UUID fileId) throws RestException {
        try {
            return mapTextFile(getResources().find(File.class, fileId));

        } catch (final CccCheckedException e) {
            throw fail(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public void update(final UUID id, final TextFileDelta file)
    throws RestException {
        byte[] bytes;
        try {
            bytes = file.getContent().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final FileDelta delta =
            new FileDelta(
                file.getMimeType(),
                null, // The command will create the data object
                bytes.length,
                Collections.singletonMap(FilePropertyNames.CHARSET, "UTF-8"));

        updateFile(
            file.getId(),
            delta,
            file.getRevisionComment(),
            file.isMajorRevision(),
            new ByteArrayInputStream(bytes));
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({CONTENT_CREATOR})
    public ResourceSummary createTextFile(final TextFileDto file)
    throws RestException {
        byte[] bytes;
        try {
            bytes = file.getContent().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final FileDelta delta =
            new FileDelta(
                file.getMimeType(),
                null, // The command will create the data object
                bytes.length,
                Collections.singletonMap(FilePropertyNames.CHARSET, "UTF-8"));

        return createFile(
            file.getParentId(),
            delta,
            file.getName(),
            new ByteArrayInputStream(bytes),
            file.getName(),
            "",
            new Date(),
            false,
            file.getRevisionComment(),
            file.isMajorRevision()
            );
    }
}
