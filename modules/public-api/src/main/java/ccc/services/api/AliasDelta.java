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
public class AliasDelta implements Serializable {
    public String _id;
    public String _name;
    public String _targetName;
    public String _targetId;
}
