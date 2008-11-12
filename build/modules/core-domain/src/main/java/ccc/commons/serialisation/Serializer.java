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
package ccc.commons.serialisation;

import java.util.Date;
import java.util.Map;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface Serializer {

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @param string2
     */
    void string(String string, String string2);

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @param version
     */
    @Deprecated
    void integer(String string, int version);

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @param paragraphs
     */
    void dict(String string, Map<String, ? extends CanSerialize> paragraphs);

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @param on
     */
    void date(String string, Date on);

    /**
     * TODO: Add a description of this method.
     *
     * @param string
     * @param _index
     */
    void number(String string, long _index);

}
