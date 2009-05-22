/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.remoting;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import ccc.commons.Exceptions;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;


/**
 * Helper class used to work around an serialization policy issues.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CCCRemoteServiceServlet extends RemoteServiceServlet {

    /** {@inheritDoc} */
    @Override
    protected SerializationPolicy doGetSerializationPolicy(
                                               final HttpServletRequest request,
                                               final String moduleBaseURL,
                                               final String strongName) {

        SerializationPolicy serializationPolicy = null;
        final String serializationPolicyFilePath = "";
        InputStream is = null;

//        final String contextPath = request.getContextPath();

        try {
            final URL baseURL =
                new URL(moduleBaseURL + strongName + ".gwt.rpc");
            final URLConnection baseURLConnection =
                baseURL.openConnection();
            is = baseURLConnection.getInputStream();
        } catch(final MalformedURLException ex) {
            final String message = "ERROR: Could not open policy file "
                + ex.toString();
            getServletContext().log(message);
            return serializationPolicy;
        } catch (final IOException e) {
            final String message = "ERROR: Could not open policy file "
                + e.toString();
            getServletContext().log(message);
            return serializationPolicy;
        }

        try {
            if (is != null) {
                try {
                    serializationPolicy =
                        SerializationPolicyLoader.loadFromStream(is, null);
                } catch (final ParseException e) {
                    getServletContext().log(
                        "ERROR: Failed to parse the policy file '"
                        + serializationPolicyFilePath + "'", e);
                } catch (final IOException e) {
                    getServletContext().log(
                        "ERROR: Could not read the policy file '"
                        + serializationPolicyFilePath + "'", e);
                }
            } else {
                final String message = "ERROR: The serialization policy file '"
                    + serializationPolicyFilePath
                    + "' was not found";
                getServletContext().log(message);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                    Exceptions.swallow(e);
                }
            }
        }
        return serializationPolicy;
    }
}
