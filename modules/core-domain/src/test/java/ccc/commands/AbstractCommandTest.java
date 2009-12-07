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
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.Username;


/**
 * Abstract class to simplify writing tests for commands.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractCommandTest
    extends
        TestCase {

    private final User _user =
        new User(new Username("currentUser"), "password");
    private final Date _now = new Date();

    private ResourceRepository _repository;
    private LogEntryRepository _audit;


    /** Constructor. */
    public AbstractCommandTest() { super(); }


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
    @Override
    protected void setUp() {
        _repository = createStrictMock(ResourceRepository.class);
        _audit = createStrictMock(LogEntryRepository.class);
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _audit = null;
        _repository = null;
    }


    /** Verify all mocks. */
    protected void verifyAll() {
        verify(_repository, _audit);
    }


    /** Replay all mocks. */
    protected void replayAll() {
        replay(_repository, _audit);
    }
}
