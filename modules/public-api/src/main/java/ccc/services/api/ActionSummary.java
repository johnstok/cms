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
package ccc.services.api;

import java.io.Serializable;
import java.util.Date;


/**
 * Summary of a CCC scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class ActionSummary implements Serializable {

    public String _id;
    public String _type;
    public String _actor;
    public Date _executeAfter;
    public String _subjectType;
    public String _subjectPath;
    public String _status;

}
