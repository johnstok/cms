/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.types;

import java.io.Serializable;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable2;



/**
 * A finite period of time.
 *
 * @author Civic Computing Ltd.
 */
public final class Duration implements Serializable, Jsonable2 {

    private static final long SECONDS_IN_MINUTE = 60;
    private static final long SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_DAY = 86400;
    private long _time; // time in seconds


    /**
     * Constructor.
     */
    public Duration() { super(); }


    /**
     * Constructor.
     *
     * @param days Number of days
     * @param hours Number of hours
     * @param minutes Number of minutes
     * @param seconds Number of seconds
     */
    public Duration(final long days,
                    final long hours,
                    final long minutes,
                    final long seconds) {
        _time = seconds
                + minutes*SECONDS_IN_MINUTE
                + hours*SECONDS_IN_HOUR
                + days*SECONDS_IN_DAY;
    }


    /**
     * Constructor.
     *
     * @param time Time in seconds.
     */
    public Duration(final long time) {
        _time = time;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a duration.
     */
    public Duration(final Json json) {
        fromJson(json);
    }


    /**
     * Accessor for the time.
     *
     * @return Time in seconds.
     */
    public long time() {
        return _time;
    }


    /**
     * Accessor.
     *
     * @return Returns the time.
     */
    public long getTime() {
        return _time;
    }


    /**
     * Mutator.
     *
     * @param time The time to set.
     */
    public void setTime(final long time) {
        _time = time;
    }


    /**
     * Accessor for the second field.
     *
     * @return Value of the second field.
     */
    public long secondField() {
        return _time%SECONDS_IN_MINUTE;
     }


    /**
     * Accessor for the minute field.
     *
     * @return Value of the minute field.
     */
    public long minuteField() {
        return ((_time%SECONDS_IN_DAY)%SECONDS_IN_HOUR)/SECONDS_IN_MINUTE;
    }


    /**
     * Accessor for the hour field.
     *
     * @return Value of the hour field.
     */
    public long hourField() {
        return (_time%SECONDS_IN_DAY)/SECONDS_IN_HOUR;
    }


    /**
     * Accessor for the day field.
     *
     * @return Value of the second field.
     */
    public long dayField() {
        return _time/SECONDS_IN_DAY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        final int bitsInAnInt = 32;
        int result = 1;
        result = prime * result + (int) (_time ^ (_time >>> bitsInAnInt));
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Duration other = (Duration) obj;
        if (_time != other._time) {
            return false;
        }
        return true;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.SECONDS, Long.valueOf(_time));
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        setTime(json.getLong(JsonKeys.SECONDS).longValue());
    }
}
