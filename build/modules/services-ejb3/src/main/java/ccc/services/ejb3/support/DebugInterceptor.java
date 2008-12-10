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
package ccc.services.ejb3.support;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class DebugInterceptor
    extends
        EmptyInterceptor {

    /** {@inheritDoc} */
    @Override
    public boolean onFlushDirty(final Object entity,
                                final Serializable id,
                                final Object[] currentState,
                                final Object[] previousState,
                                final String[] propertyNames,
                                final Type[] types) {

        return super.onFlushDirty(entity,
                                  id,
                                  currentState,
                                  previousState,
                                  propertyNames,
                                  types);
    }

    /** {@inheritDoc} */
    @Override
    public int[] findDirty(final Object entity,
                           final Serializable id,
                           final Object[] currentState,
                           final Object[] previousState,
                           final String[] propertyNames,
                           final Type[] types) {

        final int[] dirty = super.findDirty(entity,
                                            id,
                                            currentState,
                                            previousState,
                                            propertyNames,
                                            types);
        return dirty;
    }


}
