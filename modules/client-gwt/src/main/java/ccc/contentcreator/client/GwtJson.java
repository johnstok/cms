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

import ccc.serialization.Json;
import ccc.serialization.Jsonable;
import ccc.types.Decimal;
import ccc.types.ID;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


/**
 * Client side implementation of the {@link Json} interface.
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
     * Constructor.
     *
     * @param properties A map of properties for this JSON object.
     */
    public GwtJson(final Map<String, String> properties) {
        this();
        for (final Map.Entry<String, String> prop : properties.entrySet()) {
            _delegate.put(prop.getKey(), new JSONString(prop.getValue()));
        }
    }

    /**
     * Accessor.
     *
     * @return Returns the delegate.
     */
    private JSONObject getDelegate() {
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
        final Collection<Json> value = new ArrayList<Json>();
        final JSONArray a = _delegate.get(key).isArray();
        for (int i=0; i<a.size(); i++) {
            value.add(new GwtJson(a.get(i).isObject()));
        }
        return value;
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
        return new GwtJson(_delegate.get(key).isObject());
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
        _delegate.put(
            key,
            (null==value) ? JSONNull.getInstance() : new JSONString(value));
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key,
                    final Collection<? extends Jsonable> values) {
        final JSONArray value = new JSONArray();
        int i=0;
        for (final Jsonable j : values) {
            final GwtJson tmp = new GwtJson();
            j.toJson(tmp);
            value.set(i, tmp.getDelegate());
            i++;
        }
        _delegate.put(key, value);
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Boolean bool) {
        _delegate.put(
            key,
            (null==bool)
                ? JSONNull.getInstance()
                : JSONBoolean.getInstance(bool.booleanValue()));
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Date date) {
        _delegate.put(
            key,
            (null==date)
                ? JSONNull.getInstance()
                : new JSONNumber(date.getTime()));
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final ID value) {
        _delegate.put(
            key,
            (null==value)
                ? JSONNull.getInstance()
                : new JSONString(value.toString()));
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Long value) {
        _delegate.put(
            key,
            (null==value)
                ? JSONNull.getInstance()
                : new JSONNumber(value.doubleValue()));
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Decimal value) {
        _delegate.put(
            key,
            (null==value)
                ? JSONNull.getInstance()
                : new JSONString(value.toString()));
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Map<String, String> values) {
        if (null==values) {
            _delegate.put(key, JSONNull.getInstance());
        } else {
            final JSONObject strings = new JSONObject();
            for (final Map.Entry<String, String> value : values.entrySet()) {
                strings.put(value.getKey(), new JSONString(value.getValue()));
            }
            _delegate.put(key, strings);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Jsonable value) {
        if (null==value) {
            _delegate.put(key, JSONNull.getInstance());
        } else {
            final GwtJson o = new GwtJson();
            value.toJson(o);
            _delegate.put(key, o.getDelegate());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setStrings(final String key, final Collection<String> values) {
        if (null==values) {
            _delegate.put(key, JSONNull.getInstance());
        } else {
            final JSONArray strings = new JSONArray();
            int i=0;
            for (final String value : values) {
                strings.set(i, new JSONString(value));
                i++;
            }
            _delegate.put(key, strings);
        }
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
    @Override
    public Map<String, String> getStringMap(final String key) {
        final Map<String, String> value = new HashMap<String, String>();
        final JSONValue v = _delegate.get(key);
        if (null!=v.isNull()) {
            return null;
        }
        final JSONObject o = v.isObject();
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
