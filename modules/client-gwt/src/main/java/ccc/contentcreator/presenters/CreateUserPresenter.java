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
package ccc.contentcreator.presenters;

import ccc.api.dto.UserDto;
import ccc.api.types.Username;
import ccc.contentcreator.core.Editable;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.core.ValidationResult;
import ccc.contentcreator.events.UserCreated;
import ccc.contentcreator.events.UserCreated.UserCreatedHandler;
import ccc.contentcreator.i18n.UIMessages;
import ccc.contentcreator.remoting.CreateUserAction;
import ccc.contentcreator.views.CreateUser;
import ccc.contentcreator.widgets.ContentCreator;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A controller for user creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserPresenter implements Editable, UserCreatedHandler {

    private final CreateUser _view;
    private HandlerRegistration _hReg;

    private final UIMessages _messages;
    private final Globals   _globals;

    /**
     * Constructor.
     *
     * @param view The create user dialog this controller will manage.
     * @param globals The globals factory for this controller.
     */
    public CreateUserPresenter(final CreateUser view,
                                final Globals globals) {
        _view = view;
        _globals     = globals;
        _messages    = _globals.uiMessages();
        render(ContentCreator.EVENT_BUS.addHandler(UserCreated.TYPE, this));
    }

    /**
     * Render this presenter.
     *
     * @param hReg The registration of the success event handler.
     */
    protected final void render(final HandlerRegistration hReg) {
        _hReg = hReg;
        _view.show(this);
    }

    /** {@inheritDoc} */
    @Override
    public final void cancel() {
        dispose();
    }

    /**
     * Dispose of this presenter.
     */
    protected final void dispose() {
        _hReg.removeHandler();
        _view.hide();
    }


    /**
     * Accessor.
     *
     * @return Returns the messages.
     */
    UIMessages getMessages() {
        return _messages;
    }

    /** {@inheritDoc} */
    @Override
    public void save() {
        if (valid()) {
            final UserDto user = new UserDto();
            unbind(user);
            new CreateUserAction(user).execute();
        }
    }


    /**
     * Check that the data in the view is valid.
     *
     * @return True if the data is valid; false otherwise.
     */
    protected final boolean valid() {
        final ValidationResult vr = _view.getValidationResult();
        if (!vr.isValid()) {
            _view.alert(vr.getErrorText());
        }
        return vr.isValid();
    }


    /**
     * Extract data from the view into a DTO.
     *
     * @param dto The DTO that will receive the view's data.
     */
    protected final void unbind(final UserDto dto) {
        dto.setEmail(_view.getEmail());
        dto.setUsername(new Username(_view.getUsername()));
        dto.setName(_view.getName());
        dto.setPassword(_view.getPassword1());
        dto.setRoles(_view.getGroups());
    }



    /** {@inheritDoc} */
    @Override
    public void onCreate(final UserCreated event) {
        dispose();
    }


}
