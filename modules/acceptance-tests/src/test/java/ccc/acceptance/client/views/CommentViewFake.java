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
package ccc.acceptance.client.views;

import ccc.api.types.CommentStatus;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.ICommentView;


/**
 * Fake implementation of the {@link ICommentView} view.
 *
 * @author Civic Computing Ltd.
 */
public class CommentViewFake implements ICommentView {

    private String _author;
    private String _body2;
    private String _email;
    private CommentStatus _commentStatus;
    private String _url2;
    private Editable _presenter;
    private boolean _showing;
    private final ValidationResult _validationResult = new ValidationResult();

    @Override
    public String getAuthor() {
        return _author;
    }

    @Override
    public String getBody2() {
        return _body2;
    }

    @Override
    public String getEmail() {
        return _email;
    }

    @Override
    public CommentStatus getStatus() {
        return _commentStatus;
    }

    @Override
    public String getUrl2() {
        return _url2;
    }

    @Override
    public void setAuthor(final String author) {
        _author = author;
    }

    @Override
    public void setBody2(final String commentBody) {
        _body2 = commentBody;
    }

    @Override
    public void setEmail(final String email) {
        _email = email;
    }

    @Override
    public void setStatus(final CommentStatus status) {
        _commentStatus = status;
    }

    @Override
    public void setUrl2(final String url) {
        _url2 = url;
    }


    /** {@inheritDoc} */
    @Override
    public void hide() {
        _presenter = null;
        _showing   = false;
    }


    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        _showing   = true;
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        return _validationResult;
    }
}
