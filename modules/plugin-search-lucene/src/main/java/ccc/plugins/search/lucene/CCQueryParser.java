/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.search.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import ccc.commons.Exceptions;

/**
 * CC-specific query parser.
 *
 * @author Civic Computing Ltd.
 */
final class CCQueryParser
    extends
        QueryParser {

    /**
     * Constructs a query parser.
     *
     * @param matchVersion  Lucene version to match.
     * @param f             The default field for query terms.
     * @param a             Used to find terms in the query text.
     */
    CCQueryParser(final Version matchVersion,
                          final String f,
                          final Analyzer a) {
        super(matchVersion, f, a);
    }


    /** {@inheritDoc} */
    @Override
    protected Query newRangeQuery(final String f,
                                  final String min,
                                  final String max,
                                  final boolean inclusive) {
        try {
            return NumericRangeQuery.newLongRange(f,
                Long.valueOf(min),
                Long.valueOf(max),
                true,
                true);
        } catch (final NumberFormatException e) {
            Exceptions.swallow(e);
        }

        try {
            return NumericRangeQuery.newDoubleRange(f,
                Double.valueOf(min),
                Double.valueOf(max),
                true,
                true);
        } catch (final NumberFormatException e) {
            Exceptions.swallow(e);
        }

        return super.newRangeQuery(f, min, max, inclusive);
    }
}
