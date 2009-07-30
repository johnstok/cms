/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.api.Duration;
import ccc.contentcreator.actions.UpdateCacheDurationAction_;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.http.client.Response;


/**
 * Dialog for editing resource's caching setting.
 *
 * @author Civic Computing Ltd.
 */
public class EditCacheDialog extends AbstractEditDialog {

    private static final int DIALOG_HEIGHT = 240;
    private static final int DIALOG_WIDTH = 375;
    private final ResourceSummaryModelData _item;
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
    public EditCacheDialog(final ResourceSummaryModelData item,
                           final Duration ds) {
        super(new IGlobalsImpl().uiConstants().editCacheDuration(),
              new IGlobalsImpl());
        _item = item;
        setHeight(DIALOG_HEIGHT);
        setWidth(DIALOG_WIDTH);

        _useDefault.setBoxLabel(_constants.yes());
        _useDefault.addListener(Events.Change, checkBoxListener());

        _cbg.setFieldLabel(_constants.useDefaultSetting());
        _cbg.add(_useDefault);
        _panel.add(_cbg);

        _days.setFieldLabel(_constants.days());
        _days.setId("cacheDurationDays");
        _hours.setFieldLabel(_constants.hours());
        _hours.setId("cacheDurationHours");
        _minutes.setFieldLabel(_constants.minutes());
        _minutes.setId("cacheDurationMinutes");
        _seconds.setFieldLabel(_constants.seconds());
        _seconds.setId("cacheDurationSeconds");

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

        _panel.add(_days);
        _panel.add(_hours);
        _panel.add(_minutes);
        _panel.add(_seconds);
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
                Validate.callTo(updateCache())
                .check(Validations.emptyOrNumber(_days))
                .check(Validations.emptyOrNumber(_hours))
                .check(Validations.emptyOrNumber(_minutes))
                .check(Validations.emptyOrNumber(_seconds))
                .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable updateCache() {
        return new Runnable() {
            public void run() {
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

                new UpdateCacheDurationAction_(
                    _item.getId(),
                    updatedDs
                ){
                    @Override protected void onNoContent(final Response response) {
                        hide();
                    }
                }.execute();
            }
        };
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
