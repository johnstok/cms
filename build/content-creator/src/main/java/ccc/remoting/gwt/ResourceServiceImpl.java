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

import ccc.commons.jee.JNDI;
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
public class ResourceServiceImpl extends RemoteServiceServlet
                                    implements ResourceService {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 4907235349044174242L;

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
        return JNDI.get("ResourceManagerEJB/local");
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
}