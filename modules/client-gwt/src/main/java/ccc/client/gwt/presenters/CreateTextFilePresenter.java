/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.presenters;

import ccc.api.core.File;
import ccc.api.types.MimeType;
import ccc.client.core.Editable;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.events.ResourceCreated;
import ccc.client.gwt.events.ResourceCreated.ResourceCreatedHandler;
import ccc.client.gwt.remoting.CreateTextFileAction;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.client.views.CreateTextFile;


/**
 * MVP presenter for creating text file.
 *
 * @author Civic Computing Ltd.
 */
public class CreateTextFilePresenter
    extends
        AbstractPresenter<CreateTextFile, ResourceSummaryModelData>
    implements
        Editable,
        ResourceCreatedHandler {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateTextFilePresenter(final CreateTextFile view,
                                   final ResourceSummaryModelData model) {
        super(view, model);
        addHandler(ResourceCreated.TYPE, this);
        getView().show(this);
    }


    /** {@inheritDoc} */
    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void save() {
        final CreateTextFile view = getView();

        if (view.getValidationResult().isValid()) {
            final File dto = new File(
                getModel().getId(),
                view.getName(),
                new MimeType(view.getPrimaryMime(), view.getSubMime()),
                view.isMajorEdit(),
                view.getComment(),
                view.getText());
            new CreateTextFileAction(dto).execute();

        } else {
            ContentCreator.WINDOW.alert(
                view.getValidationResult().getErrorText());
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onCreate(final ResourceCreated event) {
        clearHandlers();
        getView().hide();
    }
}
