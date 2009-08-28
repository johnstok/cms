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
import ccc.serialization.JsonKeys;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasAction
    extends
        RemotingAction {

    private final ID _parentId;
    private final String _aliasName;
    private final ID _targetId;

    /**
     * Constructor.
     *
     * @param targetId The id of the target resource.
     * @param aliasName The name of the alias.
     * @param parentId The id of the alias' parent folder.
     */
    public CreateAliasAction(final ID parentId,
                              final String aliasName,
                              final ID targetId) {
        super(UI_CONSTANTS.createAlias(), RequestBuilder.POST);
        _parentId = parentId;
        _aliasName = aliasName;
        _targetId = targetId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/aliases";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.NAME, _aliasName);
        json.set(JsonKeys.TARGET, _targetId);
        return json.toString();
    }
}
