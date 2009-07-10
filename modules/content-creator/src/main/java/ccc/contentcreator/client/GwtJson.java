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
package ccc.contentcreator.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import ccc.api.Decimal;
import ccc.api.ID;
import ccc.api.Json;
import ccc.api.Jsonable;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GwtJson
    implements
        Json {

    private final JSONObject _delegate;


    /**
     * Constructor.
     *
     * @param delegate
     */
    public GwtJson(final JSONObject delegate) {
        _delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public Boolean getBool(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }
        return Boolean.valueOf(value.isBoolean().booleanValue());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<Json> getCollection(final String key) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Date getDate(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }
        // TODO: Handle non integers
        return new Date((long) value.isNumber().doubleValue());
    }

    /** {@inheritDoc} */
    @Override
    public Decimal getDecimal(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }
        return new Decimal(value.isString().stringValue());
    }

    /** {@inheritDoc} */
    @Override
    public ID getId(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }
        return new ID(value.isString().stringValue());
    }

    /** {@inheritDoc} */
    @Override
    public Integer getInt(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }
        // TODO: Handle non integers; handle values larger than maxInt, etc.
        return Integer.valueOf((int) value.isNumber().doubleValue());
    }

    /** {@inheritDoc} */
    @Override
    public Json getJson(final String key) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public String getString(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }
        return value.isString().stringValue();
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final String value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key,
                    final Collection<? extends Jsonable> values) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Boolean bool) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Date date) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final ID value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Long value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Decimal value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Map<String, String> value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Jsonable value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void setStrings(final String key, final Collection<String> value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> getStrings(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }

        final Collection<String> strings = new ArrayList<String>();

        final JSONArray array = value.isArray();
        for (int i=0; i<array.size(); i++) {
            final JSONValue arrayElement = array.get(i);
            if (null!=arrayElement.isNull()) {
                strings.add(null);
            } else {
                strings.add(arrayElement.isString().stringValue());
            }
        }

        return strings;
    }

    /** {@inheritDoc} */
    @Override
    public Long getLong(final String key) {
        final JSONValue value = _delegate.get(key);
        if (null==value) {
            throw new RuntimeException("Missing key: "+key);
        } else if (null!=value.isNull()) {
            return null;
        }
        // TODO: Handle non integers; handle values larger than maxLong, etc.
        return Long.valueOf((long) value.isNumber().doubleValue());
    }
}
