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
package ccc.domain;

import static ccc.types.FilePropertyNames.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.api.dto.FileDelta;
import ccc.api.dto.FileDto;
import ccc.api.dto.TextFileDelta;
import ccc.persistence.DataRepository;
import ccc.persistence.streams.ReadToStringAction;
import ccc.types.DBC;
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
     * @param properties The properties of the file..
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
        setDescription(description);
        update(
            new FileDelta(mimeType, data.getId(), size, properties),
            metadata);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType getType() {
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
    public MimeType getMimeType() {
        return currentRevision().getMimeType();
    }


    /**
     * Accessor for the data field.
     *
     * @return The Data instance for this file.
     */
    public Data getData() {
        return currentRevision().getData();
    }


    /**
     * Accessor for the properties field.
     *
     * @return The Data instance for this file.
     */
    public Map<String, String> getProperties() {
        return currentRevision().getProperties();
    }


    /**
     * Accessor for text charset.
     *
     * @return Property value for charset.
     */
    public String getCharset() {
        return getProperties().get(CHARSET);
    }


    /**
     * Accessor for image width.
     *
     * @return Property value for width.
     */
    public String getWidth() {
        return getProperties().get(WIDTH);
    }


    /**
     * Accessor for image height.
     *
     * @return Property value for height.
     */
    public String getHeight() {
        return getProperties().get(HEIGHT);
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


    /**
     * Determine if a file can be executed.
     *
     * @return True if the file can be executed, false otherwise.
     */
    public boolean isExecutable() {
        final boolean hasExeFlag =
            Boolean.valueOf(getMetadatum("executable")).booleanValue();
        return hasExeFlag && !isDeleted();
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
                getMimeType(),
                getData().getId(),
                size(),
                getProperties());
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
    public final FileDto forWorkingCopy() {
        final FileDto dto = mapFile();
        final FileWorkingCopy sn = getWorkingCopy();
        dto.setCharset(sn.getCharset());
        dto.setDataId(sn.getData().getId());
        dto.setExecutable(isExecutable());
        dto.setImage(sn.isImage());
        dto.setSize(sn.getSize());
        dto.setText(sn.isText());
        dto.setMimeType(sn.getMimeType());
        dto.setRevision(-1);
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public final FileDto forCurrentRevision() {
        final FileDto dto = mapFile();
        final FileRevision sn = currentRevision();
        dto.setCharset(sn.getCharset());
        dto.setDataId(sn.getData().getId());
        dto.setExecutable(isExecutable());
        dto.setImage(sn.isImage());
        dto.setSize(sn.getSize());
        dto.setText(sn.isText());
        dto.setMimeType(sn.getMimeType());
        dto.setRevision(currentRevisionNo());
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public final FileDto forSpecificRevision(final int revNo) {
        final FileDto dto = mapFile();
        final FileRevision sn = revision(revNo);
        dto.setCharset(sn.getCharset());
        dto.setDataId(sn.getData().getId());
        dto.setExecutable(isExecutable());
        dto.setImage(sn.isImage());
        dto.setSize(sn.getSize());
        dto.setText(sn.isText());
        dto.setMimeType(sn.getMimeType());
        dto.setRevision(revNo);
        return dto;
    }


    /**
     * Create a summary of a file.
     *
     * @return The summary of the file.
     */
    public FileDto mapFile() {
        final FileDto fs =
            new FileDto(
                getMimeType(),
                getAbsolutePath().removeTop().toString(),
                getId(),
                getName(),
                getTitle(),
                getProperties());
        setDtoProps(fs);
        return fs;
    }


    /**
     * Create a summary of a text file.
     *
     * @return The summary of the file.
     */
    public TextFileDelta mapTextFile(final DataRepository _dm) {

        final TextFileDelta fs =
            new TextFileDelta(
                getId(),
                (!isText())
                    ? null : read(_dm, this),
                getMimeType(),
                currentRevision().isMajorChange(),
                currentRevision().getComment());
        return fs;
    }


    /**
     * Create summaries for a collection of files.
     *
     * @param files The files.
     * @return The corresponding summaries.
     */
    public static List<FileDto> mapFiles(final Collection<File> files) {
        final List<FileDto> mapped = new ArrayList<FileDto>();
        for (final File f : files) {
            mapped.add(f.mapFile());
        }
        return mapped;
    }


    /**
     * Create a delta for a file.
     *
     * @return A corresponding delta.
     */
    public FileDelta deltaFile() {
        return getOrCreateWorkingCopy();
    }


    /* ====================================================================
     * Helper methods.
     * ================================================================== */

    /**
     * Helper method that reads a file's contents into a string.
     *
     * @param dm The file repository.
     * @param file The file to read.
     *
     * @return The file's contents as a string.
     */
    public static String read(final DataRepository dm, final File file) {
        final StringBuilder sb = new StringBuilder();
        dm.retrieve(
            file.getData(), new ReadToStringAction(sb, file.getCharset()));
        return sb.toString();
    }

    /**
     * Helper method that reads a file's contents into a string.
     * TODO: Move to the File class?
     *
     * @param dm The file repository.
     * @param file The file to read.
     *
     * @return The file's contents as a string.
     */
    public static String read(final DataRepository dm,
                              final FileDto file) {
        final StringBuilder sb = new StringBuilder();
        dm.retrieve(
            new Data(file.getData()),
            new ReadToStringAction(sb, file.getCharset()));
        return sb.toString();
    }
}
