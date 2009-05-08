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
 * FIXME: No value for boolean paragraph type.
 * FIXME: Should just use the paragraph class?
 * FIXME: Remove rawValue.
 *
 * @author Civic Computing Ltd.
 */
public final class ParagraphDelta
    implements
        Serializable {
    private String _name;
    private Type   _type;
    private String _rawValue;
    private String _textValue;
    private Date   _dateValue;
    private Decimal _numberValue;

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


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Mutator.
     *
     * @param name The name to set.
     */
    public void setName(final String name) {
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the type.
     */
    public Type getType() {
        return _type;
    }


    /**
     * Mutator.
     *
     * @param type The type to set.
     */
    public void setType(final Type type) {
        _type = type;
    }


    /**
     * Accessor.
     *
     * @return Returns the rawValue.
     */
    public String getRawValue() {
        return _rawValue;
    }


    /**
     * Mutator.
     *
     * @param rawValue The rawValue to set.
     */
    public void setRawValue(final String rawValue) {
        _rawValue = rawValue;
    }


    /**
     * Accessor.
     *
     * @return Returns the textValue.
     */
    public String getTextValue() {
        return _textValue;
    }


    /**
     * Mutator.
     *
     * @param textValue The textValue to set.
     */
    public void setTextValue(final String textValue) {
        _textValue = textValue;
    }


    /**
     * Accessor.
     *
     * @return Returns the dateValue.
     */
    public Date getDateValue() {
        return _dateValue;
    }


    /**
     * Mutator.
     *
     * @param dateValue The dateValue to set.
     */
    public void setDateValue(final Date dateValue) {
        _dateValue = dateValue;
    }


    /**
     * Accessor.
     *
     * @return Returns the numberValue.
     */
    public Decimal getNumberValue() {
        return _numberValue;
    }


    /**
     * Mutator.
     *
     * @param numberValue The numberValue to set.
     */
    public void setNumberValue(final Decimal numberValue) {
        _numberValue = numberValue;
    }
}
