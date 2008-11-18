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



/**
 * Interface implemented by classes that support CCC serialization.
 *
 * @author Civic Computing Ltd.
 */
public interface CanSerialize {

    /**
     * Serialize an object.
     *
     * @param s The serializer used to write a serialized form.
     */
    void serialize(Serializer s);

    /**
     * Deserialize an object.
     *
     * @param ds The deserializer used to read a serialized form.
     */
    void deserialize(final DeSerializer ds);
}
