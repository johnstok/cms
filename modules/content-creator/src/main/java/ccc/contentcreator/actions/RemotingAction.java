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

import static com.google.gwt.http.client.Response.*;
import ccc.contentcreator.client.Action;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class RemotingAction
    implements
        Action {

    private final String _actionName;
    private final String _path;

    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     * @param path The server path for the resource.
     */
    public RemotingAction(final String actionName,
                          final String path) {
        _actionName = actionName;
        _path = path;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() {
    //        new ErrorReportingCallback<Collection<ResourceSummary>>(
    //            _globals.userActions().internalAction())

            final String url = GLOBALS.apiURL() + _path;
            final RequestBuilder builder =
                new RequestBuilder(RequestBuilder.GET, url);
            builder.setHeader("Accept", "application/json");
            try {
                builder.sendRequest(null, new RequestCallback() {

                    public void onError(final Request request,
                                        final Throwable exception) {
                        GLOBALS.unexpectedError(exception, _actionName);
                    }

                    public void onResponseReceived(final Request request,
                                                   final Response response) {
    //                    GLOBALS.alert(
    //                        response.getStatusCode()+" "+
    //                        response.getStatusText());
                        if (SC_OK == response.getStatusCode()) {
                            onOK(response);
                        } else {
                            GLOBALS.unexpectedError(
                                new RuntimeException("Invalid response"),
                                _actionName);
                        }
                    }
                });
            } catch (final RequestException e) {
                GLOBALS.unexpectedError(e, "Foo");
            }
        }

    /**
     * Accessor.
     *
     * @return Returns the actionName.
     */
    public final String getActionName() { return _actionName; }

    /**
     * Handle a '200 OK' response from the remote server.
     *
     * @param response The server response.
     */
    protected abstract void onOK(final Response response);

}