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

import org.apache.log4j.Logger;

import ccc.commons.DBC;
import ccc.services.api.ResourceType;


/**
 * A file resource. This class encapsulates all file metadata. The raw file data
 * is stored separately and can be accessed using the token stored in this
 * class' {@link #fileData()} field.
 *
 * @author Civic Computing Ltd.
 */
public class File
    extends
        Resource {
    private static Logger LOG = Logger.getLogger(File.class);

    private String _description;
    private Data _data;
    private int _size;
    private MimeType _mimeType;


    /** Constructor: for persistence only. */
    protected File() { super(); }

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
                final int size) {
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
            // This should never happen.
            LOG.error("Failed to create mimetype: application/octet-stream", e);
            return null;
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
                final int size,
                final MimeType mimeType) {

        super(name, title);
        data(data);
        // TODO: null tests for other param's...

        _description = description;
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
    public int size() {
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

    /**
     * Mutator for the data field.
     *
     * @param newData The new data for the file.
     */
    public void data(final Data newData) {
        DBC.require().notNull(newData);
        _data = newData;
    }

    /**
     * Accessor for the data field.
     *
     * @return The Data instance for this file.
     */
    public Data data() {
        return _data;
    }

    /**
     * Mutator for the file description.
     *
     * @param description The new description as a string.
     */
    public void description(final String description) {
        _description = description;
    }

    /**
     * Mutator for the file's mime type.
     * TODO: Make defensive copy?
     *
     * @param mimeType The new mime type.
     */
    public void mimeType(final MimeType mimeType) {
        _mimeType = mimeType;
    }

    /**
     * Mutator for the file's size.
     *
     * @param size The new size.
     */
    public void size(final int size) {
        _size = size;
    }

    /** {@inheritDoc} */
    @Override
    public Snapshot createSnapshot() {
        final Snapshot s = super.createSnapshot();
        s.set("description", _description);
        s.set("mimetype", _mimeType.toString());
        s.set("size", _size);
        s.set("data", _data.id());
        return s;
    }

    /** {@inheritDoc} */
    @Override
    public void applySnapshot(final Snapshot s)
                                               throws InvalidSnapshotException {
        try {
            description(s.getString("description"));
            mimeType(new MimeType(s.getString("mimetype")));
            size(s.getInt("size"));
            data(new Data(s.getUuid("data")));
        } catch (final MimeTypeParseException e) {
            throw new InvalidSnapshotException(e);
        }
    }
}
