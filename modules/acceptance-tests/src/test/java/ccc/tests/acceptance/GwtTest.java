/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
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

import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.RequestExecutor;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.core.ResponseHandler;
import ccc.client.gwt.events.Error;
import ccc.client.gwt.i18n.ActionNameConstants;
import ccc.client.gwt.remoting.GetServicesAction;
import ccc.client.gwt.remoting.TextParser;
import ccc.client.gwt.widgets.ContentCreator;
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

        GlobalsImpl.setUserActions(Testing.stub(ActionNameConstants.class));

        ContentCreator.EVENT_BUS.addHandler(
            Error.TYPE,
            new Error.ErrorHandler() {
                /** {@inheritDoc} */
                @Override public void onError(final Error event) {
                    throw new RuntimeException(
                        "Test failed.", event.getException());
                }
            });

        new GetServicesAction()
            .setExecutor(new HttpClientRequestExecutor())
            .setParser(new ServerTextParser())
            .execute();

        assertEquals("/secure/aliases", GlobalsImpl.getAPI().aliases());
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
