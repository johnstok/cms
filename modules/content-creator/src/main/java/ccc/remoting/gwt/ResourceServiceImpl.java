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

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.AssetManager;
import ccc.services.ContentManager;
import ccc.view.contentcreator.client.ResourceService;
import ccc.view.contentcreator.dto.TemplateDTO;

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
     * Accessor for the content manager.
     *
     * @return A ContentManager.
     */
    ContentManager contentManager() {
        return _registry.get("ContentManagerEJB/local");
    }

    /**
     * Accessor for the asset manager.
     *
     * @return An AssetManager.
     */
    AssetManager assetManager() {
        return _registry.get("AssetManagerEJB/local");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResource(final String absolutePath) {
        final Resource root =
            contentManager().lookup(new ResourcePath(absolutePath));
        return root.toJSON();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveContent(final String id,
                            final String title,
                            final Map<String, String> paragraphs) {
        contentManager().saveContent(id, title, paragraphs);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param dto
     */
    public void createTemplate(final TemplateDTO dto) {
        assetManager().createDisplayTemplate(DTOs.templateFrom(dto));
    }
}
