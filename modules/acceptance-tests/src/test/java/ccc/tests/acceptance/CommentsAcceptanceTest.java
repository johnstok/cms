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
package ccc.tests.acceptance;

import java.util.Date;

import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.dto.CommentDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.CommentStatus;


/**
 * Tests for the manipulating groups.
 *
 * @author Civic Computing Ltd.
 */
public class CommentsAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     */
    public void testCreateComment() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final CommentDto c =
            new CommentDto(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
                "http://www.google.com");
        c.setEmail("test@example.com");
        c.setStatus(CommentStatus.APPROVED);

        // ACT
        final CommentDto actual = getComments().create(c);

        // ASSERT
        assertEquals("keith", actual.getAuthor());
        assertEquals("Hello world", actual.getBody());
        assertEquals("http://www.google.com", actual.getUrl());
        assertEquals(folder.getId(), actual.getResourceId());
        assertEquals(CommentStatus.PENDING, actual.getStatus());
        assertEquals("test@example.com", actual.getEmail());
    }


    /**
     * Test.
     */
    public void testDeleteComment() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final CommentDto comment =
            new CommentDto(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
                "http://www.google.com");
        comment.setEmail("test@example.com");
        final CommentDto c = getComments().create(comment);
        getComments().retrieve(c.getId()); // Comment exists.

        // ACT
        getComments().delete(c.getId());

        // ASSERT
        try {
            getComments().retrieve(c.getId());
            fail();
        } catch (final ClientResponseFailure e) {
            final EntityNotFoundException enf = convertException(e);
            assertEquals(c.getId(), enf.getId());
        }
    }


    /**
     * Test.
     */
    public void testUpdateComment() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final CommentDto comment =
            new CommentDto(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
            "http://www.google.com");
        comment.setEmail("test@example.com");
        final CommentDto c = getComments().create(comment);

        // ACT
        c.setBody("Updated world!");
        c.setStatus(CommentStatus.APPROVED);
        c.setEmail("new@example.com");

        getComments().update(c.getId(), c);

        // ASSERT
        final CommentDto actual = getComments().retrieve(c.getId());

        assertEquals("Updated world!", actual.getBody());
        assertEquals(CommentStatus.APPROVED, actual.getStatus());
        assertEquals("new@example.com", actual.getEmail());
    }
}
