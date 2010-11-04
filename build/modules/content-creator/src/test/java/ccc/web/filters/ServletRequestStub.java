package ccc.web.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A stub, implementing the {@link HttpServletRequest} interface.
 *
 * @author Civic Computing Ltd.
 */
public final class ServletRequestStub
    implements
        HttpServletRequest {

    private String _contextPath;
    private String _servletPath;
    private String _pathInfo;
    private String _queryString;
    private String _contentType;
    private byte[] _entity;
    private String _charset;
    private Map<String, String[]> _queryParams;
    private final Map<String, Object> _attributes =
        new HashMap<String, Object>();


    /**
     * Constructor.
     *
     * @param contextPath The request's context path.
     * @param servletPath The request's servlet path.
     * @param pathInfo The request's path info.
     * @param queryParams Query parameter for the the request.
     */
    public ServletRequestStub(final String contextPath,
                              final String servletPath,
                              final String pathInfo,
                              final Map<String, String[]> queryParams) {
        _contextPath = contextPath;
        _servletPath = servletPath;
        _pathInfo = pathInfo;
        _queryParams = queryParams;
    }


    /**
     * Constructor.
     *
     * @param contextPath The request's context path.
     * @param servletPath The request's servlet path.
     * @param pathInfo The request's path info.
     */
    public ServletRequestStub(final String contextPath,
                              final String servletPath,
                              final String pathInfo) {
        this(
            contextPath,
            servletPath,
            pathInfo,
            new HashMap<String, String[]>());
    }

    /** {@inheritDoc} */
    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getContextPath() {
        return _contextPath;
    }

    /** {@inheritDoc} */
    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public long getDateHeader(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getHeader(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Enumeration<String> getHeaderNames() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Enumeration<String> getHeaders(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public int getIntHeader(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getMethod() {
        return "GET";
    }

    /** {@inheritDoc} */
    @Override
    public String getPathInfo() {
        return _pathInfo;
    }

    /** {@inheritDoc} */
    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getQueryString() {
        return _queryString;
    }

    /** {@inheritDoc} */
    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getServletPath() {
        return _servletPath;
    }

    /** {@inheritDoc} */
    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public HttpSession getSession(final boolean create) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isUserInRole(final String role) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Object getAttribute(final String name) {
        return _attributes.get(name);
    }

    /** {@inheritDoc} */
    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getCharacterEncoding() {
        return _charset;
    }

    /** {@inheritDoc} */
    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getContentType() {
        return _contentType;
    }

    /** {@inheritDoc} */
    @Override
    public ServletInputStream getInputStream() {
        return (null==_entity) ? null : new ServletInputStream() {

            private ByteArrayInputStream _delegate =
                new ByteArrayInputStream(_entity);

            @Override
            public int read() {
                return _delegate.read();
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getParameter(final String name) {
        return _queryParams.get(name)[0];
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String[]> getParameterMap() {
        return new HashMap<String, String[]>(_queryParams);
    }

    /** {@inheritDoc} */
    @Override
    public Enumeration<String> getParameterNames() {
        return
            new CharsetConvertingServletRequest.IteratorEnumeration(
                _queryParams.keySet().iterator());
    }

    /** {@inheritDoc} */
    @Override
    public String[] getParameterValues(final String name) {
        return _queryParams.get(name);
    }

    /** {@inheritDoc} */
    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public BufferedReader getReader() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getRealPath(final String path) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public RequestDispatcher getRequestDispatcher(final String path) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void removeAttribute(final String name) {
        _attributes.remove(name);
    }

    /** {@inheritDoc} */
    @Override
    public void setAttribute(final String name, final Object o) {
        _attributes.put(name, o);
    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterEncoding(final String charset) {
        _charset = charset;
    }


    /**
     * Mutator.
     *
     * @param queryString The queryString to set.
     */
    public void setQueryString(final String queryString) {
        _queryString = queryString;
    }


    /**
     * Mutator.
     *
     * @param contentType The contentType to set.
     */
    public void setContentType(final String contentType) {
        _contentType = contentType;
    }


    /**
     * Mutator.
     *
     * @param entity The entity to set.
     */
    public void setEntity(final byte[] entity) {
        _entity = entity;
    }
}
