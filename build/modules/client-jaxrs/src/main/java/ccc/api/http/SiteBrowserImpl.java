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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;


/**
 * A simple HTTP implementation of the site browser API.
 *
 * @author Civic Computing Ltd.
 */
public class SiteBrowserImpl
    implements
        SiteBrowser {

    private final HttpClient _httpClient;
    private final String _hostUrl;
    private final String _previewUrl;
    private final String _previewTemplateUrl;


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
        _previewTemplateUrl = _hostUrl+"/ccc/previewtemplate";
    }


    /** {@inheritDoc} */
    @Override
    public String previewContent(final ResourceSummary rs, final boolean wc) {
        final GetMethod get =
            new GetMethod(
                _previewUrl
                + rs.getAbsolutePath()
                + ((wc) ? "?wc=" : ""));
        return invoke(get);
    }


    /** {@inheritDoc} */
    @Override
    public String previewContent(final Resource rs, final boolean wc) {
        final GetMethod get =
            new GetMethod(
                _previewUrl
                + rs.getAbsolutePath()
                + ((wc) ? "?wc=" : ""));
        return invoke(get);
    }


    /** {@inheritDoc} */
    @Override
    public String post(final ResourceSummary rs) {
        return postUrlEncoded(rs, new HashMap<String, String[]>());
    }


    /** {@inheritDoc} */
    @Override
    public String postUrlEncoded(final ResourceSummary rs,
                                 final Map<String, String[]> params) {
        /* This method deliberately elides charset values to replicate the
         * behaviour of a typical browser.                                    */

        final PostMethod post = new PostMethod(_hostUrl+rs.getAbsolutePath());
        post.setRequestHeader(
            "Content-Type", "application/x-www-form-urlencoded");

        final List<NameValuePair> qParams = new ArrayList<NameValuePair>();
        for (final Map.Entry<String, String[]> param : params.entrySet()) {
            for (final String value : param.getValue()) {
                final NameValuePair qParam =
                    new NameValuePair(param.getKey(), value);
                qParams.add(qParam);
            }
        }

        final StringBuilder buffer = createQueryString(qParams);
        post.setRequestEntity(new StringRequestEntity(buffer.toString()));

        return invoke(post);
    }


    /** {@inheritDoc} */
    @Override
    public String postMultipart(final ResourceSummary rs,
                                final Map<String, String> params) {
        /* This method deliberately elides charset values to replicate the
         * behaviour of a typical browser.                                    */

        final String boundary = UUID.randomUUID().toString().substring(0, 7);
        final String newLine  = "\r\n";

        final PostMethod post = new PostMethod(_hostUrl+rs.getAbsolutePath());
        post.setRequestHeader(
            "Content-Type", "multipart/form-data; boundary="+boundary);

        final StringBuilder buffer = new StringBuilder();
        buffer.append("Content-Type: multipart/form-data");
        buffer.append("; boundary=");
        buffer.append(boundary);
        buffer.append(newLine);
        buffer.append(newLine);
        for (final Map.Entry<String, String> param : params.entrySet()) {
            buffer.append("--");
            buffer.append(boundary);
            buffer.append(newLine);
            buffer.append("Content-Disposition: form-data; name=\"");
            buffer.append(param.getKey());
            buffer.append("\"");
            buffer.append(newLine);
            buffer.append(newLine);
            buffer.append(param.getValue());
            buffer.append(newLine);
        }
        buffer.append("--");
        buffer.append(boundary);
        buffer.append("--");
        buffer.append(newLine);

        post.setRequestEntity(new StringRequestEntity(buffer.toString()));

        return invoke(post);
    }


    /** {@inheritDoc} */
    @Override
    public String get(final String absolutePath) {
        return get(absolutePath, new HashMap<String, String[]>());
    }


    /** {@inheritDoc} */
    @Override
    public String get(final String absolutePath,
                      final Map<String, String[]> params) {
        final GetMethod get = new GetMethod(_hostUrl+absolutePath);
        final List<NameValuePair> qParams = new ArrayList<NameValuePair>();
        for (final Map.Entry<String, String[]> param : params.entrySet()) {
            for (final String value : param.getValue()) {
                final NameValuePair qParam =
                    new NameValuePair(param.getKey(), value);
                qParams.add(qParam);
            }
        }

        final StringBuilder buffer = createQueryString(qParams);
        get.setQueryString(buffer.toString());

        return invoke(get);
    }
    
    
    @Override
    public String previewTemplate(ResourceSummary rs, String body) {
        final PostMethod post =
            new PostMethod(_previewTemplateUrl+ rs.getAbsolutePath());
        post.addParameter("hiddenbody", body);
        return invoke(post);
    }


    private String invoke(final HttpMethod m) {
        try {
            _httpClient.executeMethod(m);
            final int status = m.getStatusCode();
            if (OK==status) {
                return m.getResponseBodyAsString();
            }
            throw new RuntimeException(
                status+": "+m.getResponseBodyAsString());
        } catch (final HttpException e) {
            throw new InternalError(); // FIXME: Report error.
        } catch (final IOException e) {
            throw new InternalError(); // FIXME: Report error.
        } finally {
            m.releaseConnection();
        }
    }


    private StringBuilder createQueryString(final List<NameValuePair> qParams) {
        final StringBuilder buffer = new StringBuilder();
        for (final NameValuePair pair : qParams) {
            try {
                buffer.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                buffer.append('=');
                buffer.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
                buffer.append('&');
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("UTF-8 encoding not available.", e);
            }
        }
        return buffer;
    }
}
