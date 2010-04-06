/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.domain.Comment;
import ccc.domain.Resource;
import ccc.serialization.JsonKeys;
import ccc.types.CommentStatus;
import ccc.types.DBC;
import ccc.types.SortOrder;


/**
 * JPA implementation of a comment repository.
 *
 * @author Civic Computing Ltd.
 */
public class CommentRepositoryImpl
    implements
        CommentRepository {

    private Repository _repo;


    /**
     * Constructor.
     *
     * @param repo The repository used to perform queries.
     */
    public CommentRepositoryImpl(final Repository repo) {
        DBC.require().notNull(repo);
        _repo = repo;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public CommentRepositoryImpl(final EntityManager em) {
        this(new JpaRepository(em));
    }


    /** {@inheritDoc} */
    @Override public void create(final Comment comment) {
        _repo.create(comment);
    }


    /** {@inheritDoc} */
    @Override
    public void delete(final UUID commentId) {
        _repo.delete(retrieve(commentId));
    }


    /** {@inheritDoc} */
    @Override
    public Comment retrieve(final UUID commentId) {
        return _repo.find(Comment.class, commentId);
    }


    /** {@inheritDoc} */
    @Override
    public List<Comment> list(final Resource resource,
                              final CommentStatus status,
                              final String sort,
                              final SortOrder sortOrder,
                              final int pageNo,
                              final int pageSize) {

        final StringBuffer query = new StringBuffer();
        final List<Object> params = new ArrayList<Object>();

        query.append("from ccc.domain.Comment c");

        if (null!=status) {
            query.append(" where c._status = ?");
            params.add(status);
        }

        if (null!=resource) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" c._resource = ?");
            params.add(resource);
        }

        query.append(" order by c.");
        query.append(mapSortColumn(sort));
        query.append(" ");
        query.append(sortOrder.name());

        return
            _repo.listDyn(
                query.toString(),
                Comment.class,
                pageNo,
                pageSize,
                params.toArray());
    }


    /** {@inheritDoc} */
    @Override
    public long count(final Resource resource, final CommentStatus status) {

        final StringBuffer query = new StringBuffer();
        final List<Object> params = new ArrayList<Object>();

        query.append("select count(*) from ccc.domain.Comment c");

        if (null!=status) {
            query.append(" where c._status = ?");
            params.add(status);
        }

        if (null!=resource) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" c._resource = ?");
            params.add(resource);
        }

        return _repo.scalarLong(query.toString(), params.toArray());
    }


    private String mapSortColumn(final String sort) {
        if (JsonKeys.AUTHOR.equals(sort)) {
            return "_author";
        } else if (JsonKeys.URL.equals(sort)) {
            return "_url";
        } else if (JsonKeys.DATE_CREATED.equals(sort)) {
            return "_timestamp";
        }
        return "_status";
    }
}
