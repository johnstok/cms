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

import ccc.domain.Alias;
import ccc.domain.Resource;
import ccc.persistence.DataManager;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.Response;
import ccc.rendering.StatefulReader;
import ccc.search.SearchEngine;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AliasSnapshot extends ResourceSnapshot {
    private final Alias _delegate;

    /**
     * Constructor.
     *
     * @param page
     */
    public AliasSnapshot(final Alias a) {
        super(a);
        _delegate = a;
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public Resource target() {
        return _delegate.target();
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataManager dm) {
        throw new RedirectRequiredException(target());
    }

}
