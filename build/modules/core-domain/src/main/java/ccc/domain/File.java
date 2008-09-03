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
     * @param data A token representing the binary content of the file.
     */
    public File(final ResourceName name,
                final String title,
                final String description,
                final Data data) {
        super(name, title);
        DBC.require().notNull(data);

        _description = description;
        _data = data;
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
}
