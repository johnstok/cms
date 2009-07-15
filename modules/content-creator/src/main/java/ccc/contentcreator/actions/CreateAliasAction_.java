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

import ccc.api.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasAction_
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
    public CreateAliasAction_(final ID parentId,
                              final String aliasName,
                              final ID targetId) {
        super(UI_CONSTANTS.createAlias(), RequestBuilder.POST);
        _parentId = parentId;
        _aliasName = aliasName;
        _targetId = targetId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() { // FIXME: Encoding!!
        return
            "/aliases"
            + "?id="+_parentId
            + "&n="+_aliasName
            + "&g="+_targetId;
    }
}
