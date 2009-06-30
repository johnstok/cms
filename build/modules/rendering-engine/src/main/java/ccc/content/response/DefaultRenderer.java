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

package ccc.content.response;

import java.util.Map;

import org.apache.log4j.Logger;

import ccc.api.DBC;
import ccc.api.ResourceType;
import ccc.content.exceptions.NotFoundException;
import ccc.content.exceptions.RedirectRequiredException;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.StatefulReader;
import ccc.snapshots.AliasSnapshot;
import ccc.snapshots.FileSnapshot;
import ccc.snapshots.FolderSnapshot;
import ccc.snapshots.PageSnapshot;
import ccc.snapshots.ResourceSnapshot;

/**
 * Default implementation of the {@link Renderer} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultRenderer
    implements
        Renderer {

    private static final Logger LOG =
        Logger.getLogger(DefaultRenderer.class.getName());

    private final DataManager _dm;
    private final SearchEngine _search;
    private final boolean _respectVisibility;
    private final StatefulReader _reader;

    /**
     * Constructor.
     *
     * @param dm The data manager for this resource renderer.
     * @param respectVisiblity Should we check a resource's visibility?
     * @param searchEngine The search engine to use.
     * @param reader The resource reader to use.
     */
    public DefaultRenderer(final DataManager dm,
                           final SearchEngine searchEngine,
                           final StatefulReader reader,
                           final boolean respectVisiblity) {
        DBC.require().notNull(dm);
        DBC.require().notNull(searchEngine);
        DBC.require().notNull(reader);

        _dm = dm;
        _search = searchEngine;
        _reader = reader;
        _respectVisibility = respectVisiblity;
    }


    /** {@inheritDoc} */
    @Override
    public Response render(final Resource resource,
                           final Map<String, String[]> parameters) {
        if (resource == null) {
            throw new NotFoundException();
        }
        return render_(resource.forCurrentRevision(), parameters);
    }


    private Response render_(final ResourceSnapshot resource,
                             final Map<String, String[]> parameters) {

        if (resource == null) {
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            throw new NotFoundException();
        }

        switch (resource.type()) {

            case ALIAS:
                final AliasSnapshot alias = (AliasSnapshot) resource;
                return renderAlias(alias);

            case PAGE:
                final PageSnapshot page = (PageSnapshot) resource;
                return renderPage(page, parameters);

            case FILE:
                final FileSnapshot f = (FileSnapshot) resource;
                return renderFile(f);

            case FOLDER:
                final FolderSnapshot folder = (FolderSnapshot) resource;
                return renderFolder(folder);

            case SEARCH:
                return renderSearch(resource, parameters);

            default:
                throw new NotFoundException();
        }
    }


    /** {@inheritDoc} */
    @Override
    public Response renderWorkingCopy(final Resource resource,
                                      final Map<String, String[]> parameters) {
        if (resource == null) {
            throw new NotFoundException();
        }

        final ResourceSnapshot r =
            (_respectVisibility)
                ? resource.forCurrentRevision()
                : resource.forWorkingCopy();

        return render_(r, parameters);
    }


    /** {@inheritDoc} */
    @Override
    public Response renderHistoricalVersion(
                                    final Resource resource,
                                    final Map<String, String[]> parameters) {
        if (resource == null) {
            throw new NotFoundException();
        }

        ResourceSnapshot snapshot = resource.forCurrentRevision();

        if (!_respectVisibility) {
            if (!parameters.containsKey("v")) {
                throw new NotFoundException();
            }

            final String[] vStrings = parameters.get("v");
            if (null==vStrings) {
                throw new NotFoundException();
            } else if (1 != vStrings.length){
                throw new NotFoundException();
            } else {
                try {
                    final long v = new Long(vStrings[0]).intValue();
                    if (v<0) {
                        throw new NotFoundException();
                    }

                    snapshot = resource.forSpecificRevision((int) v);

                } catch (final NumberFormatException e) {
                    throw new NotFoundException();
                }
            }
        }

        return render_(snapshot, parameters);
    }


    private Response renderAlias(final AliasSnapshot alias) {
        throw new RedirectRequiredException(alias.target());
    }


    private Response renderFile(final FileSnapshot f) {
        // Factor into 'FileResponse' class.

        final Response r = new Response(new FileBody(f, _dm));
        r.setDescription(f.description());
        r.setDisposition("inline; filename=\""+f.name()+"\"");
        r.setMimeType(f.mimeType().getPrimaryType(), f.mimeType().getSubType());
        r.setLength(f.size());
        r.setExpiry(f.computeCache());

        return r;
    }


    private Response renderFolder(final FolderSnapshot folder) {
        if (folder.indexPage() != null) {
            throw new RedirectRequiredException(folder.indexPage());
        }

        for (final Resource r : folder.entries()) {
            if (ResourceType.PAGE.equals(r.type())
                && r.isPublished()) {
                throw new RedirectRequiredException(r);
            }
        }
        throw new NotFoundException();
    }


    private Response renderPage(final PageSnapshot page,
                                final Map<String, String[]> parameters) {
        // Factor into 'PageResponse' class.

        final Template t =
            page.computeTemplate(PageBody.BUILT_IN_PAGE_TEMPLATE);
        final Response r =
            new Response(new PageBody(page, _reader, t, parameters));
        r.setCharSet("UTF-8");
        r.setMimeType(t.mimeType().getPrimaryType(), t.mimeType().getSubType());
        r.setExpiry(page.computeCache());

        return r;
    }


    private Response renderSearch(final ResourceSnapshot search,
                                  final Map<String, String[]> parameters) {
        // Factor into 'SearchResponse' class.

        String searchQuery = "";
        final String[] qParams = parameters.get("q");
        if (qParams != null && qParams.length != 0) {
            searchQuery = qParams[0];
        }

        int pageNumber = 0;
        final String[] pParams = parameters.get("p");
        if (pParams != null && pParams.length != 0) {
            try {
                pageNumber = Integer.parseInt(pParams[0]);
            } catch (final NumberFormatException e) {
                LOG.debug("Not a number");
            }
        }

        final Template t =
            search.computeTemplate(SearchBody.BUILT_IN_SEARCH_TEMPLATE);
        final Response r =
            new Response(
                new SearchBody(
                    search,
                    _reader,
                    _search,
                    searchQuery,
                    t,
                    pageNumber));
        r.setCharSet("UTF-8");
        r.setMimeType(t.mimeType().getPrimaryType(), t.mimeType().getSubType());
        r.setExpiry(search.computeCache());

        return r;
    }
}
