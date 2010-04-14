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

import static ccc.api.types.Permission.*;
import static javax.ejb.TransactionAttributeType.*;

import java.io.ByteArrayInputStream;
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

import ccc.api.Files;
import ccc.api.StreamAction;
import ccc.api.dto.DtoCollection;
import ccc.api.dto.FileDelta;
import ccc.api.dto.FileDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.TextFileDelta;
import ccc.api.dto.TextFileDto;
import ccc.api.exceptions.InternalError;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.ResourceName;
import ccc.commands.UpdateFileCommand;
import ccc.domain.File;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;


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
    public ResourceSummary createFile(final FileDto file) {
        checkPermission(FILE_CREATE);

        final User u = currentUser();

        final Date dateCreated = file.getDateCreated();

        final RevisionMetadata rm =
            new RevisionMetadata(
                dateCreated,
                u,
                file.isMajorEdit(),
                file.getComment());

        final File f =
            commands().createFileCommand(
                file.getParent(),
                new FileDelta(
                    file.getMimeType(),
                    null,
                    file.getSize(),
                    file.getProperties()),
                file.getTitle(),
                file.getDescription(),
                file.getName(),
                rm,
                file.getInputStream())
            .execute(u, dateCreated);

        if (file.isPublished()) {
            f.lock(u);
            commands().publishResource(f.getId()).execute(u, dateCreated);
            f.unlock(u);
        }

        return f.mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({FILE_UPDATE})
    public ResourceSummary updateFile(final UUID fileId, final FileDto file) {
        return
            new UpdateFileCommand(
                getRepoFactory(),
                fileId,
                new FileDelta(
                    file.getMimeType(),
                    null,
                    file.getSize(),
                    file.getProperties()),
                file.getComment(),
                file.isMajorEdit(),
                file.getInputStream())
            .execute(
                currentUser(),
                new Date())
            .mapResource();
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({FILE_UPDATE})
    public void update(final UUID id, final TextFileDelta file) {
        byte[] bytes;
        try {
            bytes = file.getContent().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        }

        final FileDto f = new FileDto(
            file.getMimeType(),
            null,
            file.getId(),
            null,
            null,
            Collections.singletonMap(FilePropertyNames.CHARSET, "UTF-8")
        );
        f.setInputStream(new ByteArrayInputStream(bytes));
        f.setSize(bytes.length);
        f.setMajorEdit(file.isMajorRevision());
        f.setComment(file.getRevisionComment());

        updateFile(f.getId(), f);
    }

    /** {@inheritDoc} */
    @Override
    @RolesAllowed({FILE_CREATE})
    public ResourceSummary createTextFile(final TextFileDto file) {
        byte[] bytes;
        try {
            bytes = file.getContent().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        }

        final FileDto f = new FileDto(
            file.getMimeType(),
            null,
            null,
            new ResourceName(file.getName()),
            file.getName(),
            Collections.singletonMap(FilePropertyNames.CHARSET, "UTF-8")
        );
        f.setDescription(file.getName());
        f.setParent(file.getParentId());
        f.setInputStream(new ByteArrayInputStream(bytes));
        f.setSize(bytes.length);
        f.setPublished(false);
        f.setMajorEdit(file.isMajorRevision());
        f.setComment(file.getRevisionComment());

        return createFile(f);
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
