/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.api.jaxrs.providers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimeTypeParseException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;

import ccc.api.core.File;
import ccc.api.exceptions.InvalidException;
import ccc.api.types.DBC;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.commons.Strings;
import ccc.plugins.PluginFactory;
import ccc.plugins.multipart.MultipartFormData;


/**
 * A {@link MessageBodyReader} for durations.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("multipart/form-data")
@Produces("multipart/form-data")
public class FileProvider
    extends
        AbstractProvider
    implements
        MessageBodyWriter<File>,
        MessageBodyReader<File> {

    private static final Logger LOG = Logger.getLogger(FileProvider.class);

    private static final String FILE        = "file";
    private static final String FILE_NAME   = "fileName";
    private static final String TITLE       = "title";
    private static final String DESCRIPTION = "description";
    private static final String PATH        = "path";
    private static final String LAST_UPDATE = "lastUpdate";
    private static final String COMMENT     = "comment";
    private static final String MAJOR_EDIT  = "majorEdit";


    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return File.class.equals(clazz);
    }


    /** {@inheritDoc} */
    @Override
    public File readFrom(final Class<File> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {

        try {
            final MultipartFormData form =
                new PluginFactory().createMultipart().parse(
                    arg3.getParameters().get("charset"),
                    Integer.parseInt(arg4.getFirst("Content-Length")),
                    arg3.toString(),
                    arg5);

            final File f = parse(form);
            return f;

        } catch (final InvalidException e) {
            throw new WebApplicationException(
                e, new RestExceptionMapper().toResponse(e, MimeType.HTML));
        }
    }


    private File parse(final MultipartFormData form) throws IOException {

        final InputStream fileIs = form.getInputStream(FILE);
        final String fileType    = form.getContentType(FILE);
        final long fileSize      = form.getSize(FILE);
        final String name        = form.getString(FILE_NAME);
        final String title       = form.getString(TITLE);
        final String description = form.getString(DESCRIPTION);
        final String path        = form.getString(PATH);
        final String lastUpdate  = form.getString(LAST_UPDATE);
        final String cItem       = form.getString(COMMENT);
        final String bItem       = form.getString(MAJOR_EDIT);

        final String comment =
            (cItem==null) ? null : cItem;
        final boolean isMajorEdit =
            (bItem == null) ? false : true;
        final UUID parentId =
            (null==path) ? null : UUID.fromString(path);
        final ResourceName fName =
            (null==name) ? null : new ResourceName(name);

        final Map<String, String> props = new HashMap<String, String>();
        props.put(FilePropertyNames.CHARSET, toCharset(fileType));

        final String titleString = (title == null) ? name : title;

        final String descriptionString =
                (description == null) ? "" : description;

        Date lastUpdateDate = new Date();
        if (lastUpdate != null) {
            lastUpdateDate = new Date(
                Long.valueOf(lastUpdate).longValue());
        }

        final File f = new File(
            toMimeType(fileType),
            null,
            null,
            fName,
            titleString,
            props
        );
        f.setComment(comment);
        f.setMajorEdit(isMajorEdit);
        f.setParent(parentId);
        f.setDescription(descriptionString);
        f.setDateCreated(lastUpdateDate);
        f.setDateChanged(lastUpdateDate);
        f.setInputStream(fileIs);
        f.setSize(fileSize);

        return f;
    }


    /**
     * Extract the mime type from the content-type HTTP header.
     * <p>If the header value is un-parsable then "application/octet-stream"
     * will be returned.
     *
     * @param contentType The HTTP content type.
     *
     * @return The mime type as a value object.
     */
    protected MimeType toMimeType(final String contentType) {
        try {
            final javax.activation.MimeType mt =
                new javax.activation.MimeType(contentType);
            return new MimeType(mt.getPrimaryType(), mt.getSubType());
        } catch (final MimeTypeParseException e) {
            LOG.warn("Ignored invalid mime type: "+contentType);
            return MimeType.BINARY_DATA;
        }
    }


    /**
     * Extract the charset parameter from the content-type HTTP header.
     *
     * @param contentType The HTTP content type.
     *
     * @return The charset param as a string.
     */
    protected String toCharset(final String contentType) {
        try {
            final javax.activation.MimeType mt =
                new javax.activation.MimeType(contentType);
            return mt.getParameter("charset");
        } catch (final MimeTypeParseException e) {
            return null;
        }
    }


    /** {@inheritDoc} */
    @Override
    public long getSize(final File t,
                        final Class<?> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType) {
        return -1;
    }


    /** {@inheritDoc} */
    @Override
    public boolean isWriteable(final Class<?> type,
                               final Type genericType,
                               final Annotation[] annotations,
                               final MediaType mediaType) {
        return File.class.equals(type);
    }


    /** {@inheritDoc} */
    @Override
    public void writeTo(final File t,
                        final Class<?> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream entityStream) throws IOException {
        try {
            final String providedCharset =
                mediaType.getParameters().get("charset");
            final String charset =
                (null==providedCharset) ? "utf-8" : providedCharset;
            final String boundary = UUID.randomUUID().toString();

            final List<Part> parts = new ArrayList<Part>();

            addPart(parts, TITLE,       charset, t.getTitle());
            addPart(parts, DESCRIPTION, charset, t.getDescription());
            addPart(parts, COMMENT,     charset, t.getComment());
            addPart(parts, FILE_NAME,   charset, t.getName().toString());
            addPart(parts, PATH,        charset, t.getParent().toString());
            addPart(parts, MAJOR_EDIT,  charset, t.isMajorEdit());
            addPart(parts, LAST_UPDATE, charset, t.getDateChanged());

            final FilePart fp =
                new UnicodeFilePart(FILE, new SimplePart(t), charset);
            fp.setContentType(t.getMimeType().toString());
            fp.setCharSet(t.getCharset());
            parts.add(fp);

            httpHeaders.add(
                "Content-Type", mediaType.toString()+";boundary="+boundary);
            Part.sendParts(
                entityStream,
                parts.toArray(new Part[parts.size()]),
                boundary.getBytes("ascii"));

        } finally {
            try {
                entityStream.close();
            } catch (final IOException e) {
                LOG.warn("Error closing output stream.", e);
            }
        }
    }


    private void addPart(final List<Part> parts,
                         final String partName,
                         final String charset,
                         final Date value) {
        if (null!=value) {
            addPart(parts, partName, charset, String.valueOf(value.getTime()));
        }
    }


    private void addPart(final List<Part> parts,
                         final String partName,
                         final String charset,
                         final boolean value) {
        addPart(parts, partName, charset, String.valueOf(value));
    }


    private void addPart(final List<Part> parts,
                         final String partName,
                         final String charset,
                         final String value) {
        if (null!=value) {
            parts.add(new UnicodeStringPart(partName, value, charset, charset));
        }
    }


    /**
     * String part that allows non ASCII encoding of the disposition.
     *
     * @author Civic Computing Ltd.
     */
    private static final class UnicodeStringPart
        extends
            StringPart {

        private final String _outerCharset;


        /**
         * Constructor.
         *
         * @param name         The name of the part.
         * @param value        The string to post.
         * @param charset      The charset to be used to encode the string.
         * @param outerCharset The charset for the disposition.
         */
        UnicodeStringPart(final String name,
                          final String value,
                          final String charset,
                          final String outerCharset) {
            super(name, value, charset);
            _outerCharset = outerCharset;
        }


        /** {@inheritDoc} */
        @Override
        protected void sendDispositionHeader(final OutputStream out)
                                                    throws IOException {
            out.write(CONTENT_DISPOSITION_BYTES);
            out.write(QUOTE_BYTES);
            out.write(
                getName()
                    .replace("%",  Strings.hex('%', _outerCharset))
                    .replace("\"", Strings.hex('"', _outerCharset))
                    .getBytes(_outerCharset));
            out.write(QUOTE_BYTES);
        }
    }


    /**
     * File part that allows non ASCII encoding of the disposition.
     *
     * @author Civic Computing Ltd.
     */
    private static final class UnicodeFilePart
        extends
            FilePart {

        private final String _outerCharset;


        /**
         * FilePart Constructor.
         *
         * @param name         The name for this part.
         * @param partSource   The source for this part.
         * @param outerCharset The charset for the disposition.
         */
        UnicodeFilePart(final String name,
                        final PartSource partSource,
                        final String outerCharset) {
            super(name, partSource);
            _outerCharset = outerCharset;
        }


        /** {@inheritDoc} */
        @Override
        protected void sendDispositionHeader(final OutputStream out)
                                                    throws IOException {
            out.write(CONTENT_DISPOSITION_BYTES);
            out.write(QUOTE_BYTES);
            out.write(
                getName()
                    .replace("%",  Strings.hex('%', _outerCharset))
                    .replace("\"", Strings.hex('"', _outerCharset))
                    .getBytes(_outerCharset));
            out.write(QUOTE_BYTES);
            final String filename = getSource().getFileName();
            if (filename != null) {
                out.write("; filename=".getBytes("ASCII"));
                out.write(QUOTE_BYTES);
                out.write(
                    filename
                        .replace("%",  Strings.hex('%', _outerCharset))
                        .replace("\"", Strings.hex('"', _outerCharset))
                        .getBytes(_outerCharset));
                out.write(QUOTE_BYTES);
            }
        }
    }


    /**
     * Simple implementation of http-client's {@link PartSource} interface.
     *
     * @author Civic Computing Ltd.
     */
    static final class SimplePart
        implements
            PartSource {

        private final File _t;


        /**
         * Constructor.
         *
         * @param t The file DTO.
         */
        SimplePart(final File t) {
            _t = DBC.require().notNull(t);
        }

        /** {@inheritDoc} */
        @Override public long getLength() {
            return _t.getSize();
        }

        /** {@inheritDoc} */
        @Override public String getFileName() {
            return _t.getName().toString();
        }

        /** {@inheritDoc} */
        @Override public InputStream createInputStream() {
            return _t.getInputStream();
        }
    }
}
