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
package ccc.snapshots;

import ccc.api.MimeType;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.IFile;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FileSnapshot extends ResourceSnapshot {
    private final IFile _delegate;

    /**
     * Constructor.
     *
     * @param delegate
     */
    public FileSnapshot(final File f, final IFile delegate) {
        super(f);
        _delegate = delegate;
    }

    /**
     * @return
     * @see ccc.domain.File#data()
     */
    public Data data() {
        return _delegate.getData();
    }

    /**
     * @return
     * @see ccc.domain.File#isImage()
     */
    public boolean isImage() {
        return _delegate.isImage();
    }

    /**
     * @return
     * @see ccc.domain.File#mimeType()
     */
    public MimeType mimeType() {
        return _delegate.getMimeType();
    }

    /**
     * @return
     * @see ccc.domain.File#size()
     */
    public int size() {
        return _delegate.getSize();
    }
}
