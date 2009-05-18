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
package ccc.api;

import java.io.InputStream;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface LocalCommands extends Commands {

    ResourceSummary createFile(ID parentFolder,
                               FileDelta file,
                               String resourceName,
                               InputStream dataStream,
                               boolean publish) throws CommandFailedException;

    void updateFile(ID fileId,
                    FileDelta fileDelta,
                    InputStream dataStream) throws CommandFailedException;
}
