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

import static ccc.client.core.InternalServices.validator;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.Duration;
import ccc.client.actions.UpdateCacheDurationAction;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for editing resource's caching setting.
 *
 * @author Civic Computing Ltd.
 */
public class EditCacheDialog extends AbstractEditDialog {

    private static final int DIALOG_HEIGHT = 240;
    private static final int DIALOG_WIDTH = 375;
    private final ResourceSummary _item;
    private final TextField<String> _seconds = new TextField<String>();
    private final TextField<String> _minutes = new TextField<String>();
    private final TextField<String> _hours = new TextField<String>();
    private final TextField<String> _days = new TextField<String>();
    private final CheckBox _useDefault = new CheckBox();
    private final CheckBoxGroup _cbg = new CheckBoxGroup();


    /**
     * Constructor.
     *
     * @param item The resource to rename.
     * @param ds The Duration summary of the resource.
     */
    public EditCacheDialog(final ResourceSummary item,
                           final Duration ds) {
        super(I18n.uiConstants.editCacheDuration(),
            InternalServices.globals);
        _item = item;
        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);

        _useDefault.setBoxLabel(getUiConstants().yes());
        _useDefault.addListener(Events.Change, checkBoxListener());

        _cbg.setFieldLabel(getUiConstants().useDefaultSetting());
        _cbg.add(_useDefault);
        getPanel().add(_cbg);

        _days.setFieldLabel(getUiConstants().days());
        _hours.setFieldLabel(getUiConstants().hours());
        _minutes.setFieldLabel(getUiConstants().minutes());
        _seconds.setFieldLabel(getUiConstants().seconds());

        if (ds != null) {
            _useDefault.setValue(Boolean.FALSE);
            _days.setValue(""+ds.dayField());
            _hours.setValue(""+ds.hourField());
            _minutes.setValue(""+ds.minuteField());
            _seconds.setValue(""+ds.secondField());
        } else {
            _useDefault.setValue(Boolean.TRUE);
        }
        changeFieldVisibility();

        getPanel().add(_days);
        getPanel().add(_hours);
        getPanel().add(_minutes);
        getPanel().add(_seconds);
    }

    private Listener<FieldEvent> checkBoxListener() {

        return new Listener<FieldEvent>() {
            @Override
            public void handleEvent(final FieldEvent be) {
                changeFieldVisibility();
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {

        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    validator.emptyOrNumber(
                        _days.getValue(), _days.getFieldLabel()));
                vr.addError(
                    validator.emptyOrNumber(
                        _hours.getValue(), _hours.getFieldLabel()));
                vr.addError(
                    validator.emptyOrNumber(
                        _minutes.getValue(), _minutes.getFieldLabel()));
                vr.addError(
                    validator.emptyOrNumber(
                        _seconds.getValue(), _seconds.getFieldLabel()));

                if (!vr.isValid()) {
                    InternalServices.window.alert(vr.getErrorText());
                    return;
                }

                updateCache();
            }
        };
    }

    private void updateCache() {
        boolean isDurationSet = false;
        long days = 0L;
        long hours = 0L;
        long minutes = 0L;
        long seconds = 0L;

        if (_days.getValue() != null
                && !_days.getValue().trim().equals("")) {
            isDurationSet = true;
            days = Long.parseLong(_days.getValue());
        }
        if (_hours.getValue() != null
                && !_hours.getValue().trim().equals("")) {
            isDurationSet = true;
            hours = Long.parseLong(_hours.getValue());
        }
        if (_minutes.getValue() != null
                && !_minutes.getValue().trim().equals("")) {
            isDurationSet = true;
            minutes = Long.parseLong(_minutes.getValue());
        }
        if (_seconds.getValue() != null
                && !_seconds.getValue().trim().equals("")) {
            isDurationSet = true;
            seconds = Long.parseLong(_seconds.getValue());
        }

        Duration updatedDs = null;

        if (isDurationSet && !_useDefault.getValue().booleanValue()) {
            updatedDs = new Duration(days, hours, minutes, seconds);
        }

        final Resource r = new Resource();
        r.setId(_item.getId());
        r.setCacheDuration(updatedDs);
        r.addLink(Resource.Links.DURATION,
                  _item.duration().toString());

        new UpdateCacheDurationAction(r){
            @Override protected void onSuccess(final Void v) {
                hide();
            }
        }.execute();
    }

    /**
     * Disables fields based on useDefault value.
     *
     */
    private void changeFieldVisibility() {

        if (_useDefault.getValue().booleanValue()) {
            _hours.setValue("");
            _days.setValue("");
            _minutes.setValue("");
            _seconds.setValue("");

            _days.disable();
            _hours.disable();
            _minutes.disable();
            _seconds.disable();
        } else {
            _days.enable();
            _hours.enable();
            _minutes.enable();
            _seconds.enable();
        }
    }


}
