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
import ccc.client.gwt.core.AbstractPresenter;
import ccc.client.gwt.core.Editable;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.events.TextFileUpdated;
import ccc.client.gwt.events.TextFileUpdated.UpdatedHandler;
import ccc.client.gwt.remoting.EditTextFileAction;
import ccc.client.gwt.views.EditTextFile;


/**
 * MVP presenter for renaming resources.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFilePresenter
    extends
        AbstractPresenter<EditTextFile, File>
    implements
        Editable,
        UpdatedHandler {

    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public EditTextFilePresenter(final Globals globals,
                                 final EditTextFile view,
                                 final File model) {
        super(globals, view, model);

        addHandler(TextFileUpdated.TYPE, this);

        getView().show(this);
        getView().setText(model.getContent());
        getView().setPrimaryMime(model.getMimeType().getPrimaryType());
        getView().setSubMime(model.getMimeType().getSubType());
    }


    /** {@inheritDoc} */
    @Override
    public void cancel() {

        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public void save() {
        final EditTextFile view = getView();

        if (view.getValidationResult().isValid()) {
            final File dto = new File(getModel().getId(),
                view.getText(),
                new MimeType(view.getPrimaryMime(), view.getSubMime()),
                view.isMajorEdit(),
                view.getComment());
            new EditTextFileAction(dto).execute();

        } else {
            getGlobals().alert(
                getView().getValidationResult().getErrorText());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onUpdated(final TextFileUpdated event) {
        getView().hide();
    }
}
