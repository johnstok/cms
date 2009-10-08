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

import ccc.domain.File;
import ccc.entities.IData;
import ccc.entities.IFile;
import ccc.rendering.FileBody;
import ccc.rendering.Response;
import ccc.types.MimeType;


/**
 * A read-only snapshot of a file resource.
 *
 * @author Civic Computing Ltd.
 */
public class FileSnapshot extends ResourceSnapshot implements IFile {
    private final IFile _delegate;

    /**
     * Constructor.
     * TODO: We shouldn't need two parameters here.
     *
     * @param f The file this snapshot wraps.
     * @param delegate The revision this snapshot wraps.
     */
    public FileSnapshot(final File f, final IFile delegate) {
        super(f);
        _delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public IData getData() {
        return _delegate.getData();
    }

    /**
     * Query.
     *
     * @return True if the file is an image, false otherwise.
     */
    public boolean isImage() {
        return _delegate.isImage();
    }

    /** {@inheritDoc} */
    @Override
    public MimeType getMimeType() {
        return _delegate.getMimeType();
    }

    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return _delegate.getSize();
    }

    /** {@inheritDoc} */
    @Override
    public Response render() {
        final Response r = new Response(new FileBody(this));
        r.setDescription(description());
        r.setDisposition("inline; filename=\""+name()+"\"");
        r.setMimeType(
            getMimeType().getPrimaryType(), getMimeType().getSubType());
        r.setLength(getSize());
        r.setExpiry(computeCache());

        return r;
    }

    /** {@inheritDoc} */
    @Override public String getCharset() {
        return _delegate.getCharset();
    }
}
