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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.web.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * A HTTP servlet request wrapper that supports the 'X-HTTP-Method-Override'
 * header defined by Google.
 *
 * @author Civic Computing Ltd.
 */
class XHttpServletRequest
    implements
        HttpServletRequest {

    private final HttpServletRequest _delegate;
    private final String _xHttpMethod;


    /**
     * Constructor.
     *
     * @param delegate The request implementation to delegate to.
     */
    XHttpServletRequest(final HttpServletRequest delegate) {
        _delegate = delegate;
        _xHttpMethod = delegate.getHeader("X-HTTP-Method-Override");
    }


    /** {@inheritDoc} */
    @Override
    public String getMethod() {
        if ("POST".equals(_delegate.getMethod())) { // Only correct for 'POST'.
            return _delegate.getMethod();
        } else if ("PUT".equals(_xHttpMethod)) {    // Correct 'PUT'.
            return "PUT";
        } else if ("DELETE".equals(_xHttpMethod)) { // Correct 'DELETE'.
            return "DELETE";
        }
        return _delegate.getMethod();
    }



    /** {@inheritDoc} */
    @Override
    public Object getAttribute(final String name) {
        return _delegate.getAttribute(name);
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getAttributeNames() {
        return _delegate.getAttributeNames();
    }


    /** {@inheritDoc} */
    @Override
    public String getAuthType() {
        return _delegate.getAuthType();
    }


    /** {@inheritDoc} */
    @Override
    public String getCharacterEncoding() {
        return _delegate.getCharacterEncoding();
    }


    /** {@inheritDoc} */
    @Override
    public int getContentLength() {
        return _delegate.getContentLength();
    }


    /** {@inheritDoc} */
    @Override
    public String getContentType() {
        return _delegate.getContentType();
    }


    /** {@inheritDoc} */
    @Override
    public String getContextPath() {
        return _delegate.getContextPath();
    }


    /** {@inheritDoc} */
    @Override
    public Cookie[] getCookies() {
        return _delegate.getCookies();
    }


    /** {@inheritDoc} */
    @Override
    public long getDateHeader(final String name) {
        return _delegate.getDateHeader(name);
    }


    /** {@inheritDoc} */
    @Override
    public String getHeader(final String name) {
        return _delegate.getHeader(name);
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getHeaderNames() {
        return _delegate.getHeaderNames();
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getHeaders(final String name) {
        return _delegate.getHeaders(name);
    }


    /** {@inheritDoc} */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return _delegate.getInputStream();
    }


    /** {@inheritDoc} */
    @Override
    public int getIntHeader(final String name) {
        return _delegate.getIntHeader(name);
    }


    /** {@inheritDoc} */
    @Override
    public String getLocalAddr() {
        return _delegate.getLocalAddr();
    }


    /** {@inheritDoc} */
    @Override
    public Locale getLocale() {
        return _delegate.getLocale();
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getLocales() {
        return _delegate.getLocales();
    }


    /** {@inheritDoc} */
    @Override
    public String getLocalName() {
        return _delegate.getLocalName();
    }


    /** {@inheritDoc} */
    @Override
    public int getLocalPort() {
        return _delegate.getLocalPort();
    }


    /** {@inheritDoc} */
    @Override
    public String getParameter(final String name) {
        return _delegate.getParameter(name);
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Map getParameterMap() {
        return _delegate.getParameterMap();
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getParameterNames() {
        return _delegate.getParameterNames();
    }


    /** {@inheritDoc} */
    @Override
    public String[] getParameterValues(final String name) {
        return _delegate.getParameterValues(name);
    }


    /** {@inheritDoc} */
    @Override
    public String getPathInfo() {
        return _delegate.getPathInfo();
    }


    /** {@inheritDoc} */
    @Override
    public String getPathTranslated() {
        return _delegate.getPathTranslated();
    }


    /** {@inheritDoc} */
    @Override
    public String getProtocol() {
        return _delegate.getProtocol();
    }


    /** {@inheritDoc} */
    @Override
    public String getQueryString() {
        return _delegate.getQueryString();
    }


    /** {@inheritDoc} */
    @Override
    public BufferedReader getReader() throws IOException {
        return _delegate.getReader();
    }


    /** {@inheritDoc} */
    @Override
    @Deprecated
    public String getRealPath(final String path) {
        return _delegate.getRealPath(path);
    }


    /** {@inheritDoc} */
    @Override
    public String getRemoteAddr() {
        return _delegate.getRemoteAddr();
    }


    /** {@inheritDoc} */
    @Override
    public String getRemoteHost() {
        return _delegate.getRemoteHost();
    }


    /** {@inheritDoc} */
    @Override
    public int getRemotePort() {
        return _delegate.getRemotePort();
    }


    /** {@inheritDoc} */
    @Override
    public String getRemoteUser() {
        return _delegate.getRemoteUser();
    }


    /** {@inheritDoc} */
    @Override
    public RequestDispatcher getRequestDispatcher(final String path) {
        return _delegate.getRequestDispatcher(path);
    }


    /** {@inheritDoc} */
    @Override
    public String getRequestedSessionId() {
        return _delegate.getRequestedSessionId();
    }


    /** {@inheritDoc} */
    @Override
    public String getRequestURI() {
        return _delegate.getRequestURI();
    }


    /** {@inheritDoc} */
    @Override
    public StringBuffer getRequestURL() {
        return _delegate.getRequestURL();
    }


    /** {@inheritDoc} */
    @Override
    public String getScheme() {
        return _delegate.getScheme();
    }


    /** {@inheritDoc} */
    @Override
    public String getServerName() {
        return _delegate.getServerName();
    }


    /** {@inheritDoc} */
    @Override
    public int getServerPort() {
        return _delegate.getServerPort();
    }


    /** {@inheritDoc} */
    @Override
    public String getServletPath() {
        return _delegate.getServletPath();
    }


    /** {@inheritDoc} */
    @Override
    public HttpSession getSession() {
        return _delegate.getSession();
    }


    /** {@inheritDoc} */
    @Override
    public HttpSession getSession(final boolean create) {
        return _delegate.getSession(create);
    }


    /** {@inheritDoc} */
    @Override
    public Principal getUserPrincipal() {
        return _delegate.getUserPrincipal();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return _delegate.isRequestedSessionIdFromCookie();
    }


    /** {@inheritDoc} */
    @Override
    @Deprecated
    public boolean isRequestedSessionIdFromUrl() {
        return _delegate.isRequestedSessionIdFromUrl();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return _delegate.isRequestedSessionIdFromURL();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isRequestedSessionIdValid() {
        return _delegate.isRequestedSessionIdValid();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isSecure() {
        return _delegate.isSecure();
    }


    /** {@inheritDoc} */
    @Override
    public boolean isUserInRole(final String role) {
        return _delegate.isUserInRole(role);
    }


    /** {@inheritDoc} */
    @Override
    public void removeAttribute(final String name) {
        _delegate.removeAttribute(name);
    }


    /** {@inheritDoc} */
    @Override
    public void setAttribute(final String name, final Object o) {
        _delegate.setAttribute(name, o);
    }


    /** {@inheritDoc} */
    @Override
    public void setCharacterEncoding(final String env)
    throws UnsupportedEncodingException {
        _delegate.setCharacterEncoding(env);
    }
}
