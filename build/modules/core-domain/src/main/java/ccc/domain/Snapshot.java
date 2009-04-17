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
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ccc.commons.DBC;


/**
 * A snapshot is used to represent relevant details of a resource at a given
 * point in time.
 *
 * @author Civic Computing Ltd.
 */
public class Snapshot implements Serializable {

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
    void setDetail(final String detail) { // FIXME: Should be private.
        DBC.require().notNull(detail);
        try {
            _detail = new JSONObject(detail);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a string.
     */
    public void set(final String key, final String value) {
        try {
            _detail.put(key, value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Mutator.
     *
     * @param key The key.
     * @param snapshots The value, as a collection of snapshots.
     */
    public void set(final String key, final Collection<Snapshot> snapshots) {
        try {
            _detail.put(key, new JSONArray());
            for (final Snapshot s : snapshots) {
                _detail.append(key, s._detail);
            }
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Mutator.
     *
     * @param key The key.
     * @param bool The value, as a boolean.
     */
    public void set(final String key, final Boolean bool) {
        try {
            _detail.put(key, bool);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Mutator.
     *
     * @param key The key.
     * @param date The value, as a date.
     */
    public void set(final String key, final Date date) {
        try {
            _detail.put(
                key, (null==date) ? null : Long.valueOf(date.getTime()));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a uuid.
     */
    public void set(final String key, final UUID value) {
        try {
            _detail.put(
                key, (null==value) ? null : value.toString());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a long.
     */
    public void set(final String key, final long value) {
        try {
            _detail.put(key, value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Mutator.
     *
     * @param key The key.
     * @param value The value, as a map of strings.
     */
    public void set(final String key, final Map<String, String> value) {
        try {
            _detail.put(key, value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a string.
     */
    public String getString(final String key) {
        try {
            return _detail.getString(key);
        } catch (final JSONException e) {
            return null;
        }
    }

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a collection of snapshots.
     */
    public Collection<Snapshot> getSnapshots(final String key) {
        try {
            final JSONArray a = _detail.getJSONArray(key);
            final Collection<Snapshot> snapshots =
                new ArrayList<Snapshot>(a.length());
            for (int i=0; i<a.length(); i++) {
                snapshots.add(new Snapshot(a.getJSONObject(i)));
            }
            return snapshots;

        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a Date.
     */
    public Date getDate(final String key) {
        try {
            return new Date(_detail.getLong(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a boolean.
     */
    public Boolean getBool(final String key) {
        try {
            return Boolean.valueOf(_detail.getBoolean(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as a UUID.
     */
    public UUID getUuid(final String key) {
        return UUID.fromString(getString(key));
    }


    /**
     * Accessor.
     *
     * @param key The key for the value.
     * @return The value, as an int.
     */
    public int getInt(final String key) {
        try {
            return _detail.getInt(key);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
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
}
