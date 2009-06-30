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

import java.util.Set;

import ccc.api.Paragraph;
import ccc.domain.IPage;
import ccc.domain.Page;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageSnapshot extends ResourceSnapshot implements IPage {
    private final IPage _delegate;

    /**
     * Constructor.
     *
     * @param page
     */
    public PageSnapshot(final Page p, final IPage page) {
        super(p);
        _delegate = page;
    }

    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> getContent() {
        return _delegate.getContent();
    }
}
