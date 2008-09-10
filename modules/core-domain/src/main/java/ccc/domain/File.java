/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import ccc.commons.DBC;


/**
 * A file resource. This class encapsulates all file metadata. The raw file data
 * is stored separately and can be accessed using the token stored in this
 * class' {@link #fileData()} field.
 *
 * @author Civic Computing Ltd.
 */
public class File extends Resource {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -8514993906228600518L;

    private String _description;
    private Data _data;
    private long _size;
    private MimeType _mimeType;

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private File() { super(); }

    /**
     * Constructor. Assumes a mime type of "application/octet-stream".
     *
     * @param name The name of the file.
     * @param title The title of the file.
     * @param description The description of the file.
     * @param data A token representing the binary content of the file.
     * @param size The size of the file in bytes.
     */
    public File(final ResourceName name,
                final String title,
                final String description,
                final Data data,
                final long size) {
        this(name, title, description, data, size, defaultMimeType());
    }

    /**
     * Create a mime type representing the default value
     * ('application/octet-stream').
     *
     * @return An instance of {@link MimeType}.
     */
    public static MimeType defaultMimeType() {
        try {
            return new MimeType("application", "octet-stream");
        } catch (final MimeTypeParseException e) {
            throw new CCCException(
                "Failed to create mimetype: application/octet-stream", e);
        }
    }


    /**
     * Constructor.
     *
     * @param name The name of the file.
     * @param title The title of the file.
     * @param description The description of the file.
     * @param data A token representing the binary content of the file.
     * @param size The size of the file in bytes.
     * @param mimeType The mime type for the file.
     */
    public File(final ResourceName name,
                final String title,
                final String description,
                final Data data,
                final long size,
                final MimeType mimeType) {

        super(name, title);
        DBC.require().notNull(data);
        // TODO: null tests for other param's...

        _description = description;
        _data = data;
        _size = size;
        _mimeType = mimeType; // TODO: Defensive copy???
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.FILE;
    }

    /**
     * Accessor for the file's description.
     *
     * @return The description as a string.
     */
    public final String description() {
        return _description;
    }

    /**
     * Accessor for FileData.
     *
     * @return A list of data for this file.
     */
    public Data fileData() {
        return _data;
    }

    /**
     * Accessor for size.
     *
     * @return The size of the file in bytes, as a long.
     */
    public long size() {
        return _size;
    }

    /**
     * Accessor for mime type.
     * TODO: Make defensive copy?
     *
     * @return The mime type.
     */
    public MimeType mimeType() {
        return _mimeType;
    }
}
