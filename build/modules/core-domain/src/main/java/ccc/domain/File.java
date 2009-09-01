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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.rest.dto.FileDelta;
import ccc.snapshots.FileSnapshot;
import ccc.types.DBC;
import ccc.types.ID;
import ccc.types.MimeType;
import ccc.types.ResourceName;
import ccc.types.ResourceType;


/**
 * A file resource. This class encapsulates all file metadata. The raw file data
 * is stored separately and can be accessed using the token stored in this
 * class' {@link #fileData()} field.
 *
 * @author Civic Computing Ltd.
 */
public class File
    extends
        WorkingCopySupport<FileRevision, FileDelta, FileWorkingCopy>  {


    // FIXME: Move these constants into public-api - they cannot change!
    public static final String CHARSET = "text.charset";
    public static final String WIDTH = "image.width";
    public static final String HEIGHT = "image.height";


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
     * @param metadata The metadata for the revision.
     */
    public File(final ResourceName name,
                final String title,
                final String description,
                final Data data,
                final int size,
                final RevisionMetadata metadata) {
        this(
            name,
            title,
            description,
            data,
            size,
            MimeType.BINARY_DATA,
            new HashMap<String, String>(),
            metadata);
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
     * @param metadata The metadata for the revision.
     */
    public File(final ResourceName name,
                final String title,
                final String description,
                final Data data,
                final int size,
                final MimeType mimeType,
                final Map<String, String> properties,
                final RevisionMetadata metadata) {
        super(name, title);
        DBC.require().notNull(data);
        description(description);
        update(
            new FileDelta(mimeType, new ID(data.id().toString()), size, properties),
            metadata);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.FILE;
    }


    /**
     * Accessor for size.
     *
     * @return The size of the file in bytes, as a long.
     */
    public int size() {
        return currentRevision().getSize();
    }


    /**
     * Accessor for mime type.
     * TODO: Make defensive copy?
     *
     * @return The mime type.
     */
    public MimeType mimeType() {
        return currentRevision().getMimeType();
    }


    /**
     * Accessor for the data field.
     *
     * @return The Data instance for this file.
     */
    public Data data() {
        return currentRevision().getData();
    }


    /**
     * Accessor for the properties field.
     *
     * @return The Data instance for this file.
     */
    public Map<String, String> properties() {
        return currentRevision().getProperties();
    }


    public String charset() {
        return properties().get(CHARSET);
    }


    public String width() {
        return properties().get(WIDTH);
    }


    public String height() {
        return properties().get(HEIGHT);
    }


    /**
     * Query if this file is an image.
     *
     * @return True if the file is an image, false otherwise.
     */
    public boolean isImage() {
        return currentRevision().isImage();
    }


    /**
     * Query if this file is a text file.
     *
     * @return True if the file is a text file, false otherwise.
     */
    public boolean isText() {
        return currentRevision().isText();
    }




    /* ====================================================================
     * Working copy implementation.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    protected void update(final FileDelta delta,
                          final RevisionMetadata metadata) {
        addRevision(
            new FileRevision(
                metadata.getTimestamp(),
                metadata.getActor(),
                metadata.isMajorChange(),
                metadata.getComment(),
                new Data(UUID.fromString(delta.getData().toString())),
                delta.getSize(),
                delta.getMimeType(),
                delta.getProperties()));
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta createSnapshot() {
        final FileDelta delta =
            new FileDelta(
                mimeType(),
                new ID(data().id().toString()),
                size(),
                properties());
        return delta;
    }


    /** {@inheritDoc} */
    @Override
    protected FileWorkingCopy createWorkingCopy(final FileDelta delta) {
        return new FileWorkingCopy(delta);
    }




    /* ====================================================================
     * Snapshot support.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public final FileSnapshot forWorkingCopy() {
        return new FileSnapshot(this, wc());
    }

    /** {@inheritDoc} */
    @Override
    public final FileSnapshot forCurrentRevision() {
        return new FileSnapshot(this, currentRevision());
    }

    /** {@inheritDoc} */
    @Override
    public final FileSnapshot forSpecificRevision(final int revNo) {
        return new FileSnapshot(this, revision(revNo));
    }
}
