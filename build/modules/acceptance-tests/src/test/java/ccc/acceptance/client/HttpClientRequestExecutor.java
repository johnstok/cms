package ccc.acceptance.client;

import static ccc.api.types.HttpStatusCode.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import ccc.api.types.MimeType;
import ccc.client.core.Request;
import ccc.client.core.RequestExecutor;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandler;

/**
 * Implementation of the {@link RequestExecutor} API using http-client.
 *
 * @author Civic Computing Ltd.
 */
public class HttpClientRequestExecutor
    implements
        RequestExecutor {

    private final String _hostUrl;
    private final HttpClient _client;


    /**
     * Constructor.
     *
     * @param client The HTTP client to use.
     * @param hostUrl The base URL for HTTP requests.
     */
    public HttpClientRequestExecutor(final HttpClient client,
                                     final String hostUrl) {
        _client  = client;
        _hostUrl = hostUrl;
    }


    /** {@inheritDoc} */
    @Override
    public void invokeRequest(final Request request) {

        final ResponseHandler handler = request.getCallback();

        final HttpMethod method = createMethod(request);
        method.setRequestHeader("Content-Type", MimeType.JSON.toString());
        method.setRequestHeader("Accept", MimeType.JSON.toString());
        try {
            _client.executeMethod(method);
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
                final PostMethod pm =
                    new PostMethod(_hostUrl+request.getPath());
                try {
                    pm.setRequestEntity(
                        new ByteArrayRequestEntity(
                            request.getBody().getBytes("UTF-8")));
                } catch (final UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                return pm;
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
