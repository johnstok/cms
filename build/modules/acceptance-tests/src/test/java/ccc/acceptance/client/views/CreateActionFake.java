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

import java.util.Date;
import java.util.Map;

import ccc.api.types.CommandType;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.CreateAction;


/**
 * Fake implementation of the {@link CreateAction} view.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionFake implements CreateAction {

    private final Date _date;
    private final CommandType _commandType;
    private final Map<String, String> _actionParams;
    private Editable _presenter;
    private boolean _showing;
    private final ValidationResult _validationResult = new ValidationResult();


    /**
     * Constructor.
     *
     * @param date The date for action.
     * @param commandType The command type.
     * @param params The parameters.
     */
    public CreateActionFake(final Date date,
                            final CommandType commandType,
                            final Map<String, String> params) {
        _date = date;
        _commandType = commandType;
        _actionParams = params;
    }

    @Override
    public void alert(final String message) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Map<String, String> getActionParameters() {
        return _actionParams;
    }

    @Override
    public CommandType getCommandType() {
        return _commandType;
    }

    @Override
    public Date getDate() {
        return _date;
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
