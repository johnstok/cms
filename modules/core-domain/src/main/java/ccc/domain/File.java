/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.util.ArrayList;
import java.util.List;

import ccc.commons.DBC;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class File extends Resource implements JSONable {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -8514993906228600518L;
    private String _description;

    // File data is store as one-to-many relationship because Hibernate does
    // not support lazy loading for one-to-one relations.
    private final List<FileData> _fileDatas = new ArrayList<FileData>();

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private File() { super(); }

    /**
     * Constructor.
     *
     * @param name The name of the file.
     * @param title The title of the file.
     * @param description The description of the file.
     * @param fileData The binary content of the file.
     */
    public File(final ResourceName name,
                final String title,
                final String description,
                final FileData fileData) {
        super(name, title);
        DBC.require().notNull(fileData);

        _description = description;
        _fileDatas.add(fileData);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.FILE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toJSON() {

        throw new UnsupportedOperationException("Method not implemented.");
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
    public FileData fileData() {
        return _fileDatas.get(0);
    }
}
