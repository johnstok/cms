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
package ccc.client.gwt.presenters;

import ccc.api.core.Comment;
import ccc.api.types.CommandType;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.events.Event;
import ccc.client.gwt.binding.CommentModelData;
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.remoting.UpdateCommentAction;
import ccc.client.gwt.views.gxt.CommentView;
import ccc.client.gwt.widgets.ContentCreator;


/**
 * MVP presenter for updating comments.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCommentPresenter
    extends
        AbstractPresenter<CommentView, CommentModelData>
    implements
        Editable {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public UpdateCommentPresenter(final CommentView view,
                                  final CommentModelData model) {
        super(view, model);

        getView().setAuthor(model.getAuthor());
        getView().setBody2(model.getBody());
        getView().setStatus(model.getStatus());
        getView().setUrl2(model.getUrl());
        getView().setEmail(model.getEmail());

        getView().show(this);
    }


    /** {@inheritDoc} */
    @Override
    public void save() {

        final ValidationResult result = getView().getValidationResult();

        if (result.isValid()) {
            final Comment updated =
                new Comment(
                    getView().getAuthor(),
                    getView().getBody2(),
                    getModel().getResourceId(),
                    getModel().getTimestamp(),
                    getView().getUrl2());
            updated.setId(getModel().getId());
            updated.setStatus(getView().getStatus());
            updated.setEmail(getView().getEmail());
            updated.addLink("self", getModel().getDelegate().self());

            new UpdateCommentAction(updated).execute();

        } else {
            ContentCreator.WINDOW.alert(result.getErrors().get(0));
        }
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case COMMENT_UPDATE:
                dispose();
                break;

            default:
                break;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void cancel() { dispose(); }
}
