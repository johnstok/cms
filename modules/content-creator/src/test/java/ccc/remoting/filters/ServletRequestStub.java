package ccc.remoting.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A stub, implementing the {@link ServletRequest} interface.
 *
 * @author Civic Computing Ltd.
 */
public final class ServletRequestStub
    implements
        HttpServletRequest {

    private String _contextPath;
    private String _servletPath;
    private String _pathInfo;
    private Map<String, String> _queryParams;

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
                              final Map<String, String> queryParams) {
        _contextPath = contextPath;
        _servletPath = servletPath;
        _pathInfo = pathInfo;
        _queryParams = queryParams;
    }

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getContextPath() {
        return _contextPath;
    }

    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public long getDateHeader(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getHeader(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Enumeration getHeaderNames() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Enumeration getHeaders(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public int getIntHeader(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getMethod() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getPathInfo() {
        return _pathInfo;
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getQueryString() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getServletPath() {
        return _servletPath;
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public HttpSession getSession(final boolean create) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public boolean isUserInRole(final String role) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Object getAttribute(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public Enumeration getLocales() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getParameter(final String name) {
        return _queryParams.get(name);
    }

    @Override
    public Map getParameterMap() {
        return new HashMap<String, String>(_queryParams);
    }

    @Override
    public Enumeration getParameterNames() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String[] getParameterValues(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getRealPath(final String path) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(final String path) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void removeAttribute(final String name) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void setAttribute(final String name, final Object o) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public void setCharacterEncoding(final String env) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}