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
import ccc.persistence.DataRepository;
import ccc.rendering.NotFoundException;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.Response;
import ccc.rendering.StatefulReader;
import ccc.search.SearchEngine;


/**
 * A read-only snapshot of an Alias.
 *
 * @author Civic Computing Ltd.
 */
public class AliasSnapshot extends ResourceSnapshot {
    private final Alias _delegate;

    /**
     * Constructor.
     *
     * @param a The alias to wrap.
     */
    public AliasSnapshot(final Alias a) {
        super(a);
        _delegate = a;
    }

    /**
     * Accessor.
     *
     * @return The alias' target resource.
     */
    public Resource target() {
        return _delegate.target();
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataRepository dm) {
        if (null==target()) {
            throw new NotFoundException();
        }
        throw new RedirectRequiredException(target());
    }

}
