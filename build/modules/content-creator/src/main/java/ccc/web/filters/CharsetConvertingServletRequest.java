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
package ccc.web.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import ccc.api.types.DBC;
import ccc.commons.Resources;


/**
 * A servlet request wrapper that performance charset conversions.
 *
 * @author Civic Computing Ltd.
 */
public class CharsetConvertingServletRequest
    extends
        HttpServletRequestWrapper {


    /**
     * Enumeration backed by an Iterator.
     *
     * @author Civic Computing Ltd.
     */
    static final class IteratorEnumeration
        implements
            Enumeration<String> {

        private final Iterator<String> _iterator;


        /**
         * Constructor.
         *
         * @param iterator The iterator that backs the enumeration.
         */
        public IteratorEnumeration(final Iterator<String> iterator) {
            _iterator = DBC.require().notNull(iterator);
        }

        /** {@inheritDoc} */
        @Override
        public String nextElement() { return _iterator.next(); }

        /** {@inheritDoc} */
        @Override
        public boolean hasMoreElements() { return _iterator.hasNext(); }
    }


    private final Map<String, String[]> _params =
        new HashMap<String, String[]>();


    /**
     * Constructor.
     *
     * @param request The request to wrap.
     *
     * @throws ServletException If an error occurs while wrapping the exception.
     * @throws IOException      If an error occurs while reading the entity.
     */
    public CharsetConvertingServletRequest(final HttpServletRequest request)
                                                       throws ServletException,
                                                              IOException {
        super(request);

        try {
            final Map<String, List<String>> params =
                new HashMap<String, List<String>>();

            reparseQueryString(request.getQueryString(), "UTF-8", params);

            final String contentType = request.getContentType();
            if (isUrlEncoded(contentType)) {
                final String requestCharset = request.getCharacterEncoding();
                reparseQueryString(
                    Resources.readIntoString(
                        request.getInputStream(), Charset.forName("ASCII")),
                    (null==requestCharset) ? "UTF-8" : requestCharset,
                    params);
            }

            for (final Map.Entry<String, List<String>> p : params.entrySet()) {
                _params.put(
                    p.getKey(),
                    p.getValue().toArray(new String[p.getValue().size()]));
            }

        } catch (final UnsupportedEncodingException e) {
            throw
                new ServletException("Failed to recode request parameters.", e);
        }
    }


    private boolean isUrlEncoded(final String contentType) {
        return
            null!=contentType
            && contentType.startsWith("application/x-www-form-urlencoded");
    }


    private void reparseQueryString(final String queryString,
                                    final String charset,
                                    final Map<String, List<String>> params)
                                           throws UnsupportedEncodingException {

        if (null==queryString || 0==queryString.length()) { return; }

        final String[] paramStrings = queryString.split("&");
        for (final String paramString : paramStrings) {

            if (!paramString.matches("[^\\=]+\\=[^\\=]*")) { continue; }

            final String[] paramParts = paramString.split("=");
            if (2<paramParts.length || 0==paramParts[0].trim().length()) {
                continue;
                // log warning?
            }

            final String key = URLDecoder.decode(paramParts[0], charset);
            final String value =
                (1==paramParts.length)
                    ? ""
                    : URLDecoder.decode(paramParts[1], charset);

            List<String> values = params.get(key);
            if (values == null) {
                values = new ArrayList<String>();
                params.put(key, values);
            }
            values.add(value);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String getParameter(final String name) {
        final String[] values = _params.get(name);
        return (null==values || 0==values.length) ? null : values[0];
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(_params);
    }


    /** {@inheritDoc} */
    @Override
    public Enumeration<String> getParameterNames() {
        return new IteratorEnumeration(_params.keySet().iterator());
    }


    /** {@inheritDoc} */
    @Override
    public String[] getParameterValues(final String name) {
        return _params.get(name);
    }
}
