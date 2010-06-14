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
package ccc.client.gwt.views.gxt;

import java.util.Date;
import java.util.Map;

import ccc.api.types.CommandType;
import ccc.client.gwt.core.Editable;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.I18n;
import ccc.client.gwt.core.ValidationResult;
import ccc.client.gwt.i18n.UIConstants;
import ccc.client.gwt.views.CreateAction;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.client.gwt.widgets.CreateActionPanel;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;


/**
 * Dialog for creating a new scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionDialog
    extends
        AbstractWizardDialog
    implements
        CreateAction {

    private final CreateActionPanel _createAction = new CreateActionPanel();
    private final DateTimePicker _dtPicker = new DateTimePicker();

    private Editable _presenter;

    /**
     * Constructor.
     *
     */
    public CreateActionDialog() {
        super(I18n.UI_CONSTANTS.createAction(),
              new GlobalsImpl());


        addCard(_createAction);
        addCard(_dtPicker);

        refresh();
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override
            public void componentSelected(final ButtonEvent ce) {
                getPresenter().save();
            }
        };
    }

    /**
     * A UI control for specifying a date and time.
     *
     * @author Civic Computing Ltd.
     */
    private static class DateTimePicker extends LayoutContainer {
        private final DateField _date = new DateField();
        private final TimeField _time = new TimeField();
        private static final UIConstants UICONSTANTS = I18n.UI_CONSTANTS;

        DateTimePicker() {
            setLayout(new FormLayout());
            _date.setFieldLabel(UICONSTANTS.date());
            _date.setEditable(false);
            add(_date, new FormData("95%"));
            _time.setFieldLabel(UICONSTANTS.time());
            _time.setEditable(false);
            add(_time, new FormData("95%"));
        }

        /**
         * Accessor.
         *
         * @return The date currently specified by the control.
         */
        @SuppressWarnings("deprecation") // Calendar class not supported by GWT
        public Date getDate() {
            final Date d = _date.getValue();
            if (null==d) {
                return null;
            }
            final Time t = _time.getValue();
            if (null==t) {
                return null;
            }

            d.setHours(t.getHour());
            d.setMinutes(t.getMinutes());
            return new Date(d.getTime());
        }
    }

    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }

    /** {@inheritDoc} */
    @Override
    public void alert(final String message) {
        ContentCreator.WINDOW.alert(message);
    }

    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }

    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();

        if (null==_createAction.commandType()) {
            result.addError(getUiConstants().pleaseChooseAnAction());
        }
        if (null==_dtPicker.getDate()) {
            result.addError(getUiConstants().pleaseSpecifyDateAndTime());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> getActionParameters() {
        return _createAction.getParameters();
    }

    /** {@inheritDoc} */
    @Override
    public CommandType getCommandType() {
        return _createAction.commandType();
    }

    /** {@inheritDoc} */
    @Override
    public Date getDate() {
        return _dtPicker.getDate();
    }
}
