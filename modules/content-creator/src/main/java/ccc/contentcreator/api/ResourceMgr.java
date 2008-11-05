/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.api;

import java.util.List;


import com.google.gwt.json.client.JSONValue;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceMgr {


    /**
     * TODO: Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    public class NoOpAction<T>
        implements
            Action<JSONValue> {

        /** {@inheritDoc} */
        public void execute(final JSONValue jsonValue) { /* No Op */ }

    }

    /**
     * List all locked resources.
     *
     * @param action The action to perform on the result.
     */
    public void locked(final Action<List<JsonModelData>> action) {
        REST.get("api/resources/locked", new Action<JSONValue>(){
            public void execute(final JSONValue jsonValue) {
                action.execute(JsonModelData.fromArray(jsonValue));
            }
        });
    }

    /**
     * Lock a resource.
     */
    public void lock(final String resourceId) {
        // TODO: require().notNull(resourceId);
        REST.get("api/resources/lock/"+resourceId, new NoOpAction<JSONValue>());
    }

    /**
     * Unlock a resource.
     */
    public void unlock(final String resourceId) {
        // TODO: require().notNull(resourceId);
        REST.get(
            "api/resources/unlock/"+resourceId,
            new NoOpAction<JSONValue>());
    }
}
