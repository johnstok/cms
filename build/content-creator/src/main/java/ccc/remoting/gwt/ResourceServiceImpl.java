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
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;
import ccc.services.adaptors.ResourceManagerAdaptor;
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
    private ResourceManager resourceManager;

    /**
     * Constructor.
     *
     */
    public ResourceServiceImpl() { super(); }

    /**
     * Constructor.
     *
     * @param resourceManager The resource manager for this servlet.
     */
    public ResourceServiceImpl(final ResourceManagerAdaptor resourceManager) {
        DBC.require().notNull(resourceManager);
        this.resourceManager = resourceManager;
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
        if (resourceManager == null) {
            resourceManager = JNDI.get("ResourceManagerEJB/local");
        }
        return resourceManager;
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