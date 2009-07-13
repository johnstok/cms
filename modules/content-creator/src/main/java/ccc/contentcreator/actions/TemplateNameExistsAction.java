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
package ccc.contentcreator.actions;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class TemplateNameExistsAction
    extends
        RemotingAction {

    private final String _name;

    /**
     * Constructor.
     *
     * @param name The name of the template.
     */
    public TemplateNameExistsAction(final String name) {
        super(USER_ACTIONS.checkUniqueTemplateName());
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/templates/"+_name+"/exists";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final boolean nameExists =
            JSONParser.parse(response.getText()).isBoolean().booleanValue();
        execute(nameExists);
    }

    protected abstract void execute(boolean nameExists);
}
