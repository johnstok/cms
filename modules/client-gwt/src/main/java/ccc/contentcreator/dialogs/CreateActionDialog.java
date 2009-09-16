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

import java.util.Date;
import java.util.UUID;

import ccc.contentcreator.actions.CreateActionAction;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.client.CreateActionPanel;
import ccc.contentcreator.client.IGlobalsImpl;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.http.client.Response;


/**
 * Dialog for creating a new scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionDialog
    extends
        AbstractWizardDialog {

    private final CreateActionPanel _createAction = new CreateActionPanel();
    private final DateTimePicker _dtPicker = new DateTimePicker();

    private final UUID _resourceId;

    /**
     * Constructor.
     *
     * @param resourceId The UUID of the resource.
     */
    public CreateActionDialog(final UUID resourceId) {
        super(new IGlobalsImpl().uiConstants().createAction(),
              new IGlobalsImpl());

        _resourceId = resourceId;

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

                if (null==_createAction.commandType()) {
                    getUiConstants().pleaseChooseAnAction();
                    return;
                }
                if (null==_dtPicker.getDate()) {
                    getUiConstants().pleaseSpecifyDateAndTime();
                    return;
                }

                new CreateActionAction(
                    _resourceId,
                    _createAction.commandType(),
                    _dtPicker.getDate(),
                    _createAction.getParameters()) {
                        /** {@inheritDoc} */
                        @Override protected void onNoContent(final Response r) {
                            hide();
                        }
                }.execute();
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
        private static final UIConstants UICONSTANTS =
            new IGlobalsImpl().uiConstants();

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
}
