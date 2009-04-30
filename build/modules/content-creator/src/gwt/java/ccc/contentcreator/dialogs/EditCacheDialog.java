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
    private final TextField<String> _duration = new TextField<String>();

    /**
     * Constructor.
     *
     * @param item The resource to rename.
     */
    public EditCacheDialog(final ModelData item, final DurationSummary ds) {
        super(Globals.uiConstants().editCache());
        _item = item;
        setHeight(Globals.DEFAULT_MIN_HEIGHT);

        _duration.setFieldLabel(constants().cacheDuration());
        _duration.setId("cacheDuration");
        if (ds != null) {
            _duration.setValue(ds._days+"D"+ds._hours+"H"+ds._minutes+"M"+ds._seconds+"S");
        }
        addField(_duration);

    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {

        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                String duration = null;
                if (_duration.getValue() != null
                        && !_duration.getValue().trim().equals("")) {
                    duration = _duration.getValue();
                }
                commands().updateCacheDuration(
                    _item.<String>get("id"),
                    duration,
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
