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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        HistoricalResource<FileRevision>
    implements
        WCAware<FileDelta> {

    // This is a collection to exploit hibernate's delete-orphan syntax.
    private List<FileWorkingCopy> _workingCopies =
        new ArrayList<FileWorkingCopy>();


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
                final int size,
                final Date timestamp,
                final User actor) {
        this(
            name,
            title,
            description,
            data,
            size,
            MimeType.BINARY_DATA,
            timestamp,
            actor);
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
                final MimeType mimeType,
                final Date timestamp,
                final User actor) {
        super(name, title);
        DBC.require().notNull(data);
        description(description);
        update(
            new RevisionMetadata(timestamp, actor, true, "Created."),
            new FileDelta(title, description, mimeType, new ID(data.id().toString()), size));
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
     * Query if this file is an image.
     *
     * @return True if the file is an image, false otherwise.
     */
    public boolean isImage() {
        return currentRevision().isImage();
    }



    /* ====================================================================
     * Working copy implementation.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public void applySnapshot(final RevisionMetadata metadata) {
        DBC.require().notNull(wc());

        description(wc().delta().getDescription());
        title(wc().delta().getTitle());

        update(metadata, wc().delta());

        clearWorkingCopy();
    }

    private void update(final RevisionMetadata metadata, final FileDelta delta) {
        incrementVersion();
        addRevision(
            new FileRevision(
                currentVersion(),
                metadata.getTimestamp(),
                metadata.getActor(),
                metadata.isMajorChange(),
                metadata.getComment(),
                new Data(UUID.fromString(delta.getData().toString())),
                delta.getSize(),
                delta.getMimeType()));
    }

    /** {@inheritDoc} */
    @Override
    public FileDelta workingCopy() {
        if (null!=wc()) {
            return wc().delta();
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

    /** {@inheritDoc} */
    public final void workingCopy(final FileDelta snapshot) {
        DBC.require().notNull(snapshot);
        if (hasWorkingCopy()) {
            wc().delta(snapshot);
        } else {
            wc(new FileWorkingCopy(snapshot));
        }
    }

    //--

    /** {@inheritDoc} */
    public final void clearWorkingCopy() {
        DBC.require().toBeTrue(hasWorkingCopy());
        _workingCopies.clear();
    }

    /** {@inheritDoc} */
    public boolean hasWorkingCopy() {
        return 0!=_workingCopies.size();
    }

    private FileWorkingCopy wc() {
        if (0==_workingCopies.size()) {
            return null;
        }
        return _workingCopies.get(0);
    }

    private void wc(final FileWorkingCopy pageWorkingCopy) {
        DBC.require().toBeFalse(hasWorkingCopy());
        _workingCopies.add(0, pageWorkingCopy);
    }
}
