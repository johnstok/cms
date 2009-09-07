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

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.commands.CreateFileCommand;
import ccc.commands.PublishCommand;
import ccc.commands.UpdateFileCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.File;
import ccc.domain.User;
import ccc.persistence.FileRepositoryImpl;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.QueryNames;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.FsCoreData;
import ccc.persistence.jpa.JpaRepository;
import ccc.rest.CommandFailedException;
import ccc.rest.Commands;
import ccc.rest.Files;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;


/**
 * EJB implementation of the {@link Commands} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=Files.NAME)
@TransactionAttribute(REQUIRES_NEW)
@Local(Files.class)
@RolesAllowed({})
public class FilesEJB
    extends
        BaseCommands
    implements
        Files {

    @PersistenceContext private EntityManager _em;
    @javax.annotation.Resource private EJBContext _context;

    private LogEntryRepository _audit;
    private FileRepositoryImpl _dm;


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public FileDelta fileDelta(final UUID fileId) {
        return
            deltaFile(_resources.find(File.class, fileId));
    }


    /** {@inheritDoc} */
    @Override
    @RolesAllowed({ADMINISTRATOR, CONTENT_CREATOR, SITE_BUILDER})
    public Collection<FileDto> getAllContentImages() {
        final List<File> list = new ArrayList<File>();
        for (final File file : _bdao.list(QueryNames.ALL_IMAGES, File.class)) {
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
                                                 throws CommandFailedException {
        try {
            final User u = loggedInUser();

            final File f =
                new CreateFileCommand(_bdao, _audit, _dm).execute(
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
                new PublishCommand(_audit).execute(lastUpdated, u, f);
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
                                                 throws CommandFailedException {

        try {
            new UpdateFileCommand(_bdao, _audit, _dm).execute(
                loggedInUser(),
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


    /* ==============
     * Helper methods
     * ============== */
    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new JpaRepository(_em);
        _audit = new LogEntryRepositoryImpl(_bdao);
        _users = new UserRepositoryImpl(_bdao);
        _dm = new FileRepositoryImpl(new FsCoreData(), _bdao);
    }

    private CommandFailedException fail(final CccCheckedException e) {
        return fail(_context, e);
    }

    private User loggedInUser() {
        return loggedInUser(_context);
    }
}
