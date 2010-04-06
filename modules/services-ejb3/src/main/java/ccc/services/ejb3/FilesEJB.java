/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.services.ejb3;

import static ccc.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.commands.UpdateFileCommand;
import ccc.domain.File;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.rest.Files;
import ccc.rest.StreamAction;
import ccc.rest.dto.DtoCollection;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.dto.TextFileDto;
import ccc.types.FilePropertyNames;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link Files} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Files.NAME)
@TransactionAttribute(REQUIRED)
@Local(Files.class)
@RolesAllowed({})
public class FilesEJB
    extends
        AbstractEJB
    implements
        Files {


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({FILE_READ})
    public DtoCollection<FileDto> getPagedImages(
            final UUID folderId, final int pageNo, final int pageSize) {
        final List<File> list =
            getRepoFactory()
                .createResourceRepository()
                .images(folderId, pageNo, pageSize);
        final long c =
            getRepoFactory()
                .createResourceRepository()
                .imagesCount(folderId);
        return new DtoCollection<FileDto>(c, File.mapFiles(list));
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public ResourceSummary createFile(final UUID parentFolder,
                                      final FileDelta file,
                                      final String resourceName,
                                      final InputStream dataStream,
                                      final String title,
                                      final String description,
                                      final Date lastUpdated,
                                      final boolean publish,
                                      final String comment,
                                      final boolean isMajorEdit) {
        checkPermission(FILE_CREATE);

        final User u = currentUser();

        final RevisionMetadata rm =
            new RevisionMetadata(
                lastUpdated,
                u,
                isMajorEdit,
                comment == null || comment.isEmpty() ? "Created." : comment
            );

        final File f =
            commands().createFileCommand(
                parentFolder,
                file,
                title,
                description,
                new ResourceName(resourceName),
                rm,
                dataStream)
            .execute(u, lastUpdated);

        if (publish) {
            f.lock(u);
            commands().publishResource(f.getId()).execute(u, lastUpdated);
            f.unlock(u);
        }

        return f.mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({FILE_UPDATE})
    public void updateFile(final UUID fileId,
                           final FileDelta fileDelta,
                           final String comment,
                           final boolean isMajorEdit,
                           final InputStream dataStream) {
        new UpdateFileCommand(
            getRepoFactory(),
            UUID.fromString(fileId.toString()),
            fileDelta,
            comment,
            isMajorEdit,
            dataStream)
        .execute(
            currentUser(),
            new Date());
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({FILE_UPDATE})
    public void update(final UUID id, final TextFileDelta file) {
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
    @RolesAllowed({FILE_CREATE})
    public ResourceSummary createTextFile(final TextFileDto file) {
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




    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public TextFileDelta get(final UUID fileId) {
        checkPermission(FILE_READ);

        // FIXME: check file is accessible to user.
        return
            getRepoFactory()
                .createResourceRepository()
                .find(File.class, fileId)
                .mapTextFile(getRepoFactory().createDataRepository());
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void retrieve(final UUID file,
                         final StreamAction action) {
        checkPermission(FILE_READ);

        final File f =
            getRepoFactory()
                .createResourceRepository()
                .find(File.class, file);
        checkRead(f);

        getRepoFactory()
            .createDataRepository()
            .retrieve(f.getData(), action);
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void retrieveRevision(final UUID file,
                                 final int revision,
                                 final StreamAction action) {
        checkPermission(FILE_READ);

        final File f =
            getRepoFactory()
                .createResourceRepository()
                .find(File.class, file);
        checkRead(f);

        getRepoFactory()
            .createDataRepository()
            .retrieve(f.revision(revision).getData(), action);
    }


    /** {@inheritDoc} */
    @Override
    @PermitAll
    public void retrieveWorkingCopy(final UUID file,
                                    final StreamAction action) {
        checkPermission(FILE_READ);

        final File f =
            getRepoFactory()
                .createResourceRepository()
                .find(File.class, file);
        checkRead(f);

        getRepoFactory()
            .createDataRepository()
            .retrieve(f.getWorkingCopy().getData(), action);
    }

}
