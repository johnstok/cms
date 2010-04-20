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
package ccc.api.http;

import static ccc.api.types.HttpStatusCode.*;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import ccc.api.dto.ResourceSummary;
import ccc.api.types.DBC;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class SiteBrowserImpl
    implements
        SiteBrowser {

    private final HttpClient _httpClient;
    private final String _hostUrl;
    private final String _previewUrl;

    /**
     * Constructor.
     *
     * @param httpClient The HTTP client used to call the server.
     * @param hostUrl The server's base URL.
     */
    public SiteBrowserImpl(final HttpClient httpClient, final String hostUrl) {
        _httpClient = DBC.require().notNull(httpClient);
        _hostUrl = DBC.require().notEmpty(hostUrl);
        _previewUrl = _hostUrl+"/ccc/preview";

    }

    /** {@inheritDoc} */
    @Override
    public String previewContent(final ResourceSummary rs, final boolean wc) {
        final GetMethod get =
            new GetMethod(
                _previewUrl
                + rs.getAbsolutePath()
                + ((wc) ? "?wc=" : ""));
        try {
            _httpClient.executeMethod(get);
            final int status = get.getStatusCode();
            if (OK==status) {
                return get.getResponseBodyAsString();
            }
            throw new RuntimeException(
                status+": "+get.getResponseBodyAsString());
        } catch (final HttpException e) {
            throw new InternalError(); // FIXME: Report error.
        } catch (final IOException e) {
            throw new InternalError(); // FIXME: Report error.
        } finally {
            get.releaseConnection();
        }
    }

}
