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

import javax.persistence.EntityManager;

import ccc.types.DBC;


/**
 * A factory for repository objects.
 *
 * @author Civic Computing Ltd.
 */
class RepositoryFactory
    implements
        IRepositoryFactory {

    private final EntityManager _em;


    /**
     * Constructor.
     *
     * @param em The entity manager for this factory.
     */
    public RepositoryFactory(final EntityManager em) {
        DBC.require().notNull(em);
        _em = em;
    }


    /** {@inheritDoc} */
    @Override public LogEntryRepository createLogEntryRepo() {
        return new LogEntryRepositoryImpl(_em);
    }


    /** {@inheritDoc} */
    @Override public UserRepository createUserRepo() {
        return new UserRepositoryImpl(_em);
    }


    /** {@inheritDoc} */
    @Override public ResourceRepository createResourceRepository() {
        return new ResourceRepositoryImpl(_em);
    }


    /** {@inheritDoc} */
    @Override public DataRepository createDataRepository() {
        return DataRepositoryImpl.onFileSystem(_em);
    }


    /** {@inheritDoc} */
    @Override public ActionRepository createActionRepository() {
        return new ActionRepositoryImpl(_em);
    }


    /** {@inheritDoc} */
    @Override public CommentRepository createCommentRepo() {
        return new CommentRepositoryImpl(_em);
    }


    /** {@inheritDoc} */
    @Override public GroupRepository createGroupRepo() {
        return new GroupRepositoryImpl(_em);
    }
}
