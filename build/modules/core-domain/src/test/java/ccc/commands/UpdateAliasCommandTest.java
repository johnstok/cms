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
import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
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
        alias.lock(getUser());

        expect(getRepository().find(Alias.class, alias.id())).andReturn(alias);
        expect(getRepository().find(Resource.class, bar.id())).andReturn(bar);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        final UpdateAliasCommand c =
            new UpdateAliasCommand(getRepository(), getAudit());

        // ACT
        c.execute(getUser(), getNow(), bar.id(), alias.id());

        // ASSERT
        verifyAll();
        assertEquals(bar, alias.target());
    }
}
