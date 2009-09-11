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
package ccc.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ccc.entities.IFile;
import ccc.rest.dto.FileDelta;
import ccc.types.DBC;
import ccc.types.MimeType;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FileRevision
    extends
        Revision<FileDelta>
    implements
        IFile {

    private Data      _data;
    private int       _size;
    private MimeType  _mimeType;
    private Map<String, String> _properties;

    /** Constructor: for persistence only. */
    protected FileRevision() { super(); }

    /**
     * Constructor.
     *
     * @param majorChange Is this revision a major change.
     * @param comment Comment describing the revision.
     * @param data The contents of the file.
     * @param size The size of the file in bytes.
     * @param mimeType The file's mime type.
     * @param properties Additional properties for the file.
     * @param timestamp The date the revision was created.
     * @param actor The actor that created this revision.
     */
    FileRevision(final Date timestamp,
                 final User actor,
                 final boolean majorChange,
                 final String comment,
                 final Data data,
                 final int size,
                 final MimeType mimeType,
                 final Map<String, String> properties) {
        super(timestamp, actor, majorChange, comment);
        DBC.require().notNull(data);
        DBC.require().notNull(mimeType);
        _data = data;
        _size = size;
        _mimeType = mimeType; // TODO: Make defensive copy?
        _properties = properties;
    }


    /** {@inheritDoc} */
    public final Data getData() {
        return _data;
    }


    /** {@inheritDoc} */
    public final int getSize() {
        return _size;
    }


    /** {@inheritDoc} */
    public final MimeType getMimeType() {
        return _mimeType;
    }

    /** {@inheritDoc} */
    public boolean isImage() {
        return "image".equalsIgnoreCase(getMimeType().getPrimaryType());
    }

    /** {@inheritDoc} */
    public boolean isText() {
        return "text".equalsIgnoreCase(getMimeType().getPrimaryType());
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta delta() {
        return new FileDelta(
            _mimeType,
            _data.id(),
            _size,
            _properties);
    }

    /**
     * Retrieve the properties for a file.
     *
     * @return The properties as a map.
     */
    public Map<String, String> getProperties() {
        return new HashMap<String, String>(_properties);
    }
}
