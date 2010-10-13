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
package ccc.persistence;

import ccc.api.types.DBC;


/**
 * A factory for repository objects.
 *
 * @author Civic Computing Ltd.
 */
public class RepositoryFactory
    implements
        IRepositoryFactory {

    private final Repository _repo;


    /**
     * Constructor.
     *
     * @param repo The base persistence repository to use.
     */
    public RepositoryFactory(final Repository repo) {
        _repo = DBC.require().notNull(repo);
    }


    /** {@inheritDoc} */
    @Override public LogEntryRepository createLogEntryRepo() {
        return new LogEntryRepositoryImpl(_repo);
    }


    /** {@inheritDoc} */
    @Override public UserRepository createUserRepo() {
        return new UserRepositoryImpl(_repo);
    }


    /** {@inheritDoc} */
    @Override public ResourceRepository createResourceRepository() {
        return new ResourceRepositoryImpl(_repo);
    }


    /** {@inheritDoc} */
    @Override public DataRepository createDataRepository() {
        return DataRepositoryImpl.onFileSystem(createSettingsRepository());
    }


    /** {@inheritDoc} */
    @Override public ActionRepository createActionRepository() {
        return new ActionRepositoryImpl(_repo);
    }


    /** {@inheritDoc} */
    @Override public CommentRepository createCommentRepo() {
        return new CommentRepositoryImpl(_repo);
    }


    /** {@inheritDoc} */
    @Override public GroupRepository createGroupRepo() {
        return new GroupRepositoryImpl(_repo);
    }


    /** {@inheritDoc} */
    @Override
    public SettingsRepository createSettingsRepository() {
        return new SettingsRepository(_repo);
    }
}
