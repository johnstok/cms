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
 * Revision      $Rev: 2472 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-02-18 15:56:04 +0000 (Thu, 18 Feb 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import static ccc.client.core.InternalServices.*;

import java.util.Map;

import ccc.api.core.User;
import ccc.client.actions.UpdateUserAction;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.widgets.MetadataGrid;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Text;


/**
 * Editable view of a user's metadata.
 *
 * @author Civic Computing Ltd.
 */
public class UserMetadataDialog extends AbstractEditDialog {

    private final User _user;
    private final MetadataGrid _metadataPanel;
    private static final int HEIGHT = 420;

    /**
     * Constructor.
     *
     * @param user The user.
     */
    public UserMetadataDialog(final User user) {
        super(I18n.uiConstants.metadata(), InternalServices.globals);
        setHeight(HEIGHT);
        _user = user;

        final Text fieldName = new Text("Values:");
        fieldName.setStyleName("x-form-item");
        addField(fieldName);

        _metadataPanel = new MetadataGrid(user.getMetadata().entrySet());

        addField(_metadataPanel);
        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                _metadataPanel.handleResize(be);
            }
        });
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    validator.validateMetadataValues(
                        _metadataPanel.currentMetadata()));

                if (!vr.isValid()) {
                    InternalServices.window.alert(vr.getErrorText());
                    return;
                }

                updateMetaData();
            }
        };
    }


    private void updateMetaData() {
        final Map<String, String> metadata = _metadataPanel.currentMetadata();
        _user.setMetadata(metadata);
        new UpdateUserAction(_user).execute(
            new HideDialogCallback<User>(
                I18n.uiConstants.editUser(), UserMetadataDialog.this));
    }
}
