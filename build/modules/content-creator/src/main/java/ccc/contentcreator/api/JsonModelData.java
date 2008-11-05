/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class JsonModelData
    implements
        ModelData {

    private final JSONObject _object;

    /**
     * Constructor.
     *
     * @param object The JSONObject that backs this model data.
     */
    public JsonModelData(final JSONObject object) {
        _object = object;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public <X> X get(final String property) {
        final JSONValue value = _object.get(property);

        if (null == value) {
            return null;
        } else if (null != value.isNull()) {
            return null;
        } else if (null != value.isNumber()) {
            return (X) Double.valueOf(value.isNumber().doubleValue());
        } else if (null != value.isBoolean()) {
            return (X) Boolean.valueOf(value.isBoolean().booleanValue());
        } else if (null != value.isString()) {
            return (X) value.isString().stringValue();
        } else { // Array or Object
            throw new UnsupportedOperationException(
                "Can't convert arrays or objects");
        }
    }

    /** {@inheritDoc} */
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    public Collection<String> getPropertyNames() {
        return _object.keySet();
    }

    /** {@inheritDoc} */
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /** {@inheritDoc} */
    @SuppressWarnings("boxing")
    public <X> X set(final String property, final X value) {
        final X oldValue = this.<X>get(property);

        if (null == value) {
            _object.put(property, JSONNull.getInstance());
            return oldValue;
        } else if (value instanceof Double) {
            _object.put(property, new JSONNumber((Double) value));
            return oldValue;
        } else if (value instanceof Boolean) {
            _object.put(property, JSONBoolean.getInstance((Boolean) value));
            return oldValue;
        } else if (value instanceof String) {
            _object.put(property, new JSONString((String) value));
            return oldValue;
        } else {
            throw new UnsupportedOperationException(
                "Can't set value of type"+value.getClass());
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param jsonValue
     * @return
     */
    public static final List<JsonModelData> fromArray(final JSONValue jsonValue) {
        final List<JsonModelData> modelData = new ArrayList<JsonModelData>();
        final JSONArray array = jsonValue.isArray();
        for (int i=0; i<array.size(); i++) {
            modelData.add(fromObject(array.get(i)));
        }
        return modelData;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param jsonValue
     * @return
     */
    public static final JsonModelData fromObject(final JSONValue jsonValue) {
        return new JsonModelData(jsonValue.isObject());
    }
}
