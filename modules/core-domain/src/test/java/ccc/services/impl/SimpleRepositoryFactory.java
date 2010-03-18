/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.services.impl;

import ccc.persistence.ActionRepository;
import ccc.persistence.CommentRepository;
import ccc.persistence.DataRepository;
import ccc.persistence.GroupRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.UserRepository;


/**
 * Simple container implementation of a repo factory - for testing.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleRepositoryFactory
    implements
        IRepositoryFactory {

    private ActionRepository _actionRepo;
    private CommentRepository _commentRepo;
    private DataRepository _dataRepo;
    private GroupRepository _groupRepo;
    private LogEntryRepository _logEntryRepo;
    private ResourceRepository _resourceRepo;
    private UserRepository _userRepo;

    /** {@inheritDoc} */
    @Override
    public ActionRepository createActionRepository() {
        return _actionRepo;
    }

    /** {@inheritDoc} */
    @Override
    public CommentRepository createCommentRepo() {
        return _commentRepo;
    }

    /** {@inheritDoc} */
    @Override
    public DataRepository createDataRepository() {
        return _dataRepo;
    }

    /** {@inheritDoc} */
    @Override
    public GroupRepository createGroupRepo() {
        return _groupRepo;
    }

    /** {@inheritDoc} */
    @Override
    public LogEntryRepository createLogEntryRepo() {
        return _logEntryRepo;
    }

    /** {@inheritDoc} */
    @Override
    public ResourceRepository createResourceRepository() {
        return _resourceRepo;
    }

    /** {@inheritDoc} */
    @Override
    public UserRepository createUserRepo() {
        return _userRepo;
    }


    /**
     * Mutator.
     *
     * @param actionRepo The actionRepo to set.
     */
    public void setActionRepo(final ActionRepository actionRepo) {
        _actionRepo = actionRepo;
    }


    /**
     * Mutator.
     *
     * @param commentRepo The commentRepo to set.
     */
    public void setCommentRepo(final CommentRepository commentRepo) {
        _commentRepo = commentRepo;
    }


    /**
     * Mutator.
     *
     * @param dataRepo The dataRepo to set.
     */
    public void setDataRepo(final DataRepository dataRepo) {
        _dataRepo = dataRepo;
    }


    /**
     * Mutator.
     *
     * @param groupRepo The groupRepo to set.
     */
    public void setGroupRepo(final GroupRepository groupRepo) {
        _groupRepo = groupRepo;
    }


    /**
     * Mutator.
     *
     * @param logEntryRepo The logEntryRepo to set.
     */
    public void setLogEntryRepo(final LogEntryRepository logEntryRepo) {
        _logEntryRepo = logEntryRepo;
    }


    /**
     * Mutator.
     *
     * @param resourceRepo The resourceRepo to set.
     */
    public void setResourceRepo(final ResourceRepository resourceRepo) {
        _resourceRepo = resourceRepo;
    }


    /**
     * Mutator.
     *
     * @param userRepo The userRepo to set.
     */
    public void setUserRepo(final UserRepository userRepo) {
        _userRepo = userRepo;
    }
}
