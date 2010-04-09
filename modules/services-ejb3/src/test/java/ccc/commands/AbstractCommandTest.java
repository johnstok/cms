/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

package ccc.commands;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.api.types.EmailAddress;
import ccc.api.types.Username;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.GroupRepository;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.UserRepository;
import ccc.services.impl.SimpleRepositoryFactory;


/**
 * Abstract class to simplify writing tests for commands.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractCommandTest
    extends
        TestCase {

    protected final User _user =
        new User(new Username("currentUser"), "password");
    protected final Date _now = new Date();

    protected final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
    protected SimpleRepositoryFactory _repoFactory =
        new SimpleRepositoryFactory();

    protected ResourceRepository _repository;
    protected LogEntryRepository _audit;
    protected GroupRepository _groups;
    protected UserRepository _um;


    /** Constructor. */
    public AbstractCommandTest() {
        super();
        _user.setEmail(new EmailAddress("test@civicuk.com"));
    }


    /**
     * Accessor.
     *
     * @return Returns the user.
     */
    public User getUser() {
        return _user;
    }


    /**
     * Accessor.
     *
     * @return Returns the now.
     */
    public Date getNow() {
        return _now;
    }


    /**
     * Accessor.
     *
     * @return Returns the repository.
     */
    public ResourceRepository getRepository() {
        return _repository;
    }


    /**
     * Accessor.
     *
     * @return Returns the audit.
     */
    public LogEntryRepository getAudit() {
        return _audit;
    }


    /** {@inheritDoc} */
    @Override protected void setUp() throws Exception {
        _repository = createStrictMock(ResourceRepository.class);
        _audit = createStrictMock(LogEntryRepository.class);
        _groups = createStrictMock(GroupRepository.class);
        _um = createStrictMock(UserRepository.class);
        _repoFactory.setResourceRepo(_repository);
        _repoFactory.setLogEntryRepo(_audit);
        _repoFactory.setGroupRepo(_groups);
        _repoFactory.setUserRepo(_um);
    }


    /** {@inheritDoc} */
    @Override protected void tearDown() throws Exception {
        _repoFactory.setUserRepo(null);
        _repoFactory.setGroupRepo(null);
        _repoFactory.setLogEntryRepo(null);
        _repoFactory.setResourceRepo(null);
        _um = null;
        _groups = null;
        _audit = null;
        _repository = null;
    }


    /** Verify all mocks. */
    protected void verifyAll() {
        verify(_repository, _audit, _groups, _um);
    }


    /** Replay all mocks. */
    protected void replayAll() {
        replay(_repository, _audit, _groups, _um);
    }
}
