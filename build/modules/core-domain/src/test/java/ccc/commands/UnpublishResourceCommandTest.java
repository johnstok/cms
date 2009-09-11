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
import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Search;


/**
 * Tests for the {@link UnpublishResourceCommand} class.
 *
 * @author Civic Computing Ltd.
 */
public class UnpublishResourceCommandTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testUnpublishResource() throws CccCheckedException {

        // ARRANGE
        final Search s = new Search("foo");
        s.lock(getUser());
        s.publish(getUser());

        expect(getRepository().find(Resource.class, s.id())).andReturn(s);
        getAudit().record(isA(LogEntry.class));

        replayAll();

        final UnpublishResourceCommand c =
            new UnpublishResourceCommand(getRepository(), getAudit());

        // ACT
        c.execute(getUser(), getNow(), s.id());

        // ASSERT
        verifyAll();
        assertFalse(s.isPublished());
    }
}
