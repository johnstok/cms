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
package ccc.services.api;

import java.io.Serializable;

import ccc.annotations.ValueObject;



/**
 * A finite period of time.
 *
 * @author Civic Computing Ltd.
 */
@ValueObject
public final class Duration implements Serializable {

    private static final long SECONDS_IN_MINUTE = 60;
    private static final long SECONDS_IN_HOUR = 3600;
    private static final int SECONDS_IN_DAY = 86400;
    private long _time; // time in seconds


    /** Constructor: for persistence only. */
    protected Duration() { super(); }

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
     * Accessor for the time.
     *
     * @return Time in seconds.
     */
    public long time() {
        return _time;
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
        int result = 1;
        result = prime * result + (int) (_time ^ (_time >>> 32));
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
}
