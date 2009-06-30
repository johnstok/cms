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

import ccc.domain.Alias;
import ccc.domain.Resource;


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

}
