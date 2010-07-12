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

package ccc.client.views;

import ccc.api.types.CommentStatus;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.core.View;


/**
 * API for viewing a comment.
 *
 * @author Civic Computing Ltd.
 */
public interface ICommentView
    extends
        View<Editable> {

    /**
     * Mutator.
     *
     * @param author The author of the comment.
     */
    void setAuthor(final String author);

    /**
     * Mutator.
     *
     * @param commentBody The text of the comment.
     */
    void setBody2(final String commentBody);

    /**
     * Mutator.
     *
     * @param status The status of the comment.
     */
    void setStatus(final CommentStatus status);

    /**
     * Mutator.
     *
     * @param url The url of the comment.
     */
    void setUrl2(final String url);

    /**
     * Accessor.
     *
     * @return The author of the comment.
     */
    String getAuthor();

    /**
     * Accessor.
     *
     * @return The text of the comment.
     */
    String getBody2();

    /**
     * Accessor.
     *
     * @return The status of the comment.
     */
    CommentStatus getStatus();

    /**
     * Accessor.
     *
     * @return The URL of the comment.
     */
    String getUrl2();

    /** {@inheritDoc} */
    ValidationResult getValidationResult();

    /**
     * Mutator.
     *
     * @param email Email to set.
     */
    void setEmail(final String email);

    /**
     * Accessor.
     *
     * @return Email field value.
     */
    String getEmail();

}