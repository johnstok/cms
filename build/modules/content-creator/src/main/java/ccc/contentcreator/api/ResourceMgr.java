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
    public void lock(final String resourceId, final Action<JSONValue> action) {
        // TODO: require().notNull(resourceId);
        REST.get("api/resources/lock/"+resourceId, action);
    }

    /**
     * Unlock a resource.
     */
    public void unlock(final String resourceId,
                       final Action<JSONValue> action) {
        // TODO: require().notNull(resourceId);
        REST.get("api/resources/unlock/"+resourceId, action);
    }

    /**
     * Retrieve a resource's history.
     *
     * @param action The action to perform on the result.
     * @param resourceId The id of the resource.
     */
    public void history(final String resourceId,
                       final Action<List<JsonModelData>> action) {
        REST.get("api/resources/history/"+resourceId, new Action<JSONValue>(){
            public void execute(final JSONValue jsonValue) {
                action.execute(JsonModelData.fromArray(jsonValue));
            }
        });
    }

    /**
     * Publish a resource.
     *
     * @param resourceId resourceId The id of the resource.
     * @param action The action to perform on the result.
     */
    public void publish(final String resourceId,
                        final Action<JSONValue> action) {
        REST.post("api/resources/publish/"+resourceId, action);
    }

    /**
     * Unpublish a resource.
     *
     * @param resourceId resourceId The id of the resource.
     * @param action The action to perform on the result.
     */
    public void unpublish(final String resourceId,
                        final Action<JSONValue> action) {
        REST.post("api/resources/unpublish/"+resourceId, action);
    }
}
