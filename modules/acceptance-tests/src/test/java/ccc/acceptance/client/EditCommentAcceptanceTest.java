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
package ccc.acceptance.client;

import java.util.Date;

import ccc.acceptance.client.views.CommentViewFake;
import ccc.api.core.Comment;
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommentStatus;
import ccc.client.presenters.UpdateCommentPresenter;
import ccc.client.views.ICommentView;
import ccc.tests.acceptance.AbstractAcceptanceTest;


/**
 * Tests for the {@link UpdateCommentPresenter} class.
 *
 * @author Civic Computing Ltd.
 */
public class EditCommentAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     */
    public void testEditCommentSuccess() {

        //ARRANGE
        ResourceSummary parent = tempFolder();
        ResourceSummary template = dummyTemplate(parent);
        ResourceSummary pr = tempPage(parent.getId(), template.getId());
        getCommands().lock(pr.getId());
        Comment comment =
            new Comment("author",
                "btext",
                pr.getId(),
                new Date(),
                "http://foo.foo");
        comment.setEmail("foo@civicuk.com");
        Comment model = getComments().create(comment);

        ICommentView view = new CommentViewFake();
        UpdateCommentPresenter p = new UpdateCommentPresenter(view, model);
        view.setBody2("new text");
        view.setEmail("new@civicuk.com");
        view.setAuthor("new");
        view.setUrl2("http://new.civicuk.com");
        view.setStatus(CommentStatus.SPAM);

        // ACT
        p.save();

        // ASSERT
        Comment c = getComments().retrieve(model.getId());
        assertEquals("new text", c.getBody());
        assertEquals("new", c.getAuthor());
        assertEquals("http://new.civicuk.com", c.getUrl());
        assertEquals("new@civicuk.com", c.getEmail());
        assertEquals(CommentStatus.SPAM, c.getStatus());

    }

}
