/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.commands;

import java.util.List;

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.Constants;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.dialogs.UpdateOptionsDialog;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.OptionDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class UpdateOptionsCommand extends ApplicationCommand {

    /**
     * Constructor.
     *
     * @param application
     */
    public UpdateOptionsCommand(final Application application) {

        super(application);
        // TODO Auto-generated constructor stub
    }

    private final ResourceServiceAsync _resourceService =
       _app.lookupService();

    /** {@inheritDoc} */
    public void execute() {

        _resourceService.listOptions(
            new AsyncCallback<List<OptionDTO<? extends DTO>>>(){

                private final Constants _constants =
                    GWT.create(Constants.class);

                public void onFailure(final Throwable arg0) {
                    Window.alert(_constants.error());
                }

                public void onSuccess(
                              final List<OptionDTO<? extends DTO>> options) {
                    new UpdateOptionsDialog(_app, options).center();
                }});

    }

}
