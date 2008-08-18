package ccc.services.ejb3;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Adaptor for the {@link Query} interface, useful for testing.
 *
 * @author Civic Computing Ltd
 */
class QueryAdaptor implements Query {

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdate() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<?> getResultList() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSingleResult() {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setFirstResult(final int arg0) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setFlushMode(final FlushModeType arg0) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setHint(final String arg0, final Object arg1) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setMaxResults(final int arg0) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setParameter(final String arg0, final Object arg1) {
        /* NO-OP */
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setParameter(final int arg0, final Object arg1) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setParameter(final String arg0,
                              final Date arg1,
                              final TemporalType arg2) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setParameter(final String arg0,
                              final Calendar arg1,
                              final TemporalType arg2) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setParameter(final int arg0,
                              final Date arg1,
                              final TemporalType arg2) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Query setParameter(final int arg0,
                              final Calendar arg1,
                              final TemporalType arg2) {

        throw new UnsupportedOperationException("Method not implemented.");
    }
}
