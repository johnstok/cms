/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.domain;

import java.net.URL;

import junit.framework.TestCase;
import ccc.api.core.Comment;
import ccc.api.types.EmailAddress;


/**
 * Tests for the comment class.
 *
 * @author Civic Computing Ltd.
 */
public class CommentTest
    extends
        TestCase {

    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testCreateDto() throws Exception {

        // ARRANGE
        final CommentEntity c = new CommentEntity(P, "Foo", "keith");
        c.setUrl(new URL("http://www.google.com"));
        c.setEmail(new EmailAddress("test@example.com"));

        // ACT
        final Comment dto = c.createDto();

        // ASSERT
        assertEquals("Foo", dto.getBody());
        assertEquals("http://www.google.com", dto.getUrl());
        assertEquals("keith", dto.getAuthor());
        assertEquals(P.getId(), dto.getResourceId());
    }

    /**
     * Test.
     */
    public void testDefaultConstructor() {

        // ARRANGE

        // ACT
        final CommentEntity c = new CommentEntity(P, "Foo", "keith");

        // ASSERT
        assertEquals("Foo", c.getBody());
        assertEquals("keith", c.getAuthor());
        assertEquals(P, c.getResource());
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testUrlMutator() throws Exception {

        // ARRANGE
        final CommentEntity c = new CommentEntity(P, "Foo", "keith");

        // ACT
        c.setUrl(new URL("http://www.google.com"));

        // ASSERT
        assertEquals("http://www.google.com", c.getUrl().toExternalForm());
    }


    /**
     * Test.
     */
    public void testUrlIsNullByDefault() {

        // ARRANGE

        // ACT
        final CommentEntity c = new CommentEntity(P, "Foo", "keith");

        // ASSERT
        assertNull(c.getUrl());
    }


    private static final PageEntity P = new PageEntity();
}
