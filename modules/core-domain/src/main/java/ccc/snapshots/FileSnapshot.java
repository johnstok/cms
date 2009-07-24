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

import ccc.api.MimeType;
import ccc.api.template.StatefulReader;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.IFile;
import ccc.rendering.FileBody;
import ccc.rendering.Response;
import ccc.services.DataManager;
import ccc.services.SearchEngine;


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

    /** {@inheritDoc} */
    @Override
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataManager dm) {
        final Response r = new Response(new FileBody(this, dm));
        r.setDescription(description());
        r.setDisposition("inline; filename=\""+name()+"\"");
        r.setMimeType(mimeType().getPrimaryType(), mimeType().getSubType());
        r.setLength(size());
        r.setExpiry(computeCache());

        return r;
    }
}
