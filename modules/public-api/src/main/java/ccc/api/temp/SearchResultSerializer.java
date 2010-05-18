/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.temp;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.SearchResult;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link SearchResult}s.
 *
 * @author Civic Computing Ltd.
 */
public class SearchResultSerializer implements Serializer<SearchResult> {


    /** {@inheritDoc} */
    @Override
    public SearchResult read(final Json json) {
        if (null==json) { return null; }

        final Set<UUID> hits = new HashSet<UUID>();
        for (final String hit : json.getStrings("hits")) {
            hits.add(UUID.fromString(hit));
        }

        final int page = json.getInt("page");
        final int size = json.getInt("size");
        final int pageSize = json.getInt("page-size");
        final String terms = json.getString("terms");

        final SearchResult r =
            new SearchResult(hits, size, pageSize, terms, page);

        return r;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final SearchResult instance) {
        if (null==instance) { return null; }

        final Set<String> hits = new HashSet<String>();
        for (final UUID hit : instance.hits()) {
            hits.add(hit.toString());
        }

        json.set("page", Long.valueOf(instance.getPageNo()));
        json.set("size", Long.valueOf(instance.totalResults()));
        json.set("page-size", Long.valueOf(instance.noOfResultsPerPage()));
        json.set("terms", instance.getTerms());
        json.setStrings("hits", hits);

        return json;
    }
}
