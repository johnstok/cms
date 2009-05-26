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

import java.util.UUID;

import ccc.api.DBC;
import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.Json;
import ccc.api.MimeType;
import ccc.api.ResourceType;


/**
 * A file resource. This class encapsulates all file metadata. The raw file data
 * is stored separately and can be accessed using the token stored in this
 * class' {@link #fileData()} field.
 *
 * @author Civic Computing Ltd.
 */
public class File
    extends
        WorkingCopyAware<FileDelta> {

    private Data      _data;
    private int       _size;
    private MimeType  _mimeType;


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
        this(name, title, description, data, size, MimeType.BINARY_DATA);
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
        description(description);
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

    //--
    /** {@inheritDoc} */
    @Override
    public void applySnapshot() {
        DBC.require().notNull(_workingCopy);

        description(_workingCopy.getDescription());
        mimeType(_workingCopy.getMimeType());
        size(_workingCopy.getSize());
        title(_workingCopy.getTitle());
        data(new Data(UUID.fromString(_workingCopy.getData().toString())));

        clearWorkingCopy();
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta workingCopy() {
        if (null!=_workingCopy) {
            return _workingCopy;
        }
        return createSnapshot();
    }

    /** {@inheritDoc} */
    @Override
    public void workingCopy(final Json snapshot) {
        workingCopy(new FileDelta(snapshot));
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta createSnapshot() {
        final FileDelta delta =
            new FileDelta(
                title(),
                description(),
                mimeType(),
                new ID(data().id().toString()),
                size());
        return delta;
    }

    @SuppressWarnings("unused")
    private String getWorkingCopyString() {
        if (null==_workingCopy) {
            return null;
        }
        final Snapshot s = new Snapshot();
        _workingCopy.toJson(s);
        return s.getDetail();
    }

    @SuppressWarnings("unused")
    private void setWorkingCopyString(final String wcs) {
        if (null==wcs) {
            return;
        }
        final Snapshot s = new Snapshot(wcs);
        _workingCopy = new FileDelta(s);
    }

    /**
     * Query if this file is an image.
     *
     * @return True if the file is an image, false otherwise.
     */
    public boolean isImage() {
        return "image".equalsIgnoreCase(mimeType().getPrimaryType());
    }
}
