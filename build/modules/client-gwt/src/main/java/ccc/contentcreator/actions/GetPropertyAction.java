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



/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GetPropertyAction
    extends
        RemotingAction {

    private final String _key;


    /**
     * Constructor.
     *
     * @param key The name of the property to look up.
     */
    public GetPropertyAction(final String key) {
        super(USER_ACTIONS.readProperty());
        _key = key;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/properties?key="+_key;
    }
}
