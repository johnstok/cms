/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.jaxrs;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.Files;
import ccc.rest.StreamAction;
import ccc.rest.dto.DtoCollection;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.dto.TextFileDto;


/**
 * Implementation of the {@link Files} API.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/files")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class FilesImpl
    extends
        JaxrsCollection
    implements
        Files {


    /** {@inheritDoc} */
    @Override
    public TextFileDelta get(final UUID fileId) {
        try {
            return getFiles().get(fileId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void update(final UUID id, final TextFileDelta file) {
        try {
            getFiles().update(id, file);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTextFile(final TextFileDto textFile) {
        try {
            return getFiles().createTextFile(textFile);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public DtoCollection<FileDto> getPagedImages(final UUID folderId,
                                                 final int pageNo,
                                                 final int pageSize) {
        try {
            return getFiles().getPagedImages(folderId, pageNo, pageSize);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
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
        try {
            return getFiles().createFile(
                parentFolder,
                file,
                resourceName,
                dataStream,
                title,
                description,
                lastUpdated,
                publish,
                comment,
                isMajorEdit);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void retrieve(final UUID file, final StreamAction action) {
        try {
            getFiles().retrieve(file, action);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void retrieveRevision(final UUID file,
                                 final int revision,
                                 final StreamAction action) {
        try {
            getFiles().retrieveRevision(file, revision, action);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void retrieveWorkingCopy(final UUID file,
                                    final StreamAction action) {
        try {
            getFiles().retrieveWorkingCopy(file, action);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateFile(final UUID fileId,
                           final FileDelta fileDelta,
                           final String comment,
                           final boolean isMajorEdit,
                           final InputStream dataStream) {
        try {
            getFiles().updateFile(
                fileId, fileDelta, comment, isMajorEdit, dataStream);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

}
