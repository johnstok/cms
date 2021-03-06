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
package ccc.commands;

import java.io.InputStream;
import java.util.UUID;

import ccc.api.core.Alias;
import ccc.api.core.File;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.Template;
import ccc.api.types.DBC;
import ccc.api.types.ResourceName;
import ccc.domain.FileEntity;
import ccc.domain.FolderEntity;
import ccc.domain.PageEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.TemplateEntity;
import ccc.messaging.Producer;
import ccc.persistence.DataRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;


/**
 * Factory for command objects.
 *
 * @author Civic Computing Ltd.
 */
public class CommandFactory {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;
    private final DataRepository     _files;
    private final Producer           _producer;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param data  The data repository for storing binary data.
     * @param producer The message producer to broadcast messages.
     */
    public CommandFactory(final ResourceRepository repository,
                          final LogEntryRepository audit,
                          final DataRepository data,
                          final Producer producer) {
        /*
         * TODO: The command factory should accept a repository factory.
         */
        DBC.require().notNull(audit);
        DBC.require().notNull(repository);
        DBC.require().notNull(data);
        _repository = repository;
        _audit = audit;
        _files = data;
        _producer = DBC.require().notNull(producer);
    }


    /**
     * Create a 'delete resource' command.
     *
     * @param resourceId The id of the resource to delete.
     *
     * @return The corresponding command.
     */
    public Command<Void> createDeleteResourceCmd(final UUID resourceId) {
        return
            new DeleteResourceCommand(
                _repository, _audit, _producer, resourceId);
    }


    /**
     * Create a 'create template' command.
     *
     * @param template Details of the template to create.
     *
     * @return The corresponding command.
     */
    public Command<TemplateEntity> createTemplateCommand(
                                                final Template template) {
        return new CreateTemplateCommand(
            _repository,
            _audit,
            template);
    }


    /**
     * Create a 'create alias' command.
     *
     * @param alias Details of the alias to create.
     *
     * @return The corresponding command.
     */
    public Command<Alias> createAliasCommand(final Alias alias) {
        return new CreateAliasCommand(
            _repository,
            _audit,
            alias.getParent(),
            alias.getTargetId(),
            alias.getName());
    }


    /**
     * Create a 'create file' command.
     *
     * @param parentFolder The folder in which the file will be created.
     * @param file The details of the file.
     * @param title The file's title.
     * @param description The file's description.
     * @param resourceName The file's name.
     * @param rm Details of the revision.
     * @param dataStream The stream containing the contents of the file.
     *
     * @return The corresponding command.
     */
    public Command<FileEntity> createFileCommand(final UUID parentFolder,
                                           final File file,
                                           final String title,
                                           final String description,
                                           final ResourceName resourceName,
                                           final RevisionMetadata rm,
                                           final InputStream dataStream) {
        return new CreateFileCommand(
            _repository,
            _audit,
            _files,
            _producer,
            parentFolder,
            file,
            title,
            description,
            resourceName,
            rm,
            dataStream);
    }


    /**
     * Create a 'create folder' command.
     *
     * @param name The name of the folder.
     * @param parentId The folder's parent.
     * @param title The folder's title.
     *
     * @return The corresponding command.
     */
    public Command<FolderEntity> createFolderCommand(final UUID parentId,
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
     * Create a 'create root' command.
     *
     * @param f The folder to create.
     *
     * @return The corresponding command.
     */
    public Command<Void> createRootCommand(final FolderEntity f) {
        return new CreateRootCommand(
            _repository,
            _audit,
            f);
    }


    /**
     * Create a 'create page' command.
     *
     * @param parentId The parent folder in which the page will be created.
     * @param page The page to create.
     *
     * @return The corresponding command.
     */
    public Command<PageEntity> createPageCommand(final UUID parentId,
                                           final Page page) {

        return new CreatePageCommand(
            _repository,
            _audit,
            _producer,
            parentId,
            page);
    }


    /**
     * Create a 'publish resource' command.
     *
     * @param resourceId The ID of the resource to publish.
     *
     * @return The corresponding command.
     */
    public Command<Resource> publishResource(final UUID resourceId) {
        return new PublishCommand(
            _repository,
            _audit,
            resourceId);
    }


    /**
     * Create a 'lock resource' command.
     *
     * @param resourceId The ID of the resource to lock.
     *
     * @return The corresponding command.
     */
    public Command<Void> lockResourceCommand(final UUID resourceId) {
        return new LockResourceCommand(
            _repository,
            _audit,
            resourceId);
    }


    /**
     * Create an 'unlock resource' command.
     *
     * @param resourceId The ID of the resource to unlock;
     *
     * @return The corresponding command.
     */
    public Command<Void> unlockResourceCommand(final UUID resourceId) {
        return new UnlockResourceCommand(
            _repository,
            _audit,
            resourceId);
    }


    /**
     * Create an 'unpublish resource' command.
     *
     * @param resourceId The ID of the resource to unpublish.
     *
     * @return The corresponding command.
     */
    public Command<Void> unpublishResourceCommand(final UUID resourceId) {
        return new UnpublishResourceCommand(
            _repository,
            _audit,
            resourceId);
    }
}
