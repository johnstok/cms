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

import static ccc.api.types.Permission.FILE_CREATE;
import static ccc.api.types.Permission.FILE_READ;
import static ccc.api.types.Permission.FILE_UPDATE;
import static javax.ejb.TransactionAttributeType.REQUIRED;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.api.core.File;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceCriteria;
import ccc.api.synchronous.Files;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.StreamAction;
import ccc.commands.UpdateFileCommand;
import ccc.domain.FileEntity;
import ccc.domain.FolderEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;


/**
 * EJB implementation of the {@link Files} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Files.NAME)
@TransactionAttribute(REQUIRED)
@Local(Files.class)
public class FilesEJB
    extends
        AbstractEJB
    implements
        Files {


    /** {@inheritDoc} */
    @Override
    public PagedCollection<File> getPagedImages(
            final ResourceCriteria criteria,
            final int pageNo,
            final int pageSize) {
        checkPermission(FILE_READ);

        final UUID parent = criteria.getParent();
        FolderEntity f = null;
        if (parent != null) {
            f =
                getRepoFactory()
                .createResourceRepository()
                .find(FolderEntity.class, parent);
            checkRead(f);
            criteria.setParent(parent);
        }


        final List<FileEntity> list =
            getRepoFactory()
                .createResourceRepository()
                .images(criteria, f, pageNo, pageSize);
        final long c =
            getRepoFactory()
                .createResourceRepository()
                .imagesCount(criteria, f);
        return
            new PagedCollection<File>(c, File.class, FileEntity.mapFiles(list));
    }


    /** {@inheritDoc} */
    @Override
    public File create(final File file) {
        checkPermission(FILE_CREATE);

        final UserEntity u = currentUser();

        final Date dateCreated = file.getDateCreated();

        final RevisionMetadata rm =
            new RevisionMetadata(
                dateCreated,
                u,
                file.isMajorEdit(),
                file.getComment());

        final FileEntity f =
            commands().createFileCommand(
                file.getParent(),
                new File(
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

        return f.forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public File updateFile(final UUID fileId, final File file) {
        checkPermission(FILE_UPDATE);

        return
            execute(
                new UpdateFileCommand(
                    getRepoFactory(),
                    fileId,
                    new File(
                        file.getMimeType(),
                        null,
                        file.getSize(),
                        file.getProperties()),
                    file.getComment(),
                    file.isMajorEdit(),
                    file.getInputStream()))
            .forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public File update(final UUID id, final File file) {
        checkPermission(FILE_UPDATE);

        byte[] bytes;
        try {
            bytes = file.getContent().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        }

        final File f = new File(
            file.getMimeType(),
            null,
            file.getId(),
            null,
            null,
            Collections.singletonMap(FilePropertyNames.CHARSET, "UTF-8")
        );
        f.setInputStream(new ByteArrayInputStream(bytes));
        f.setSize(bytes.length);
        f.setMajorEdit(file.isMajorEdit());
        f.setComment(file.getComment());

        return updateFile(f.getId(), f);
    }

    /** {@inheritDoc} */
    @Override
    // FIXME: Removal of TextFileDto made this method a mess.
    public File createTextFile(final File file) {
        checkPermission(FILE_CREATE);

        byte[] bytes;
        try {
            bytes = file.getContent().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        }

        final File f = new File(
            file.getMimeType(),
            null,
            null,
            file.getName(),
            file.getName().toString(),
            Collections.singletonMap(FilePropertyNames.CHARSET, "UTF-8")
        );
        f.setDescription(file.getName().toString());
        f.setParent(file.getParent());
        f.setInputStream(new ByteArrayInputStream(bytes));
        f.setSize(bytes.length);
        f.setMajorEdit(file.isMajorEdit());
        f.setComment(file.getComment());
        f.setDateCreated(new Date());
        f.setDateChanged(f.getDateCreated());

        return create(f);
    }




    /* ====================================================================
     * UNSAFE METHODS.
     * ================================================================== */


    /** {@inheritDoc} */
    @Override
    public File retrieve(final UUID fileId) {
        checkPermission(FILE_READ);

        // FIXME: check file is accessible to user.
        return
            getRepoFactory()
                .createResourceRepository()
                .find(FileEntity.class, fileId)
                .mapTextFile(getRepoFactory().createDataRepository());
    }


    /** {@inheritDoc} */
    @Override
    public void retrieve(final UUID file,
                         final StreamAction action) {
        checkPermission(FILE_READ);

        final FileEntity f =
            getRepoFactory()
                .createResourceRepository()
                .find(FileEntity.class, file);
        checkRead(f);

        getRepoFactory()
            .createDataRepository()
            .retrieve(f.getData(), action);
    }


    /** {@inheritDoc} */
    @Override
    public void retrieveRevision(final UUID file,
                                 final int revision,
                                 final StreamAction action) {
        checkPermission(FILE_READ);

        final FileEntity f =
            getRepoFactory()
                .createResourceRepository()
                .find(FileEntity.class, file);
        checkRead(f);

        getRepoFactory()
            .createDataRepository()
            .retrieve(f.revision(revision).getData(), action);
    }


    /** {@inheritDoc} */
    @Override
    public void retrieveWorkingCopy(final UUID file,
                                    final StreamAction action) {
        checkPermission(FILE_READ);

        final FileEntity f =
            getRepoFactory()
                .createResourceRepository()
                .find(FileEntity.class, file);
        checkRead(f);

        getRepoFactory()
            .createDataRepository()
            .retrieve(f.getWorkingCopy().getData(), action);
    }

}
