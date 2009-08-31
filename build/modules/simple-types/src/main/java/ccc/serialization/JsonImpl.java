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
package ccc.serialization;

import static org.json.JSONObject.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.types.DBC;
import ccc.types.Decimal;
import ccc.types.ID;


/**
 * Default implementation of the {@link Json} interface.
 *
 * @author Civic Computing Ltd.
 */
public class JsonImpl implements Serializable, Json {

    private transient JSONObject _detail;

    /**
     * Constructor.
     *
     * @param detail The snapshot's detail.
     */
    public JsonImpl(final String detail) {
        DBC.require().notNull(detail);
        try {
            _detail = new JSONObject(detail);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Constructor.
     */
    public JsonImpl() {
        _detail = new JSONObject();
    }

    /**
     * Constructor.
     *
     * @param detail The JSON object this snapshot wraps.
     */
    public JsonImpl(final JSONObject detail) {
        DBC.require().notNull(detail);

        _detail = detail;
    }

    /**
     * Constructor.
     *
     * @param jsonable The object to convert to JSON.
     */
    public JsonImpl(final Jsonable jsonable) {
        this();
        DBC.require().notNull(jsonable);
        jsonable.toJson(this);
    }

    /**
     * Constructor.
     *
     * @param map The map to convert into a snapshot.
     */
    public JsonImpl(final Map<String, String> map) {
        this();
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Get the internal state of the snapshot.
     *
     * @return The snapshot's state, as JSON a string.
     */
    public String getDetail() {
        return _detail.toString();
    }

    /**
     * Set the internal state of the snapshot from a JSON string.
     *
     * @param detail The JSON string.
     */
    private void setDetail(final String detail) {
        DBC.require().notNull(detail);
        try {
            _detail = new JSONObject(detail);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    private <T> T fixNull(final T object) {
        if (NULL.equals(object)) {
            return null;
        }
        return object;
    }


    /* ====================================================================
     * Mutators.
     * ================================================================== */

    /** {@inheritDoc} */
    public void set(final String key, final String value) {
        try {
            _detail.put(key, (null==value) ? NULL : value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key,
                    final Collection<? extends Jsonable> snapshots) {
        try {
            _detail.put(key, new JSONArray());
            for (final Jsonable o : snapshots) {
                final JsonImpl s = new JsonImpl();
                o.toJson(s);
                _detail.append(key, s._detail);
            }
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void setStrings(final String key,
                           final Collection<String> value) {
        try {
            if (null==value) {
                _detail.put(key, NULL);
            } else {
                _detail.put(key, new JSONArray());
                for (final String o : value) {
                    _detail.append(key, o);
                }
            }
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final Boolean value) {
        try {
            _detail.put(key, (null==value) ? NULL : value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final Date date) {
        try {
            _detail.put(
                key, (null==date) ? NULL : Long.valueOf(date.getTime()));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final ID value) {
        try {
            _detail.put(
                key, (null==value) ? NULL : value.toString());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final Long value) {
        try {
            _detail.put(key, value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }


    /** {@inheritDoc} */
    public void set(final String key, final Decimal value) {
        try {
            // Javascript doesn't support decimals - store as a string.
            _detail.put(key, (null==value) ? NULL : value.toString());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final Map<String, String> value) {
        try {
            _detail.put(key, (null==value) ? NULL : value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Jsonable value) {
        try {
            if (null==value) {
                _detail.put(key, NULL);
            } else {
                final JsonImpl s = new JsonImpl();
                value.toJson(s);
                _detail.put(key, s._detail);
            }
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }


    /* ====================================================================
     * Accessors.
     * ================================================================== */

    /** {@inheritDoc} */
    public String getString(final String key) {
        try {
            return (String) fixNull(_detail.get(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public Collection<Json> getCollection(final String key) {
        try {
            final JSONArray a =
                (JSONArray) fixNull(_detail.get(key));
            if (null==a) {
                return null;
            }
            final Collection<Json> snapshots =
                new ArrayList<Json>(a.length());
            for (int i=0; i<a.length(); i++) {
                snapshots.add(new JsonImpl(a.getJSONObject(i)));
            }
            return snapshots;

        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public Date getDate(final String key) {
        try {
            final Number n = (Number) fixNull(_detail.get(key));
            if (null==n) {
                return null;
            }
            return new Date(n.longValue());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public Boolean getBool(final String key) {
        try {
            return (Boolean) fixNull(_detail.get(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public ID getId(final String key) {
        final String s = getString(key);
        if (null==s) {
            return null;
        }
        return new ID(s);
    }

    /** {@inheritDoc} */
    public Integer getInt(final String key) {
        try {
            return (Integer) fixNull(_detail.get(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public Decimal getDecimal(final String key) {
        final String s = getString(key);
        if (null==s) {
            return null;
        }
        return new Decimal(s);
    }

    /** {@inheritDoc} */
    @Override
    public Json getJson(final String string) {
        try {
            final JSONObject o = (JSONObject) fixNull(_detail.get(string));
            if (null==o) {
                return null;
            }
            return new JsonImpl(o);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }


    /* ====================================================================
     * Serialization methods.
     * ================================================================== */

    private void readObject(final ObjectInputStream aInputStream)
                                   throws IOException, ClassNotFoundException  {
        aInputStream.defaultReadObject();
        setDetail(aInputStream.readUTF());
    }

    private void writeObject(final ObjectOutputStream aOutputStream)
                                                            throws IOException {
        aOutputStream.defaultWriteObject();
        aOutputStream.writeUTF(getDetail());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> getStrings(final String key) {
        try {
            final JSONArray a =
                (JSONArray) fixNull(_detail.get(key));
            if (null==a) {
                return null;
            }
            final Collection<String> strings =
                new ArrayList<String>(a.length());
            for (int i=0; i<a.length(); i++) {
                strings.add((String) fixNull(a.get(i)));
            }
            return strings;

        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Long getLong(final String key) {
        try {
            final Number n = (Number) fixNull(_detail.get(key));
            return Long.valueOf(n.longValue());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> getStringMap(final String key) {
        try {
            final JSONObject o =
                (JSONObject) fixNull(_detail.get(key));
            if (null==o) {
                return null;
            }

            final Map<String, String> stringMap = new HashMap<String, String>();
            for (final Iterator<String> i = o.keys(); i.hasNext();) {
                final String mapKey = i.next();
                final String mapValue = (String) fixNull(o.get(mapKey));
                stringMap.put(mapKey, mapValue);
            }
            return stringMap;

        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }
}
