/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */package ccc.contentcreator.api;

/**
 * The action class encapsulates some behaviour that must take place.
 *
 * @param <T> The type of the input data.
 * @author Civic Computing Ltd.
 */
public interface Action<T> {

    /**
     * Perform this action with the specified data.
     *
     * @param inputData The dat upon which this action will operate.
     */
    void execute(T inputData);

}
