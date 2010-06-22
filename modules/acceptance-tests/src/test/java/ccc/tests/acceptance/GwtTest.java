/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.tests.acceptance;

import static ccc.api.types.HttpStatusCode.*;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import ccc.api.core.ResourceIdentifiers.Alias;
import ccc.client.core.CoreEvents;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Request;
import ccc.client.core.RequestExecutor;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandler;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.remoting.GetServicesAction;
import ccc.client.i18n.ActionNameConstants;
import ccc.client.remoting.TextParser;
import ccc.commons.Testing;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Tests for the {@link GetServicesAction} class.
 *
 * @author Civic Computing Ltd.
 */
public class GwtTest extends TestCase {

    /**
     * Test.
     */
    public void testGetServices() {

        I18n.USER_ACTIONS = Testing.stub(ActionNameConstants.class);

        InternalServices.CORE_BUS.registerHandler(
            new EventHandler<CoreEvents>() {
                @Override
                public void handle(final Event<CoreEvents> event) {
                    switch (event.getType()) {
                        case ERROR:
                            throw new RuntimeException(
                                "Test failed.",
                                event.<Throwable>getProperty("exception"));
                        default:
                            break;
                    }
                }
            });


        new GetServicesAction()
            .setExecutor(new HttpClientRequestExecutor())
            .setParser(new ServerTextParser())
            .execute();

        assertEquals(Alias.COLLECTION, GlobalsImpl.getAPI().aliases());
    }


    /**
     * Server implementation of the {@link TextParser} API.
     *
     * @author Civic Computing Ltd.
     */
    public static class ServerTextParser
        implements
            TextParser {

        /** {@inheritDoc} */
        @Override
        public Json parseJson(final String text) {
            return new JsonImpl(text);
        }
    }


    /**
     * Implementation of the {@link RequestExecutor} API using http-client.
     *
     * @author Civic Computing Ltd.
     */
    public static class HttpClientRequestExecutor
        implements
            RequestExecutor {

        private final String _hostUrl = "http://localhost:8080/cc7/ccc/";


        /** {@inheritDoc} */
        @Override
        public void invokeRequest(final Request request) {

            final ResponseHandler handler = request.getCallback();

            final HttpClient client = new HttpClient();
            final HttpMethod method = createMethod(request);
            try {
                client.executeMethod(method);
                final Response response =
                    new Response(
                        method.getResponseBodyAsString(),
                        method.getStatusText(),
                        method.getStatusCode());
                selectHandlerForStatusCode(response, handler);
            } catch (final HttpException e) {
                handler.onFailed(e);
            } catch (final IOException e) {
                handler.onFailed(e);
            } finally {
                method.releaseConnection();
            }
        }


        private HttpMethod createMethod(final Request request) {
            switch (request.getMethod()) {
                case GET:
                    return new GetMethod(_hostUrl+request.getPath());
                case POST:
                    return new PostMethod(_hostUrl+request.getPath());
                default:
                    throw new IllegalArgumentException(
                        "Method not supported: "+request.getMethod());
            }
        }


        private void selectHandlerForStatusCode(final Response response,
                                                final ResponseHandler handler) {
            switch (response.getStatusCode()) {
                case OK:
                    handler.onOK(response);
                    break;

                case NO_CONTENT:
                case MS_IE6_1223: // IE bug
                    handler.onNoContent(response);
                    break;

                case NOT_FOUND:
                    handler.onNotFound(response);
                    break;

                case ERROR:
                    handler.onError(response);
                    break;

                case UNAUTHORIZED:
                    handler.onUnauthorized(response);
                    break;

                case BAD_REQUEST:
                    handler.onBadRequest(response);
                    break;

                case CONFLICT:
                    handler.onConflict(response);
                    break;

                default:
                    handler.onUnsupported(response);
            }
        }
    }
}
