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
import java.util.Map;

import ccc.commons.DBC;
import ccc.domain.Alias;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceType;
import ccc.domain.Search;
import ccc.domain.Snapshot;
import ccc.services.DataManager;
import ccc.services.ISearch;
import ccc.services.StatefulReader;

/**
 * Default implementation of the {@link Renderer} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultRenderer
    implements
        Renderer {

    private final DataManager _dm;
    private final ISearch _search;
    private final boolean _respectVisibility;
    private final StatefulReader _reader;

    /**
     * Constructor.
     *
     * @param dm The data manager for this resource renderer.
     * @param respectVisiblity Should we check a resource's visibility?
     */
    public DefaultRenderer(final DataManager dm,
                           final ISearch searchEngine,
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
            if (resource instanceof Page) {
                final Page p = (Page) resource;
                if (null!=p.workingCopy()) {
                    p.applySnapshot(p.workingCopy());
                }
            }
        }
        return render(resource, parameters);
    }


    /** {@inheritDoc} */
    @Override
    public Response renderHistoricalVersion(final Resource resource,
                                            final Map<String, String[]> parameters) {
        if (!_respectVisibility) {
            if (resource instanceof Page) {
                final Page p = (Page) resource;

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
                            p.applySnapshot(new Snapshot(le.detail()));
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
        final Response r = new Response();
        r.setExpiry(Long.valueOf(0));
        r.setCharSet("UTF-8");
        r.setMimeType("text", "html");
        r.setBody(new PageBody(page, Charset.forName("UTF-8"), parameters));

        return r;
    }


    private Response renderSearch(final Search search,
                                  final Map<String, String[]> parameters) {
        final Response r = new Response();
        r.setExpiry(Long.valueOf(0));
        r.setCharSet("UTF-8");
        r.setMimeType("text", "html");
        r.setBody(
            new SearchBody(
                search,
                Charset.forName("UTF-8"),
                _search,
                parameters.get("q")[0]));

        return r;
    }
}
