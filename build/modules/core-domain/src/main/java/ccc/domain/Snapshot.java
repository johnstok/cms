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
package ccc.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.api.DBC;
import ccc.api.Decimal;
import ccc.api.ID;
import ccc.api.Json;
import ccc.api.Jsonable;


/**
 * A snapshot is used to represent relevant details of a resource at a given
 * point in time.
 *
 * @author Civic Computing Ltd.
 */
public class Snapshot implements Serializable, Json {

    private transient JSONObject _detail;

    /**
     * Constructor.
     *
     * @param detail The snapshot's detail.
     */
    public Snapshot(final String detail) {
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
    public Snapshot() {
        _detail = new JSONObject();
    }

    /**
     * Constructor.
     *
     * @param detail The JSON object this snapshot wraps.
     */
    private Snapshot(final JSONObject detail) {
        DBC.require().notNull(detail);

        _detail = detail;
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

    /** {@inheritDoc} */
    public void set(final String key, final String value) {
        try {
            _detail.put(key, value);
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
                final Snapshot s = new Snapshot();
                o.toJson(s);
                _detail.append(key, s._detail);
            }
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final Boolean bool) {
        try {
            _detail.put(key, bool);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final Date date) {
        try {
            _detail.put(
                key, (null==date) ? null : Long.valueOf(date.getTime()));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final ID value) {
        try {
            _detail.put(
                key, (null==value) ? null : value.toString());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final long value) {
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
            _detail.put(key, (null==value) ? null : value.toString());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public void set(final String key, final Map<String, String> value) {
        try {
            _detail.put(key, value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void set(final String key, final Jsonable value) {
        try {
            final Snapshot s = new Snapshot();
            value.toJson(s);
            _detail.put(key, s._detail);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public String getString(final String key) {
        try {
            return _detail.getString(key);
        } catch (final JSONException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    public Collection<Json> getCollection(final String key) {
        try {
            final JSONArray a = _detail.getJSONArray(key);
            final Collection<Json> snapshots =
                new ArrayList<Json>(a.length());
            for (int i=0; i<a.length(); i++) {
                snapshots.add(new Snapshot(a.getJSONObject(i)));
            }
            return snapshots;

        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public Date getDate(final String key) {
        try {
            return new Date(_detail.getLong(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public Boolean getBool(final String key) {
        try {
            return Boolean.valueOf(_detail.getBoolean(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /** {@inheritDoc} */
    public ID getId(final String key) {
        return new ID(getString(key));
    }


    /** {@inheritDoc} */
    public int getInt(final String key) {
        try {
            return _detail.getInt(key);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }


    /** {@inheritDoc} */
    public Decimal getDecimal(final String key) {
        return new Decimal(getString(key));
    }

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
    public Json getJson(final String string) {
        try {
            return new Snapshot(_detail.getJSONObject(string));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }
}
