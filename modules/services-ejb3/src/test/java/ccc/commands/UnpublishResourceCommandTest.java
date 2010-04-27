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
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
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
     */
    public void testUnpublishResource() {

        // ARRANGE
        final Search s = new Search("foo");
        s.lock(getUser());
        s.publish(getUser());

        expect(getRepository().find(ResourceEntity.class, s.getId())).andReturn(s);
        getAudit().record(isA(LogEntry.class));

        replayAll();

        final UnpublishResourceCommand c =
            new UnpublishResourceCommand(getRepository(), getAudit(), s.getId());

        // ACT
        c.execute(getUser(), getNow());

        // ASSERT
        verifyAll();
        assertFalse(s.isPublished());
    }
}
