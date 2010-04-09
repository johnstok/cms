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
package ccc.api.exceptions;

import java.util.UUID;

import ccc.api.exceptions.UnauthorizedException;

import junit.framework.TestCase;


/**
 * Tests for the {@link UnauthorizedException} class.
 *
 * @author Civic Computing Ltd.
 */
public class UnauthorizedExceptionTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testMessagesAreFormedCorrectly() {

        // ARRANGE
        final UUID user   = UUID.randomUUID();
        final UUID target = UUID.randomUUID();

        // ACT
        final UnauthorizedException withUser =
            new UnauthorizedException(target, user);
        final UnauthorizedException withoutUser =
            new UnauthorizedException(target, null);

        // ASSERT
        assertEquals(
            "User " + user + " isn't authorized to access entity " + target + ".",
            withUser.getMessage());
        assertEquals(
            "User null isn't authorized to access entity " + target + ".",
            withoutUser.getMessage());
    }
}
