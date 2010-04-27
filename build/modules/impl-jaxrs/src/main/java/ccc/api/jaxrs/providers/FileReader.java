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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimeTypeParseException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import ccc.api.core.FileDto;
import ccc.api.types.DBC;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
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
public class FileReader
    extends
        AbstractProvider
    implements
        MessageBodyWriter<FileDto>,
        MessageBodyReader<FileDto> {

    private static final Logger LOG = Logger.getLogger(FileReader.class);

    private static final String FILE        = "file";
    private static final String FILE_NAME   = "fileName";
    private static final String TITLE       = "title";
    private static final String DESCRIPTION = "description";
    private static final String PATH        = "path";
    private static final String PUBLISH     = "publish";
    private static final String LAST_UPDATE = "lastUpdate";
    private static final String COMMENT     = "comment";
    private static final String MAJOR_EDIT  = "majorEdit";


    /** {@inheritDoc} */
    @Override
    public boolean isReadable(final Class<?> clazz,
                              final Type type,
                              final Annotation[] annotations,
                              final MediaType mediaType) {
        return FileDto.class.equals(clazz);
    }


    /** {@inheritDoc} */
    @Override
    public FileDto readFrom(final Class<FileDto> arg0,
                             final Type arg1,
                             final Annotation[] arg2,
                             final MediaType arg3,
                             final MultivaluedMap<String, String> arg4,
                             final InputStream arg5) throws IOException {

        final MultipartFormData form =
            new PluginFactory().createFormData(
                arg3.getParameters().get("charset"),
                Integer.parseInt(arg4.getFirst("Content-Length")),
                arg3.toString(),
                arg5);

        final FileDto f = parse(form);

        return f;
    }


    private FileDto parse(final MultipartFormData form) throws IOException {

        final InputStream fileIs = form.getInputStream(FILE);
        final String fileType    = form.getContentType(FILE);
        final long fileSize      = form.getSize(FILE);
        final String name        = form.getString(FILE_NAME);
        final String title       = form.getString(TITLE);
        final String description = form.getString(DESCRIPTION);
        final String path        = form.getString(PATH);
        final String publish     = form.getString(PUBLISH);
        final String lastUpdate  = form.getString(LAST_UPDATE);
        final String cItem       = form.getString(COMMENT);
        final String bItem       = form.getString(MAJOR_EDIT);

        final String comment =
            (cItem==null) ? null : cItem;
        final boolean isMajorEdit =
            (bItem == null) ? false : true;
        final UUID parentId =
            (null==path) ? null : UUID.fromString(path);
        final boolean p =
            (null==publish) ? false : Boolean.parseBoolean(publish);
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

        final FileDto f = new FileDto(
            toMimeType(fileType),
            null,
            null,
            fName,
            titleString,
            props
        );
        f.setComment(
            (comment == null || comment.isEmpty()) ? "" : comment);
        f.setMajorEdit(isMajorEdit);
        f.setPublished(p);
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
    public long getSize(final FileDto t,
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
        return FileDto.class.equals(type);
    }


    /** {@inheritDoc} */
    @Override
    public void writeTo(final FileDto t,
                        final Class<?> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream entityStream) throws IOException {
        try {

            final String name        = t.getName().toString();
            final String title       = t.getTitle();
            final String description = t.getDescription();
            final Date lastUpdate    = t.getDateChanged();
            final UUID parentId      = t.getParent();
            final boolean publish    = t.isPublished();
            final String contentType = t.getMimeType().toString();
            final String charset     = t.getCharset();
            final String comment     = t.getComment();
            final String majorEdit   =
                Boolean.valueOf(t.isMajorEdit()).toString();

            final PartSource ps = new SimplePart(t);

            final FilePart fp = new FilePart(FILE, ps);
            fp.setContentType(contentType);
            fp.setCharSet(charset);
            final Part[] parts = {
                new StringPart(FILE_NAME, name),
                new StringPart(TITLE, title),
                new StringPart(DESCRIPTION, description),
                new StringPart(COMMENT, comment),
                new StringPart(MAJOR_EDIT, majorEdit),
                new StringPart(
                    LAST_UPDATE,
                    String.valueOf(
                        (null==lastUpdate)
                            ? new Date().getTime()
                            : lastUpdate.getTime())),
                new StringPart(PATH, parentId.toString()),
                new StringPart(PUBLISH, String.valueOf(publish)),
                fp
            };

            final RequestEntity entity =
                new MultipartRequestEntity(parts, new HttpMethodParams());

            httpHeaders.add("Content-Type", entity.getContentType());
            entity.writeRequest(entityStream);

        } finally {
            try {
                entityStream.close();
            } catch (final IOException e) {
                LOG.warn("Error closing output stream.", e);
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

        private final FileDto _t;


        /**
         * Constructor.
         *
         * @param t The file DTO.
         */
        SimplePart(final FileDto t) {
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
