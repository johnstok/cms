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
package ccc.snapshots;

import java.util.Map;

import ccc.api.template.StatefulReader;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.rendering.Response;
import ccc.rendering.SearchBody;
import ccc.services.DataManager;
import ccc.services.SearchEngine;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class SearchSnapshot
    extends
        ResourceSnapshot {

    /**
     * Constructor.
     *
     * @param delegate
     */
    public SearchSnapshot(final Resource delegate) {
        super(delegate);
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataManager dm) {
        final Template t =
            computeTemplate(SearchBody.BUILT_IN_SEARCH_TEMPLATE);
        final Response r =
            new Response(
                new SearchBody(
                    reader,
                    search,
                    t,
                    parameters));
        r.setCharSet("UTF-8");
        r.setMimeType(t.mimeType().getPrimaryType(), t.mimeType().getSubType());
        r.setExpiry(computeCache());

        return r;
    }

}
