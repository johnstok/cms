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

import java.util.Map;

import ccc.domain.File;
import ccc.entities.IData;
import ccc.entities.IFile;
import ccc.persistence.DataManager;
import ccc.rendering.FileBody;
import ccc.rendering.Response;
import ccc.rendering.StatefulReader;
import ccc.search.SearchEngine;
import ccc.types.MimeType;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FileSnapshot extends ResourceSnapshot implements IFile {
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

    /** {@inheritDoc} */
    @Override
    public IData getData() {
        return _delegate.getData();
    }

    /**
     * @return
     * @see ccc.domain.File#isImage()
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
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataManager dm) {
        final Response r = new Response(new FileBody(this, dm));
        r.setDescription(description());
        r.setDisposition("inline; filename=\""+name()+"\"");
        r.setMimeType(getMimeType().getPrimaryType(), getMimeType().getSubType());
        r.setLength(getSize());
        r.setExpiry(computeCache());

        return r;
    }
}
