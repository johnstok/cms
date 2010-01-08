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
package ccc.acceptance;

import java.util.Date;

import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.rest.RestException;
import ccc.rest.dto.CommentDto;
import ccc.rest.dto.ResourceSummary;
import ccc.types.CommentStatus;
import ccc.types.HttpStatusCode;


/**
 * Tests for the manipulating resources.
 *
 * @author Civic Computing Ltd.
 */
public class CommentsAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testCreateComment() throws RestException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final CommentDto c =
            new CommentDto(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
                "http://www.google.com");

        // ACT
        final CommentDto actual = getComments().create(c);

        // ASSERT
        assertEquals("keith", actual.getAuthor());
        assertEquals("Hello world", actual.getBody());
        assertEquals("http://www.google.com", actual.getUrl());
        assertEquals(folder.getId(), actual.getResourceId());
        assertEquals(CommentStatus.PENDING, actual.getStatus());
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testDeleteComment() throws RestException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final CommentDto c =
            getComments().create(
                new CommentDto(
                    "keith",
                    "Hello world",
                    folder.getId(),
                    new Date(),
                    "http://www.google.com"));
        getComments().retrieve(c.getId()); // Comment exists.

        // ACT
        getComments().delete(c.getId());

        // ASSERT
        try {
            getComments().retrieve(c.getId());
            fail();
        } catch (final ClientResponseFailure e) {
            assertEquals(
                HttpStatusCode.IM_A_TEAPOT, e.getResponse().getStatus());
        }
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUpdateComment() throws RestException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final CommentDto c =
            getComments().create(
                new CommentDto(
                    "keith",
                    "Hello world",
                    folder.getId(),
                    new Date(),
                "http://www.google.com"));

        // ACT
        c.setBody("Updated world!");
        c.setStatus(CommentStatus.APPROVED);

        getComments().update(c.getId(), c);

        // ASSERT
        final CommentDto actual = getComments().retrieve(c.getId());

        assertEquals("Updated world!", actual.getBody());
        assertEquals(CommentStatus.APPROVED, actual.getStatus());
    }
}