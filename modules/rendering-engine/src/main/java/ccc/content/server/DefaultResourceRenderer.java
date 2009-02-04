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

import java.nio.charset.Charset;
import java.util.UUID;

import ccc.commons.DBC;
import ccc.domain.Alias;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.DataManager;
import ccc.services.StatefulReader;

/**
 * Default implementation of the {@link ResourceRenderer} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultResourceRenderer
    implements
        ResourceRenderer {

    private final DataManager _dm;
    private final StatefulReader _reader;
    private final boolean _respectVisibility;
    private final String _rootName;


    /**
     * Constructor.
     *
     * @param dm The data manager for this resource renderer.
     * @param reader The reader to use for looking up resources.
     * @param respectVisiblity Should we check a resource's visibility?
     * @param rootName The name of the root folder to look up resources from.
     */
    public DefaultResourceRenderer(final DataManager dm,
                                   final StatefulReader reader,
                                   final boolean respectVisiblity,
                                   final String rootName) {
        DBC.require().notNull(dm);
        DBC.require().notNull(reader);
        DBC.require().notNull(rootName);

        _dm = dm;
        _reader = reader;
        _respectVisibility = respectVisiblity;
        _rootName = rootName;
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final ResourcePath resourcePath) {
        final Resource resource =
            _reader.lookup(_rootName, resourcePath);
        return render(resource);
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final UUID resourceId) {
        throw new NotFoundException();
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final Resource resource) {
        if (resource == null) {
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            throw new NotFoundException();
        }

        switch (resource.type()) {

            case ALIAS:
                final Alias alias = resource.as(Alias.class);
                return renderAlias(alias);

            case PAGE:
                final Page page = resource.as(Page.class);
                return renderPage(page);

            case FILE:
                final File f = resource.as(File.class);
                return renderFile(f);

            case FOLDER:
                final Folder folder = resource.as(Folder.class);
                return renderFolder(folder);

            default:
                throw new NotFoundException();
        }
    }

    private Response renderAlias(final Alias alias) {
        throw new RedirectRequiredException(alias.target());
    }

    private Response renderFile(final File f) {
        final Response r = new Response();
        r.setDescription(f.description());
        r.setDisposition("inline; filename=\""+f.name()+"\"");
        r.setMimeType(f.mimeType().getPrimaryType(), f.mimeType().getSubType());
        r.setExpiry(Long.valueOf(0));
        r.setLength(Long.valueOf(f.size()));
        r.setBody(new FileBody(f, _dm));

        return r;
    }

    private Response renderFolder(final Folder folder) {
        if (folder.hasAliases()) {
            throw new RedirectRequiredException(folder.firstAlias());
        } else if (folder.hasPages()) {
            throw new RedirectRequiredException(folder.firstPage());
        }
        throw new NotFoundException();
    }

    private Response renderPage(final Page page) {
        final Response r = new Response();
        r.setExpiry(Long.valueOf(0));
        r.setCharSet("UTF-8");
        r.setMimeType("text", "html");
        r.setBody(new PageBody(page, Charset.forName("UTF-8"), _reader));

        return r;
    }
}
