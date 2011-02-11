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
import java.util.UUID;

import ccc.api.core.Comment;
import ccc.api.core.Folder;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.CommentStatus;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


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
        final Folder folder = tempFolder();
        final Comment c =
            new Comment(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
                "http://www.google.com");
        c.setEmail("test@example.com");
        c.setStatus(CommentStatus.APPROVED);

        // ACT
        final Comment actual = getComments().create(c);

        // ASSERT
        assertEquals("keith", actual.getAuthor());
        assertEquals("Hello world", actual.getBody());
        assertEquals("http://www.google.com", actual.getUrl());
        assertEquals(folder.getId(), actual.getResourceId());
        assertEquals(CommentStatus.APPROVED, actual.getStatus());
        assertEquals("test@example.com", actual.getEmail());
    }


    /**
     * Test.
     */
    public void testDeleteComment() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Comment comment =
            new Comment(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
                "http://www.google.com");
        comment.setEmail("test@example.com");
        final Comment c = getComments().create(comment);
        getComments().retrieve(c.getId()); // Comment exists.

        // ACT
        getComments().delete(c.getId());

        // ASSERT
        final Comment comm = getComments().retrieve(c.getId());
        assertNull("Comment should be null", comm);
    }


    /**
     * Test.
     */
    public void testUpdateComment() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Comment comment =
            new Comment(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
            "http://www.google.com");
        comment.setEmail("test@example.com");
        final Comment c = getComments().create(comment);

        // ACT
        c.setBody("Updated world!");
        c.setStatus(CommentStatus.APPROVED);
        c.setEmail("new@example.com");

        getComments().update(c.getId(), c);

        // ASSERT
        final Comment actual = getComments().retrieve(c.getId());

        assertEquals("Updated world!", actual.getBody());
        assertEquals(CommentStatus.APPROVED, actual.getStatus());
        assertEquals("new@example.com", actual.getEmail());
    }


    /**
     * Test.
     */
    public void testAnonymousRetrieve() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final String templateName = UUID.randomUUID().toString();

        final Comment c =
            new Comment(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
                "http://www.google.com");
        c.setEmail("test@example.com");
        c.setStatus(CommentStatus.APPROVED);
        final Comment comment = getComments().create(c);

        final Template t = new Template();
        t.setName(new ResourceName(templateName));
        t.setParent(folder.getId());
        t.setDescription(templateName);
        t.setTitle(templateName);
        t.setBody("$services.getComments().retrieve($uuid.fromString('"
            +comment.getId()+"')).getBody()");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        final ResourceSummary template = getTemplates().create(t);

        final ResourceSummary page = tempPage(folder.getId(), template.getId());
        getCommands().lock(folder.getId());
        getCommands().publish(folder.getId());
        getCommands().lock(page.getId());
        getCommands().publish(page.getId());

        // ACT
        getSecurity().logout();
        final String pContent = getBrowser().get(page.getAbsolutePath());

        // ASSERT
        assertEquals("Hello world", pContent);
    }


    /**
     * Test.
     */
    public void testAnonymousQuery() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final String templateName = UUID.randomUUID().toString();
        final Comment c =
            new Comment(
                "keith",
                "Hello world",
                folder.getId(),
                new Date(),
                "http://www.google.com");
        c.setEmail("test@example.com");
        c.setStatus(CommentStatus.APPROVED);
        getComments().create(c);

        final Template t = new Template();
        t.setName(new ResourceName(templateName));
        t.setParent(folder.getId());
        t.setDescription(templateName);
        t.setTitle(templateName);
        final StringBuilder sb = new StringBuilder();
        sb.append("$services.getComments().query($uuid.fromString('");
        sb.append(folder.getId().toString());
        sb.append("'),$enums.of('ccc.api.types.CommentStatus', 'APPROVED'),");
        sb.append("'author',$enums.of('ccc.api.types.SortOrder', 'ASC'),");
        sb.append("1, 20).getTotalCount()");
        t.setBody(sb.toString());
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        final ResourceSummary template = getTemplates().create(t);
        final ResourceSummary page = tempPage(folder.getId(), template.getId());
        getCommands().lock(folder.getId());
        getCommands().publish(folder.getId());
        getCommands().lock(page.getId());
        getCommands().publish(page.getId());


        // ACT
        getSecurity().logout();
        final String pContent = getBrowser().get(page.getAbsolutePath());

        // ASSERT
        assertEquals("1", pContent);
    }
}
