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
import ccc.domain.LogEntry;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.Search;



/**
 * Tests for the {@link IncludeInMainMenuCommand} class.
 *
 * @author Civic Computing Ltd.
 */
public class IncludeInMainMenuCommandTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testIncludeInMainMenu() throws CccCheckedException {

        // ARRANGE
        final Search s = new Search("foo");
        s.lock(_user);

        expect(_repository.find(Resource.class, s.id())).andReturn(s);
        _audit.record(isA(LogEntry.class));

        replayAll();

        final IncludeInMainMenuCommand c =
            new IncludeInMainMenuCommand(_repository, _audit);

        // ACT
        c.execute(_user, _now, s.id(), true);

        // ASSERT
        verifyAll();
        assertTrue(s.includeInMainMenu());
    }

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testRemoveFromMainMenu() throws CccCheckedException {

        // ARRANGE
        final Search s = new Search("foo");
        s.lock(_user);

        expect(_repository.find(Resource.class, s.id())).andReturn(s);
        _audit.record(isA(LogEntry.class));

        replayAll();

        final IncludeInMainMenuCommand c =
            new IncludeInMainMenuCommand(_repository, _audit);

        // ACT
        c.execute(_user, _now, s.id(), false);

        // ASSERT
        verifyAll();
        assertFalse(s.includeInMainMenu());
    }
}
