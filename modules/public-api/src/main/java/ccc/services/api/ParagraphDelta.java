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
 * FIXME: No boolean.
 * FIXME: Should just use the paragraph class?
 * FIXME: Remove rawValue.
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
    public Decimal _numberValue;

    public static enum Type {
        TEXT, DATE, BOOLEAN, NUMBER;
    }

    @SuppressWarnings("unused") private ParagraphDelta() { super(); }

    /**
     * Constructor.
     *
     * @param name
     * @param type
     * @param rawValue
     * @param textValue
     * @param dateValue
     * @param numberValue
     */
    public ParagraphDelta(final String name,
                          final Type   type,
                          final String rawValue,
                          final String textvalue,
                          final Date   dateValue,
                          final Decimal numberValue) {

        _name = name;
        _type = type;
        _rawValue = rawValue;
        _textValue = textvalue;
        _dateValue = dateValue;
        _numberValue = numberValue;
    }
}
