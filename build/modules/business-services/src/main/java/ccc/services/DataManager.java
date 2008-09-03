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
package ccc.services;

import java.io.InputStream;
import java.io.OutputStream;

import ccc.domain.Data;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface DataManager {

    void create(Data data, InputStream dataStream);
    void retrieve(Data data, OutputStream dataStream);
}
