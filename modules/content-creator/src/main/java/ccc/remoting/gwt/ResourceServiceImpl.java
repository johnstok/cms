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

import static ccc.remoting.gwt.DTOs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.services.AssetManager;
import ccc.services.ContentManager;
import ccc.view.contentcreator.client.ResourceService;
import ccc.view.contentcreator.dto.DTO;
import ccc.view.contentcreator.dto.OptionDTO;
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
    public String getResource(final String absolutePath) {
        final Resource resource =
            contentManager().lookup(new ResourcePath(absolutePath));
        return resource.toJSON();
    }

    /**
     * {@inheritDoc}
     */
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

    private void setDefaultTemplate(final TemplateDTO templateDTO) {
        if (null==templateDTO) {
            contentManager().setDefaultTemplate(null);
        } else {
            final Template newDefault =
                assetManager().lookup(UUID.fromString(templateDTO.getId()));
            contentManager().setDefaultTemplate(newDefault);
        }

    }

    /**
     * {@inheritDoc}
     */
    public List<TemplateDTO> listTemplates() {
        final List<TemplateDTO> dtos = new ArrayList<TemplateDTO>();
        final List<Template> templates = assetManager().lookupTemplates();
        for (final Template template : templates) {
            dtos.add(dtoFrom(template));
        }
        return dtos;
    }

    /**
     * {@inheritDoc}
     */
    public List<OptionDTO<? extends DTO>> listOptions() {

        final List<OptionDTO<? extends DTO>> options =
            new ArrayList<OptionDTO<? extends DTO>>();

        final Folder contentRoot = contentManager().lookupRoot();
        final Template rootTemplate = contentRoot.displayTemplateName();

        final OptionDTO<TemplateDTO> defaultTemplate =
            new OptionDTO<TemplateDTO>(dtoFrom(rootTemplate),
                                       listTemplates(),
                                       OptionDTO.Type.CHOICES);
        options.add(defaultTemplate);

        return options;
    }

    /**
     * {@inheritDoc}
     */
    public void updateOptions(final List<OptionDTO<? extends DTO>> options) {
        final OptionDTO<TemplateDTO> defaultTemplate =
            options.get(0).makeTypeSafe();
        if (defaultTemplate.hasChanged()) {
            setDefaultTemplate(defaultTemplate.getCurrentValue());
        }
    }
}
