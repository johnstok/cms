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

import ccc.api.core.User;
import ccc.api.types.CommandType;
import ccc.api.types.Username;
import ccc.client.core.AbstractPresenter;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.events.Event;
import ccc.client.gwt.remoting.CreateUserAction;
import ccc.client.views.CreateUser;

/**
 * A controller for user creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreateUserPresenter
    extends
        AbstractPresenter<CreateUser, User>
    implements
        Editable {

//    private final UIMessages _messages;


    /**
     * Constructor.
     *
     * @param view The create user dialog this controller will manage.
     */
    public CreateUserPresenter(final CreateUser view) {
        super(view, new User());
//        _messages = I18n.UI_MESSAGES;
        render();
    }


    /**
     * Render this presenter.
     */
    protected final void render() {
        getView().show(this);
    }


    /** {@inheritDoc} */
    @Override
    public final void cancel() {
        dispose();
    }


//    /**
//     * Accessor.
//     *
//     * @return Returns the messages.
//     */
//    UIMessages getMessages() {
//        return _messages;
//    }


    /** {@inheritDoc} */
    @Override
    public void save() {
        if (valid()) {
            final User user = new User();
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
        final ValidationResult vr = getView().getValidationResult();
        if (!vr.isValid()) {
            getView().alert(vr.getErrorText());
        }
        return vr.isValid();
    }


    /**
     * Extract data from the view into a DTO.
     *
     * @param dto The DTO that will receive the view's data.
     */
    protected final void unbind(final User dto) {
        dto.setEmail(getView().getEmail());
        dto.setUsername(new Username(getView().getUsername()));
        dto.setName(getView().getName());
        dto.setPassword(getView().getPassword1());
        dto.setGroups(getView().getGroups());
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch(event.getType()) {
            case USER_CREATE:
                dispose();
                return;
            default:
                return;
        }
    }
}
