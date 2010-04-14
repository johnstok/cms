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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import ccc.api.dto.FileDelta;
import ccc.api.dto.FileDto;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


/**
 * A {@link MessageBodyReader} for durations.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("multipart/form-data")
public class FileReader
    extends
        AbstractProvider
    implements
        MessageBodyWriter<FileDto>,
        MessageBodyReader<FileDto> {
    private static final Logger LOG = Logger.getLogger(FileReader.class);

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
        final JaxrsRequestContext rc =
            new JaxrsRequestContext(
                arg3.getParameters().get("charset"),
                Integer.parseInt(arg4.getFirst("Content-Length")),
                arg3.toString(),
                arg5);

        final FileDto f = parse(rc);

        return f;
    }

    private FileDto parse(final JaxrsRequestContext rc) throws IOException {
        final MultipartForm form = new MultipartForm(rc);

        final FileItem file        = form.getFileItem().get("file");
        final FileItem name        = form.getFormItem("fileName");
        final FileItem title       = form.getFormItem("title");
        final FileItem description = form.getFormItem("description");
        final FileItem path        = form.getFormItem("path");
        final FileItem publish     = form.getFormItem("publish");
        final FileItem lastUpdate  = form.getFormItem("lastUpdate");

        final FileItem cItem = form.getFormItem("comment");
        final String comment = cItem==null ? null : cItem.getString();

        final FileItem bItem = form.getFormItem("majorEdit");
        final boolean isMajorEdit = bItem == null ? false : true;

        final UUID parentId = UUID.fromString(path.getString());
        final boolean p =
            (null==publish)
                ? false
                : Boolean.parseBoolean(publish.getString());

        final Map<String, String> props = new HashMap<String, String>();
        props.put(FilePropertyNames.CHARSET, toCharset(file.getContentType()));

        final FileDelta delta =
            new FileDelta(
                toMimeType(file.getContentType()),
                null,
                (int) file.getSize(),
                props);

        final String titleString =
            (title == null) ? name.getString() : title.getString();

        final String descriptionString =
                (description == null) ? "" : description.getString();

        Date lastUpdateDate = new Date();
        if (lastUpdate != null) {
            lastUpdateDate = new Date(
                Long.valueOf(lastUpdate.getString()).longValue());
        }

        final FileDto f = new FileDto(
            toMimeType(file.getContentType()),
            null,
            null,
            new ResourceName(name.getString()),
            titleString,
            props
        );
        f.setComment(
            (comment == null || comment.isEmpty()) ? "Created." : comment);
        f.setMajorEdit(isMajorEdit);
        f.setPublished(p);
        f.setParent(parentId);
        f.setDescription(descriptionString);
        f.setDateCreated(lastUpdateDate);
        f.setDateChanged(lastUpdateDate);
        f.setInputStream(file.getInputStream());
        f.setSize((int) file.getSize());

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

        final String name        = t.getName().toString();
        final String title       = t.getTitle();
        final String description = t.getDescription();
        final Date lastUpdate    = t.getDateChanged();
        final UUID parentId      = t.getParent();
        final boolean publish    = t.isPublished();
        final long size          = t.getSize();
        final String contentType = t.getMimeType().toString();
        final String charset     = t.getCharset();

        final PartSource ps = new PartSource() {

            @Override public long getLength() {
                return size;
            }

            @Override public String getFileName() {
                return name;
            }

            @Override public InputStream createInputStream() {
                return t.getInputStream();
            }
        };

        final FilePart fp = new FilePart("file", ps);
        fp.setContentType(contentType);
        fp.setCharSet(charset);
        final Part[] parts = {
            new StringPart("fileName", name),
            new StringPart("title", title),
            new StringPart("description", description),
            new StringPart(
                "lastUpdate",
                String.valueOf(
                    (null==lastUpdate)
                        ? new Date().getTime()
                        : lastUpdate.getTime())),
            new StringPart("path", parentId.toString()),
            new StringPart("publish", String.valueOf(publish)),
            fp
        };

        final RequestEntity entity =
            new MultipartRequestEntity(parts, new HttpMethodParams());

        httpHeaders.add("Content-Type", entity.getContentType());
        entity.writeRequest(entityStream);

    }
}
