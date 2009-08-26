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

package ccc.rendering.response;

import java.util.Map;

import ccc.domain.Resource;
import ccc.rendering.NotFoundException;
import ccc.rendering.Response;
import ccc.rendering.StatefulReader;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.snapshots.ResourceSnapshot;
import ccc.types.DBC;

/**
 * Default implementation of the {@link Renderer} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultRenderer
    implements
        Renderer {

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
        return renderResourceSnaphot(resource.forCurrentRevision(), parameters);
    }


    private Response renderResourceSnaphot(final ResourceSnapshot resource,
                             final Map<String, String[]> parameters) {

        if (resource == null) {
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            throw new NotFoundException();
        }

        return resource.render(parameters, _search, _reader, _dm);
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

        return renderResourceSnaphot(r, parameters);
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

        return renderResourceSnaphot(snapshot, parameters);
    }
}
