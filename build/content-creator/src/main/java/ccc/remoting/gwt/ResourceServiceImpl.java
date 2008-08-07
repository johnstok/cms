/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.remoting.gwt;

import java.util.Map;

import ccc.commons.jee.DBC;
import ccc.commons.jee.JNDI;
import ccc.commons.jee.Registry;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;
import ccc.view.contentcreator.client.ResourceService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


/**
 * Implementation of {@link ResourceService}.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceServiceImpl extends RemoteServiceServlet
                                    implements ResourceService {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 4907235349044174242L;
    private Registry _registry = new JNDI();

    /**
     * Constructor.
     *
     */
    public ResourceServiceImpl() { super(); }

    /**
     * Constructor.
     *
     * @param registry The registry for this servlet.
     */
    public ResourceServiceImpl(final Registry registry) {
        DBC.require().notNull(registry);
        _registry = registry;
    }

    /**
     * {@inheritDoc}
     */
    public String getContentRoot() {
        return getResource("/");
    }

    /**
     * Accessor for the resource manager.
     *
     * @return A ResourceManager.
     */
    ResourceManager resourceManager() {
        return _registry.get("ResourceManagerEJB/local");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResource(final String absolutePath) {
        final Resource root =
            resourceManager().lookup(new ResourcePath(absolutePath));
        return root.toJSON();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveContent(final String id,
                            final String title,
                            final Map<String, String> paragraphs) {
        resourceManager().saveContent(id, title, paragraphs);
    }
}
