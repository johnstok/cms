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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface CanSerialize {

    void serialize(Serializer s);
    public void deserialize(final DeSerializer ds);
}
