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
package ccc.view.contentcreator.commands;

import java.util.List;

import ccc.view.contentcreator.client.Constants;
import ccc.view.contentcreator.client.ResourceService;
import ccc.view.contentcreator.client.ResourceServiceAsync;
import ccc.view.contentcreator.dialogs.UpdateOptionsDialog;
import ccc.view.contentcreator.dto.TemplateDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class UpdateOptionsCommand implements Command {

    private final Constants _constants = GWT.create(Constants.class);
    private final ResourceServiceAsync _resourceService =
        (ResourceServiceAsync) GWT.create(ResourceService.class);

    /**
     * {@inheritDoc}
     */
    public void execute() {

        _resourceService.listTemplates(
            new AsyncCallback<List<TemplateDTO>>(){

                public void onFailure(final Throwable arg0) {
                    Window.alert(_constants.error());
                }

                public void onSuccess(final List<TemplateDTO> templates) {
                    new UpdateOptionsDialog(templates).center();
                }});

    }

}
