package ccc.contentcreator.core;

import static ccc.api.types.HttpStatusCode.*;
import ccc.api.types.DBC;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

/**
 * Adapter that maps GWT's callback handler to the CC response handler.
 *
 * @author Civic Computing Ltd.
 */
public final class RequestCallbackAdapter
    implements
        RequestCallback {

    private final ResponseHandler _handler;

    /**
     * Constructor.
     *
     * @param handler The response handler for this callback.
     */
    public RequestCallbackAdapter(final ResponseHandler handler) {
        _handler = DBC.require().notNull(handler);
    }

    public void onError(final Request request,
                        final Throwable exception) {
        _handler.onFailed(exception);
    }

    public void onResponseReceived(final Request request,
                                   final Response response) {

        if (SessionTimeoutException.isTimedout(response.getText())) {
            _handler.onSessionTimeout(response);
            return;
        }

        selectHandlerForStatusCode(response);
    }

    private void selectHandlerForStatusCode(final Response response) {
        switch (response.getStatusCode()) {
            case OK:
                _handler.onOK(response);
                break;

            case NO_CONTENT:
            case MS_IE6_1223: // IE bug
                _handler.onNoContent(response);
                break;

            case NOT_FOUND:
                _handler.onNotFound(response);
                break;

            case ERROR:
                _handler.onError(response);
                break;

            case UNAUTHORIZED:
                _handler.onUnauthorized(response);
                break;

            case BAD_REQUEST:
                _handler.onBadRequest(response);
                break;

            case CONFLICT:
                _handler.onConflict(response);
                break;

            default:
                _handler.onUnsupported(response);
        }
    }
}