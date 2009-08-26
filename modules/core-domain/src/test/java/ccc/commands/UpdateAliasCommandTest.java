/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
import ccc.domain.Alias;
import ccc.domain.LogEntry;
import ccc.domain.CccCheckedException;
import ccc.domain.Resource;
import ccc.domain.Search;


/**
 * Tests for the {@link UpdateAliasCommand} class.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasCommandTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testUpdateAlias() throws CccCheckedException {

        // ARRANGE
        final Search foo = new Search("foo");
        final Search bar = new Search("bar");
        final Alias alias = new Alias("alias", foo);
        alias.lock(_user);

        expect(_dao.find(Alias.class, alias.id())).andReturn(alias);
        expect(_dao.find(Resource.class, bar.id())).andReturn(bar);
        _audit.record(isA(LogEntry.class));
        replayAll();

        final UpdateAliasCommand c =  new UpdateAliasCommand(_dao, _audit);

        // ACT
        c.execute(_user, _now, bar.id(), alias.id());

        // ASSERT
        verifyAll();
        assertEquals(bar, alias.target());
    }
}
