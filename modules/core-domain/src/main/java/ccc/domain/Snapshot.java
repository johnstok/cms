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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
public class Snapshot {

    private JSONObject _detail;

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
     *
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
     * Accessor.
     *
     * @return The snapshot's detail, as a string.
     */
    public String getDetail() {
        return _detail.toString();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param detail
     */
    void setDetail(final String detail) {
        DBC.require().notNull(detail);
        try {
            _detail = new JSONObject(detail);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param key
     * @param value
     */
    public void set(final String key, final String value) {
        try {
            _detail.put(key, value);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param key
     * @param snapshots
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
     * TODO: Add a description of this method.
     *
     * @param key
     * @param bool
     */
    public void set(final String key, final Boolean bool) {
        try {
            _detail.put(key, bool);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param key
     * @param date
     */
    public void set(final String key, final Date date) {
        try {
            _detail.put(key, (null==date) ? null : date.getTime());
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param key
     * @return
     */
    public String getString(final String key) {
        try {
            return _detail.getString(key);
        } catch (final JSONException e) {
            return null;
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @return
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
     * TODO: Add a description of this method.
     *
     * @param key
     * @return
     */
    public Date getDate(final String key) {
        try {
            return new Date(_detail.getLong(key));
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param key
     * @return
     */
    public Boolean getBool(final String key) {
        try {
            return _detail.getBoolean(key);
        } catch (final JSONException e) {
            throw new InvalidSnapshotException(e);
        }
    }
}
