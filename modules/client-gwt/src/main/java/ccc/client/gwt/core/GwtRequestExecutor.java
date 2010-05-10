/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Revision      $Rev: 2636 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-04-09 14:56:56 +0100 (Fri, 09 Apr 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestBuilder.Method;


/**
 * GWT implementation of the {@link RequestExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
public class GwtRequestExecutor
    implements
        RequestExecutor {


    /** {@inheritDoc} */
    @Override
    public void invokeRequest(final Request request) {

        final ResponseHandler handler = request.getCallback();

        final String url = new GlobalsImpl().appURL() + request.getPath();
        final RequestBuilder builder =
            new RequestBuilder(getMethod(request.getMethod()), url);
        builder.setHeader("Accept", "application/json");
        if (HttpMethod.POST.equals(request.getMethod())) {
            builder.setHeader("Content-Type", "application/json");
            builder.setRequestData(request.getBody());
        }
        builder.setCallback(new RequestCallbackAdapter(handler));

        try {
            builder.send();
            GWT.log("Sent request: "+request.getMethod()+" "+url, null);
        } catch (final RequestException e) {
            handler.onFailed(e);
        }
    }


    private Method getMethod(final HttpMethod method) {
        switch (method) {
            case GET:
                return RequestBuilder.GET;
            case POST:
                return RequestBuilder.POST;
            default:
                throw new IllegalArgumentException(
                    "Unsupported HTTP method: "+method);
        }
    }
}
