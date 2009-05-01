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

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.services.api.DurationSummary;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class EditCacheDialog extends AbstractEditDialog {

    private final ModelData _item;
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
    public EditCacheDialog(final ModelData item, final DurationSummary ds) {
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
            _days.setValue(""+ds._days);
            _hours.setValue(""+ds._hours);
            _minutes.setValue(""+ds._minutes);
            _seconds.setValue(""+ds._seconds);
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
            @SuppressWarnings("unchecked")
            public void run() {
                final DurationSummary updatedDs = new DurationSummary();
                boolean isDurationSet = false;
                if (_days.getValue() != null
                        && !_days.getValue().trim().equals("")) {
                    isDurationSet = true;
                    updatedDs._days = Long.parseLong(_days.getValue());
                }
                if (_hours.getValue() != null
                        && !_hours.getValue().trim().equals("")) {
                    isDurationSet = true;
                    updatedDs._hours = Long.parseLong(_hours.getValue());
                }
                if (_minutes.getValue() != null
                        && !_minutes.getValue().trim().equals("")) {
                    isDurationSet = true;
                    updatedDs._minutes = Long.parseLong(_minutes.getValue());
                }
                if (_seconds.getValue() != null
                        && !_seconds.getValue().trim().equals("")) {
                    isDurationSet = true;
                    updatedDs._seconds = Long.parseLong(_seconds.getValue());
                }

                commands().updateCacheDuration(
                    _item.<String>get("id"),
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
