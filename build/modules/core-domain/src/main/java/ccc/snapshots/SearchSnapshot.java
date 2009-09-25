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

import ccc.domain.Search;
import ccc.domain.Template;
import ccc.persistence.FileRepository;
import ccc.rendering.Response;
import ccc.rendering.SearchBody;
import ccc.rendering.StatefulReader;
import ccc.search.SearchEngine;


/**
 * A read-only snapshot of a search resource.
 *
 * @author Civic Computing Ltd.
 */
public class SearchSnapshot
    extends
        ResourceSnapshot {

    /**
     * Constructor.
     *
     * @param delegate The file this snapshot wraps.
     */
    public SearchSnapshot(final Search delegate) {
        super(delegate);
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final FileRepository dm) {
        final Template t =
            computeTemplate(SearchBody.BUILT_IN_SEARCH_TEMPLATE);
        final Response r =
            new Response(
                new SearchBody(
                    reader,
                    search,
                    this,
                    t,
                    parameters));
        r.setCharSet("UTF-8");
        r.setMimeType(t.mimeType().getPrimaryType(), t.mimeType().getSubType());
        r.setExpiry(computeCache());

        return r;
    }

}
