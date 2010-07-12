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
package ccc.commons;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.activation.MimeTypeParseException;
import javax.activation.MimetypesFileTypeMap;

import org.apache.log4j.Logger;

import ccc.api.exceptions.CCException;
import ccc.api.types.DBC;
import ccc.api.types.HttpStatusCode;
import ccc.api.types.MimeType;


/**
 * A simple library for making HTTP requests.
 *
 * @author Civic Computing Ltd.
 */
public final class HTTP {
    private static final Logger LOG = Logger.getLogger(HTTP.class);
    private static final int FIVE_SECONDS = 5*1000;

    private HTTP() { super(); }


    /**
     * Perform an HTTP GET.
     *
     * @param host The host to GET from.
     * @param params The request query parameters.
     *
     * @return The response body as a string.
     */
    public static String get(final String host,
                             final Map<String, String> params) {

        HttpURLConnection connection = null;

        try {

            final URI serverAddress = new URI(host+'?'+query(params));

            connection =
                (HttpURLConnection) serverAddress.toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(FIVE_SECONDS);
            connection.connect();

            if (HttpStatusCode.OK==connection.getResponseCode()) {
                final String response =
                    Resources.readIntoString(
                        connection.getInputStream(), Resources.UTF8);

                return response;
            }

            return
                connection.getResponseCode()
                + " " + connection.getResponseMessage();

        // FIXME Auto-generated catch block.
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (final ProtocolException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            if (null!=connection) { connection.disconnect(); }
        }
    }


    /**
     * Perform an HTTP POST.
     *
     * @param host The host to POST to.
     * @param body The request body.
     * @param mimeType The body's mime-type.
     *
     * @return The response body as a string.
     */
    public static String post(final String host,
                              final String body,
                              final MimeType mimeType) {
        HttpURLConnection connection = null;

        try {
            final URI serverAddress = new URI(host);

            connection =
                (HttpURLConnection) serverAddress.toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", mimeType.toString());
            connection.setDoOutput(true);
            connection.setReadTimeout(FIVE_SECONDS);
            connection.connect();

            write(body, connection.getOutputStream());

            if (HttpStatusCode.OK==connection.getResponseCode()) {
                final String response =
                    Resources.readIntoString(
                        connection.getInputStream(), Resources.UTF8);

                return response;
            }

            return
                connection.getResponseCode()
                + " " + connection.getResponseMessage();

        // FIXME Auto-generated catch block.
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (final ProtocolException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            if (null!=connection) { connection.disconnect(); }
        }
    }


    /**
     * Write a string to an output stream.
     *
     * @param string The string to write.
     * @param os The output stream to write to.
     *
     * @throws IOException If the write fails.
     */
    private static void write(final String string,
                              final OutputStream os) throws IOException {
        final OutputStreamWriter wr =
            new OutputStreamWriter(os, Resources.UTF8);
        wr.write(string);
        wr.flush();
        wr.close();
    }

    /**
     * Create a valid query string.
     *
     * @param params A map of query parameters.
     *
     * @throws UnsupportedEncodingException If the UTF-8 encoding is not
     *  available.
     *
     * @return An encoded HTTP query string.
     */
    private static String query(final Map<String, String> params)
    throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> param : params.entrySet()) {
            sb.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            sb.append('=');
            sb.append(URLEncoder.encode(param.getValue(), "UTF-8"));
            sb.append('&');
        }
        return sb.toString();
    }


    /**
     * Encode a string using the <code>application/x-www-form-urlencoded</code>
     * format.
     *
     * @param string The string to encode.
     * @param charset The charset used to generate encoded values.
     *
     * @return The encoded string.
     */
    public static String encode(final String string, final String charset) {
        try {
            return URLEncoder.encode(string, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new CCException("Invalid charset.", e);
        }
    }


    /**
     * Decode a string encoded with the
     * <code>application/x-www-form-urlencoded</code> format.
     *
     * @param string The string to decode.
     * @param charset The charset used to interpret encoded values.
     *
     * @return The decoded string.
     */
    public static String decode(final String string, final String charset) {
        try {
            return URLDecoder.decode(string, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new CCException("Invalid charset.", e);
        }
    }

    /**
     * Guess the mime type of a file from its name.
     *
     * @param filename The name of the file.
     *
     * @return The corresponding mime type.
     */
    public static MimeType determineMimetype(final String filename) {
        DBC.require().notEmpty(filename);

        try {
            final MimetypesFileTypeMap typeMap =
                new MimetypesFileTypeMap(Resources.open("ccc7mime.types"));
            final javax.activation.MimeType mt =
                new javax.activation.MimeType(typeMap.getContentType(filename));
            final String primary = mt.getPrimaryType();
            final String sub     = mt.getSubType();
            return new MimeType(primary, sub);
        } catch (final MimeTypeParseException e) {
            LOG.warn("Couldn't determine mime-type for file: " + filename, e);
            return MimeType.BINARY_DATA;
        }
    }
}
