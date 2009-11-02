/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.util.Date;
import java.util.UUID;

import ccc.domain.CccCheckedException;
import ccc.domain.Folder;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.CommandType;


/**
 * Command: create a new folder.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderCommand extends CreateResourceCommand<Folder> {

    private final UUID _parentFolder;
    private final String _name;
    private final String _title;


    /**
     * Constructor.
     *
     * @param repository The DAO used for CRUD operations, etc.
     * @param audit The audit log to record business actions.
     * @param parentFolder The folder in which the new folder will be created.
     * @param name The name of the new folder.
     * @param title The title of the new folder.
     */
    public CreateFolderCommand(final ResourceRepository repository,
                               final LogEntryRepository audit,
                               final UUID parentFolder,
                               final String name,
                               final String title) {
        super(repository, audit);
        _parentFolder = parentFolder;
        _name = name;
        _title = title;
    }


    /** {@inheritDoc} */
    @Override
    public Folder doExecute(final User actor,
                            final Date happenedOn) throws CccCheckedException {
        final Folder f = new Folder(_name);
        f.title((null==_title)?_name:_title);

        create(actor, happenedOn, _parentFolder, f);

        return f;
    }


    /** {@inheritDoc} */
    @Override
    protected CommandType getType() { return CommandType.FOLDER_CREATE; }
}
