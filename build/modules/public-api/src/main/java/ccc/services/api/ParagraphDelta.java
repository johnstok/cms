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
import java.util.Date;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ParagraphDelta
    implements
        Serializable {
    public String _name;
    public Type   _type;
    public String _rawValue;
    public String _textValue;
    public Date   _dateValue;
    public String _numberValue;

    public static enum Type {
        TEXT, DATE, BOOLEAN, NUMBER;
    }
}
