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

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import ccc.api.DBC;
import ccc.api.ResourceType;
import ccc.content.exceptions.NotFoundException;
import ccc.content.exceptions.RedirectRequiredException;
import ccc.domain.Alias;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.Search;
import ccc.domain.Snapshot;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.domain.WCAware;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.StatefulReader;

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
        } else if (_respectVisibility && !resource.isVisible()) {
            throw new NotFoundException();
        }

        switch (resource.type()) {

            case ALIAS:
                final Alias alias = resource.as(Alias.class);
                return renderAlias(alias);

            case PAGE:
                final Page page = resource.as(Page.class);
                return renderPage(page, parameters);

            case FILE:
                final File f = resource.as(File.class);
                return renderFile(f);

            case FOLDER:
                final Folder folder = resource.as(Folder.class);
                return renderFolder(folder);

            case SEARCH:
                final Search search = resource.as(Search.class);
                return renderSearch(search, parameters);

            default:
                throw new NotFoundException();
        }
    }


    /** {@inheritDoc} */
    @Override
    public Response renderWorkingCopy(final Resource resource,
                                      final Map<String, String[]> parameters) {
        if (!_respectVisibility) {
            if (resource instanceof WCAware) {
                final WCAware<?> p = (WCAware<?>) resource;
                if (null!=p.workingCopy()) {
                    p.applySnapshot(
                        new RevisionMetadata(
                            new Date(),
                            User.SYSTEM_USER,
                            true,
                            "Updated."));
                } else {
                    LOG.warn("No working copy found for resource: "+resource);
                }
            }
        }
        return render(resource, parameters);
    }


    /** {@inheritDoc} */
    @Override
    public Response renderHistoricalVersion(
                                    final Resource resource,
                                    final Map<String, String[]> parameters) {
        if (!_respectVisibility) {
            if (resource instanceof WCAware) {

                final WCAware<?> sa = (WCAware<?>) resource;

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
                        final LogEntry le = _reader.lookup(v);
                        if (null==le) {
                            throw new NotFoundException();
                        } else if (resource.id().equals(le.subjectId())) {
                            sa.workingCopy(new Snapshot(le.detail()));
                            sa.applySnapshot(
                                new RevisionMetadata(
                                    new Date(),
                                    User.SYSTEM_USER,
                                    true,
                                    "Updated."));
                        } else {
                            throw new NotFoundException();
                        }
                    } catch (final NumberFormatException e) {
                        throw new NotFoundException();
                    }
                }
            }
        }
        return render(resource, parameters);
    }


    private Response renderAlias(final Alias alias) {
        throw new RedirectRequiredException(alias.target());
    }


    private Response renderFile(final File f) {
        // Factor into 'FileResponse' class.

        final Response r = new Response(new FileBody(f, _dm));
        r.setDescription(f.description());
        r.setDisposition("inline; filename=\""+f.name()+"\"");
        r.setMimeType(f.mimeType().getPrimaryType(), f.mimeType().getSubType());
        r.setLength(f.size());
        r.setExpiry(f.computeCache());

        return r;
    }


    private Response renderFolder(final Folder folder) {
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


    private Response renderPage(final Page page,
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


    private Response renderSearch(final Search search,
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
