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
public class TemplateDelta implements Serializable {
    public String _id;
    public long   _version;
    public String _name;
    public String _title;
    public String _description;
    public String _body;
    public String _definition;
}
