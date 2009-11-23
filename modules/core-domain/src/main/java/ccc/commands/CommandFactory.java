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
package ccc.commands;

import java.io.InputStream;
import java.util.UUID;

import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.Search;
import ccc.persistence.DataRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.TemplateDto;
import ccc.types.DBC;
import ccc.types.ResourceName;


/**
 * Factory for command objects.
 *
 * @author Civic Computing Ltd.
 */
public class CommandFactory {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;
    private final DataRepository     _files;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param data  The data repository for storing binary data.
     */
    public CommandFactory(final ResourceRepository repository,
                          final LogEntryRepository audit,
                          final DataRepository data) {
        /*
         * TODO: The command factory should accept a repository factory.
         */
        DBC.require().notNull(audit);
        DBC.require().notNull(repository);
        DBC.require().notNull(data);
        _repository = repository;
        _audit = audit;
        _files = data;
    }


    /**
     * Create a 'delete resource' command.
     *
     * @param resourceId The id of the resource to delete.
     *
     * @return The corresponding command.
     */
    public Command<Void> createDeleteResourceCmd(final UUID resourceId) {
        return new DeleteResourceCommand(_repository, _audit, resourceId);
    }


    /**
     * Create a 'create template' command.
     *
     * @param template Details of the template to create.
     *
     * @return The  corresponding command.
     */
    public Command<? extends Resource> createTemplateCommand(
                                                final TemplateDto template) {
        return new CreateTemplateCommand(
            _repository,
            _audit,
            template.getParentId(),
            template.getDelta(),
            template.getTitle(),
            template.getDescription(),
            new ResourceName(template.getName()));
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param alias
     * @return
     */
    public Command<? extends Resource> createAliasCommand(final AliasDto alias) {
        return new CreateAliasCommand(
            _repository,
            _audit,
            alias.getParent(),
            alias.getTargetId(),
            alias.getName());
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param files
     * @param parentFolder
     * @param file
     * @param title
     * @param description
     * @param resourceName
     * @param rm
     * @param dataStream
     * @return
     */
    public Command<File> createFileCommand(final UUID parentFolder,
                                           final FileDelta file,
                                           final String title,
                                           final String description,
                                           final ResourceName resourceName,
                                           final RevisionMetadata rm,
                                           final InputStream dataStream) {
        return new CreateFileCommand(
            _repository,
            _audit,
            _files,
            parentFolder,
            file,
            title,
            description,
            resourceName,
            rm,
            dataStream);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param parentId
     * @param name
     * @param title
     * @return
     */
    public Command<Folder> createFolderCommand(final UUID parentId,
                                              final String name,
                                              final String title) {

        return new CreateFolderCommand(
            _repository,
            _audit,
            parentId,
            name,
            title);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param f
     * @return
     */
    public Command<Void> createRootCommand(final Folder f) {
        return new CreateRootCommand(
            _repository,
            _audit,
            f);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param parentId
     * @param delta
     * @param name
     * @param title
     * @param templateId
     * @param comment
     * @param majorChange
     * @return
     */
    public Command<Page> createPageCommand(final UUID parentId,
                                            final PageDelta delta,
                                            final ResourceName name,
                                            final String title,
                                            final UUID templateId,
                                            final String comment,
                                            final boolean majorChange) {

        return new CreatePageCommand(
            _repository,
            _audit,
            parentId,
            delta,
            name,
            title,
            templateId,
            comment,
            majorChange);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param parentId
     * @param title
     * @return
     */
    public Command<Search> createSearchCommand(final UUID parentId,
                                               final String title) {
        return new CreateSearchCommand(
            _repository,
            _audit,
            parentId,
            title);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param id
     * @return
     */
    public Command<Void> publishResource(final UUID resourceId) {
        return new PublishCommand(
            _repository,
            _audit,
            resourceId);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param resourceId
     * @return
     */
    public Command<Void> lockResourceCommand(final UUID resourceId) {
        return new LockResourceCommand(
            _repository,
            _audit,
            resourceId);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param resourceId
     * @return
     */
    public Command<Void> unlockResourceCommand(final UUID resourceId) {
        return new UnlockResourceCommand(
            _repository,
            _audit,
            resourceId);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param resourceId
     * @return
     */
    public Command<Void> unpublishResourceCommand(final UUID resourceId) {
        return new UnpublishResourceCommand(
            _repository,
            _audit,
            resourceId);
    }
}
