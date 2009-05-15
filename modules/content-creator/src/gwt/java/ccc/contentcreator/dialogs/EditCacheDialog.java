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
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class EditCacheDialog extends AbstractEditDialog {

    private final ResourceSummaryModelData _item;
    private final TextField<String> _seconds = new TextField<String>();
    private final TextField<String> _minutes = new TextField<String>();
    private final TextField<String> _hours = new TextField<String>();
    private final TextField<String> _days = new TextField<String>();


    /**
     * Constructor.
     *
     * @param item The resource to rename.
     * @param ds The Duration summary of the resource.
     */
    public EditCacheDialog(final ResourceSummaryModelData item,
                           final Duration ds) {
        super(Globals.uiConstants().editCacheDuration());
        _item = item;
        setHeight(200);
        setWidth(350);
        _days.setFieldLabel(_constants.days());
        _days.setId("cacheDurationDays");
        _hours.setFieldLabel(_constants.hours());
        _hours.setId("cacheDurationHours");
        _minutes.setFieldLabel(_constants.minutes());
        _minutes.setId("cacheDurationMinutes");
        _seconds.setFieldLabel(_constants.seconds());
        _seconds.setId("cacheDurationSeconds");

        if (ds != null) {
            _days.setValue(""+ds.dayField());
            _hours.setValue(""+ds.hourField());
            _minutes.setValue(""+ds.minuteField());
            _seconds.setValue(""+ds.secondField());
        }
        _panel.add(_days);
        _panel.add(_hours);
        _panel.add(_minutes);
        _panel.add(_seconds);
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
                Long days = 0l;
                Long hours = 0l;
                Long minutes = 0l;
                Long seconds = 0l;

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

                final Duration updatedDs =
                    new Duration(days, hours, minutes, seconds);

                commands().updateCacheDuration(
                    _item.getId(),
                    (isDurationSet) ? updatedDs : null,
                    new ErrorReportingCallback<Void>() {
                    @Override
                    public void onSuccess(final Void arg0) {
                        close();
                    }
                }
                );
            }
        };
    }


}
