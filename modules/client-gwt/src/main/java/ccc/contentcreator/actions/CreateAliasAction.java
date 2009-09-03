/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 1744 $
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2009-08-28 16:17:04 +0100 (Fri, 28 Aug 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasAction
    extends
        RemotingAction {

    private final UUID _parentId;
    private final String _aliasName;
    private final UUID _targetId;

    /**
     * Constructor.
     *
     * @param targetId The id of the target resource.
     * @param aliasName The name of the alias.
     * @param parentId The id of the alias' parent folder.
     */
    public CreateAliasAction(final UUID parentId,
                              final String aliasName,
                              final UUID targetId) {
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
        json.set(JsonKeys.TARGET_ID, _targetId);
        return json.toString();
    }
}
