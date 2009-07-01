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

import java.util.List;

import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FolderSnapshot extends ResourceSnapshot {
    private final Folder _delegate;

    /**
     * Constructor.
     *
     * @param page
     */
    public FolderSnapshot(final Folder f) {
        super(f);
        _delegate = f;
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public Page indexPage() {
        return _delegate.indexPage();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public List<Resource> entries() {
        return _delegate.entries();
    }

    /**
     * @return
     * @see ccc.domain.Folder#pages()
     */
    public List<Page> pages() {
        return _delegate.pages();
    }
}
