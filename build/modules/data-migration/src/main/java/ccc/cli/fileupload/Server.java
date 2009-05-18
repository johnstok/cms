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
package ccc.cli.fileupload;

import java.io.File;
import java.util.UUID;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface Server {
    void createFile(final UUID parentFolder, final File f, boolean publish);
    UUID createFolder(final UUID parentFolder, String name, boolean publish);
    UUID getRoot();
}
