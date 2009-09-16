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
package ccc.types;

/**
 * Supported statuses for an action.
 *
 * @author Civic Computing Ltd.
 */
public enum ActionStatus {

    /** SCHEDULED : ActionStatus. */
    SCHEDULED,
    /** COMPLETE : ActionStatus. */
    COMPLETE,
    /** FAILED : ActionStatus. */
    FAILED,
    /** CANCELLED : ActionStatus. */
    CANCELLED;

    /**
     * Get the name of the name in 'camel case'.
     *
     * @return The camel case name as a string.
     */
    public String camelCaseName() {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = name().toCharArray();
        for (int i=0; i<chars.length; i++) {
            if ('_'==chars[i]) {
                i++;
                sb.append(chars[i]);
            } else {
                sb.append(Character.toLowerCase(chars[i]));
            }
        }
        return sb.toString();
    }
}
