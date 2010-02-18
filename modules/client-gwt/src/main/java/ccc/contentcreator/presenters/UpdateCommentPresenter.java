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
package ccc.contentcreator.presenters;

import ccc.contentcreator.binding.CommentModelData;
import ccc.contentcreator.core.AbstractPresenter;
import ccc.contentcreator.core.EventBus;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.core.ValidationResult;
import ccc.contentcreator.events.CommentUpdatedEvent;
import ccc.contentcreator.remoting.UpdateCommentAction;
import ccc.contentcreator.views.gxt.CommentView;
import ccc.rest.dto.CommentDto;

import com.google.gwt.http.client.Response;


/**
 * MVP presenter for updating comments.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateCommentPresenter
    extends
        AbstractPresenter<CommentView, CommentModelData> {


    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param bus Implementation of the Event Bus API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public UpdateCommentPresenter(final Globals globals,
                                  final EventBus bus,
                                  final CommentView view,
                                  final CommentModelData model) {
        super(globals, bus, view, model);

        getView().setPresenter(this);

        getView().setAuthor(model.getAuthor());
        getView().setBody2(model.getBody());
        getView().setStatus(model.getStatus());
        getView().setUrl2(model.getUrl());

        getView().show();
    }


    public void update() {

        final ValidationResult result = getView().getValidationResult();

        if (result.isValid()) {
            final CommentDto updated =
                new CommentDto(
                    getView().getAuthor(),
                    getView().getBody2(),
                    getModel().getResourceId(),
                    getModel().getTimestamp(),
                    getView().getUrl2());
            updated.setId(getModel().getId());
            updated.setStatus(getView().getStatus());
            updated.setEmail(getModel().getEmail());

            new UpdateCommentAction(updated) {

                /** {@inheritDoc} */
                @Override protected void onNoContent(final Response response) {
                    getModel().setDelegate(updated);
                    getView().hide();
                    getBus().put(new CommentUpdatedEvent(getModel()));
                }

            }.execute();
        } else {
            getGlobals().alert(result.getErrors().get(0));
        }
    }
}
