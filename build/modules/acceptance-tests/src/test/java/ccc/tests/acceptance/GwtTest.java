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
import ccc.client.gwt.widgets.ContentCreator;
import ccc.commons.Testing;
import junit.framework.TestCase;


/**
 * Tests for the {@link GetServicesAction} class.
 *
 * @author Civic Computing Ltd.
 */
public class GwtTest extends TestCase {
    
    public void testServerCall() throws Exception {
        
        GlobalsImpl.setUserActions(Testing.stub(ActionNameConstants.class));
        
        ContentCreator.EVENT_BUS.addHandler(
            Error.TYPE, 
            new Error.ErrorHandler() {
                /** {@inheritDoc} */
                @Override public void onError(Error event) {
                    throw new RuntimeException(
                        "Test failed.", event.getException());
                }
            });
        
        new GetServicesAction()
            .setExecutor(new HttpClientRequestExecutor())
            .execute();
    }
    
    
    
    public static class HttpClientRequestExecutor
        implements
            RequestExecutor {
        
        private final String _hostUrl = "http://localhost:8080/cc7/ccc/api";
        
        
        /** {@inheritDoc} */
        @Override
        public void invokeRequest(Request request) {

            ResponseHandler handler = request.getCallback();
            
            HttpClient client = new HttpClient();
            HttpMethod method = createMethod(request);
            try {
                client.executeMethod(method);
                Response response =
                    new Response(
                        method.getResponseBodyAsString(), 
                        method.getStatusText(), 
                        method.getStatusCode());
                selectHandlerForStatusCode(response, handler);
            } catch (HttpException e) {
                handler.onFailed(e);
            } catch (IOException e) {
                handler.onFailed(e);
            } finally {
                method.releaseConnection();
            }
        }


        private HttpMethod createMethod(Request request) {
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
