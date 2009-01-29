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

import java.util.Collection;

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
     * @param key
     * @param value
     */
    public void add(final String key, final String value) {
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
    public void add(final String key, final Collection<Snapshot> snapshots) {
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

}
