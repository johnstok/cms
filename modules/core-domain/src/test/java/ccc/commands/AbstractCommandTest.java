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

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;


/**
 * Abstract class to simplify writing tests for commands.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractCommandTest
    extends
        TestCase {

    protected final User _user = new User("currentUser");
    protected final Date _now = new Date();

    protected Repository _repository;
    protected LogEntryRepository _audit;


    /** Constructor. */
    public AbstractCommandTest() { super(); }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _repository = createStrictMock(Repository.class);
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