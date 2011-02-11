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
package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.api.synchronous.SearchEngine;
import ccc.api.synchronous.SearchEngine2;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;
import ccc.commands.SearchReindexCommand;
import ccc.search.SearchHelper;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Local(SearchEngine2.class)
public class SearchEngineEJB
    extends
        AbstractEJB
    implements
        SearchEngine2 {

    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    @PersistenceContext private EntityManager _em;



    /** Constructor. */
    public SearchEngineEJB() { super(); }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final int resultCount,
                             final int page) {
        return
            new SearchHelper(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createDataRepository(),
                getRepoFactory().createSettingsRepository(),
                currentUser())
            .find(searchTerms, resultCount, page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final String sort,
                             final SortOrder order,
                             final int resultCount,
                             final int page) {
        return
            new SearchHelper(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createDataRepository(),
                getRepoFactory().createSettingsRepository(),
                currentUser())
            .find(searchTerms, sort, order, resultCount, page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult similar(final String uuid,
                                final int noOfResultsPerPage,
                                final int page) {
        return
            new SearchHelper(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createDataRepository(),
                getRepoFactory().createSettingsRepository(),
                currentUser())
            .similar(uuid, noOfResultsPerPage, page);
    }


    /** {@inheritDoc} */
    @Override
    public void index() {
        execute(new SearchReindexCommand(getRepoFactory(), getProducer()));
    }

}
