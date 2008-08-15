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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
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
        final Resource resource =
            contentManager().lookup(new ResourcePath(absolutePath));
        return resource.toJSON();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveContent(final String id,
                            final String title,
                            final Map<String, String> paragraphs) {
        contentManager().update(UUID.fromString(id), title, paragraphs);
    }

    /**
     * Create a new template.
     *
     * @param dto A DTO representing the template.
     */
    public void createTemplate(final TemplateDTO dto) {
        assetManager().createDisplayTemplate(DTOs.templateFrom(dto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultTemplate(final String templateId) {
        final Template newDefault =
            assetManager().lookup(UUID.fromString(templateId));
        contentManager().setDefaultTemplate(newDefault);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemplateDTO> listTemplates() {
        final List<TemplateDTO> dtos = new ArrayList<TemplateDTO>();
        final List<Template> templates = assetManager().lookupTemplates();
        for (final Template template : templates) {
            // TODO: Should probably factor this object creation to DTOs.
            final TemplateDTO dto = new TemplateDTO(template.title(),
                                              template.description(),
                                              template.body());
            dto.setId(template.id().toString());
            dtos.add(dto);
        }
        return dtos;
    }
}
