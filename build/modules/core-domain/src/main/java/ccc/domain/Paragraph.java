/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import static ccc.commons.DBC.*;

import java.io.Serializable;
import java.util.Date;

/**
 * A paragraph of HTML.
 *
 * @author Civic Computing Ltd
 */
public final class Paragraph implements Serializable {


    private String  _text;
    private Type    _type;
    private Boolean _boolean;
    private Date    _date;
    private String  _name;

    @SuppressWarnings("unused") private Paragraph() { super(); }


    /**
     * Factory method. Creates a paragraph representing text.
     *
     * @param text The text for this paragraph.
     * @param name The name of the paragraph.
     * @return A paragraph with string content.
     */
    public static Paragraph fromText(final String name, final String text) {
        require().notNull(name);
        require().notNull(text);

        final Paragraph p = new Paragraph();
        p._text = text;
        p._type = Type.TEXT;
        p._name = name;
        return p;
    }

    /**
     * Factory method. Creates a paragraph representing a boolean.
     *
     * @param b The boolean for this paragraph.
     * @param name The name of the paragraph.
     * @return A paragraph with boolean content.
     */
    public static Paragraph fromBoolean(final String name, final Boolean b) {
        require().notNull(name);
        require().notNull(b);

        final Paragraph p = new Paragraph();
        p._boolean = b;
        p._type = Type.BOOLEAN;
        p._name = name;
        return p;
    }

    /**
     * Factory method. Creates a paragraph representing a date.
     *
     * @param date The date for this paragraph.
     * @param name The name of the paragraph.
     * @return A paragraph with date content.
     */
    public static Paragraph fromDate(final String name, final Date date) {
        require().notNull(name);
        require().notNull(date);

        final Paragraph p = new Paragraph();
        p._date = date;
        p._type = Type.DATE;
        p._name = name;
        return p;
    }

    /**
     * Accessor.
     *
     * @return The string representation of this paragraph.
     */
    public String text() {
        return _text;
    }

    /**
     * Accessor.
     *
     * @return The type of this paragraph.
     */
    public Type type() {
        return _type;
    }

    /**
     * Accessor.
     *
     * @return The boolean representation of this paragraph.
     */
    public Boolean bool() {
        return _boolean;
    }

    /**
     * Accessor.
     *
     * @return The date representation of this paragraph.
     */
    public Date date() {
        return (null==_date) ? null : new Date(_date.getTime());
    }

    /**
     * Accessor.
     *
     * @return The paragraph's name.
     */
    public String name() {
        return _name;
    }

    /**
     * The types of paragraph available.
     *
     * @author Civic Computing Ltd.
     */
    public static enum Type {
        /** TEXT : Type. */
        TEXT,

        /** BOOLEAN : Type. */
        BOOLEAN,

        /** DATE : Type. */
        DATE}

    /** {@inheritDoc} */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Paragraph other = (Paragraph) obj;
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        return true;
    }
}
