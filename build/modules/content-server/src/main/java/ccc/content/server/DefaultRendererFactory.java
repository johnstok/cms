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
package ccc.content.server;

import ccc.commons.DBC;
import ccc.commons.Registry;
import ccc.services.DataManager;
import ccc.services.ServiceNames;
import ccc.services.StatefulReader;


/**
 * Default implementation of the {@link RendererFactory} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultRendererFactory implements RendererFactory {

    private final Registry _registry;

    /**
     * Constructor.
     *
     * @param registry The registry used to look up business services.
     */
    public DefaultRendererFactory(final Registry registry) {
        DBC.require().notNull(registry);
        _registry = registry;
    }

    /** {@inheritDoc} */
    @Override
    public ResourceRenderer newInstance() {
        return new DefaultResourceRenderer(dataManager(),
                                           resourceReader(),
                                           true);
    }

    private StatefulReader resourceReader() {
        return _registry.get(ServiceNames.STATEFUL_READER);
    }

    private DataManager dataManager() {
        return _registry.get(ServiceNames.DATA_MANAGER_LOCAL);
    }
}
