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

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.AliasDelta;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;



/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasAction
    extends
        RemotingAction {

    private final ID _alias;
    private final AliasDelta _details;


    /**
     * Constructor.
     * @param details The new alias details.
     * @param alias The alias to update.
     */
    public UpdateAliasAction(final ID alias, final AliasDelta details) {
        super(UI_CONSTANTS.updateAlias(), RequestBuilder.POST);
        _alias = alias;
        _details = details;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/aliases/"+_alias;
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _details.toJson(json);
        return json.toString();
    }
}
