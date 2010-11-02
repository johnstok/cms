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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import ccc.api.types.DBC;
import ccc.web.TmpRenderer;


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


    private final Map<String, String[]> _params;


    /**
     * Constructor.
     *
     * @param request The request to wrap.
     *
     * @throws ServletException If an error occurs while wrapping the exception.
     */
    @SuppressWarnings("unchecked") // API doesn't support generics.
    public CharsetConvertingServletRequest(final HttpServletRequest request)
                                                       throws ServletException {
        super(request);

        try {
            final Map<String, String[]> params =
                new HashMap<String, String[]>();
            final Enumeration<String> paramNames =
                request.getParameterNames();

            while (paramNames.hasMoreElements()) {
                final String pName = paramNames.nextElement();
                final String[] pValues = request.getParameterValues(pName);
                params.put(
                    recode(pName, "iso-8859-1", TmpRenderer.DEFAULT_CHARSET),
                    recode(pValues, "iso-8859-1", TmpRenderer.DEFAULT_CHARSET));
            }

            _params = params;

        } catch (final UnsupportedEncodingException e) {
            throw
                new ServletException("Failed to recode request parameters.", e);
        }
    }


    private String[] recode(final String[] strings,
                            final String currentEncoding,
                            final String targetEncoding)
                                           throws UnsupportedEncodingException {
        if (null==strings) { return null; }

        final String[] recoded = new String[strings.length];
        for (int i=0; i<strings.length; i++) {
            recoded[i] =
                recode(strings[i], currentEncoding, targetEncoding);
        }

        return recoded;
    }


    private String recode(final String string,
                          final String currentEncoding,
                          final String targetEncoding)
                                           throws UnsupportedEncodingException {
        if (null==string) { return null; }

        final String unencoded = URLEncoder.encode(string, currentEncoding);

        return URLDecoder.decode(unencoded, targetEncoding);
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
