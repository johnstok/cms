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
import java.util.HashMap;
import java.util.Map;

import ccc.api.Decimal;
import ccc.api.ID;
import ccc.api.Json;
import ccc.api.Jsonable;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
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
     * @param delegate The GWT class will delegate to.
     */
    public GwtJson(final JSONObject delegate) {
        _delegate = delegate;
    }

    /**
     * Constructor.
     *
     */
    public GwtJson() {
        _delegate = new JSONObject();
    }

    /**
     * Accessor.
     *
     * @return Returns the delegate.
     */
    private final JSONObject getDelegate() {
        return _delegate;
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
        _delegate.put(key, new JSONString(value));
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
    public void set(final String key, final Map<String, String> values) {
        final JSONObject strings = new JSONObject();
        for (final Map.Entry<String, String> value : values.entrySet()) {
            strings.put(value.getKey(), new JSONString(value.getValue()));
        }
        _delegate.put(key, strings);
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Jsonable value) {
        final GwtJson o = new GwtJson();
        value.toJson(o);
        _delegate.put(key, o.getDelegate());
    }

    /** {@inheritDoc} */
    @Override
    public void setStrings(final String key, final Collection<String> values) {
        final JSONArray strings = new JSONArray();
        int i=0;
        for (final String value : values) {
            strings.set(i, new JSONString(value));
            i++;
        }
        _delegate.put(key, strings);
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

    /** {@inheritDoc} */
    @Override // FIXME: Doesn't handle NULL
    public Map<String, String> getStringMap(final String key) {
        final Map<String, String> value = new HashMap<String, String>();
        final JSONObject o = _delegate.get(key).isObject();
        for (final String mapKey : o.keySet()) {
            value.put(mapKey, o.get(mapKey).isString().stringValue());
        }
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _delegate.toString();
    }
}
