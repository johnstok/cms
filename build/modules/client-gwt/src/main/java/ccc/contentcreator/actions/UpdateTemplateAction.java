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

import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.TemplateDelta;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for template updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateTemplateAction
    extends
        RemotingAction {

    private final UUID _template;
    private final TemplateDelta _details;


    /**
     * Constructor.
     *
     * @param details The new details for the template.
     * @param template The template to update.
     */
    public UpdateTemplateAction(final UUID template,
                                 final TemplateDelta details) {
        super(UI_CONSTANTS.editTemplate(), RequestBuilder.POST);
        _template = template;
        _details = details;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/templates/"+_template;
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _details.toJson(json);
        return json.toString();
    }
}
