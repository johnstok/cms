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
package ccc.services.api;

import java.io.Serializable;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSummary implements Serializable {

    public String _id;
    public String _parentId;
    public String _name;
    public String _publishedBy;
    public String _title;
    public String _lockedBy;
    public String _type;
    public int    _childCount;
    public int    _folderCount;
    public boolean _includeInMainMenu;
    public String _stylesheet;
}
