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


    private String _string;
    private Type _type;
    private Boolean _boolean;
    private Date _date;

    @SuppressWarnings("unused")
    private Paragraph() { /* NO-OP */ }


    /**
     * Factory method. Creates a paragraph representing text.
     *
     * @param string The text for this paragraph.
     * @return A paragraph with string content.
     */
    public static Paragraph fromText(final String string) {
        require().notNull(string);
        final Paragraph p = new Paragraph();
        p._string = string;
        p._type = Type.TEXT;
        return p;
    }

    /**
     * Factory method. Creates a paragraph representing a boolean.
     *
     * @param b The boolean for this paragraph.
     * @return A paragraph with boolean content.
     */
    public static Paragraph fromBoolean(final Boolean b) {
        require().notNull(b);
        final Paragraph p = new Paragraph();
        p._boolean = b;
        p._type = Type.BOOLEAN;
        return p;
    }

    /**
     * Factory method. Creates a paragraph representing a date.
     *
     * @param date The date for this paragraph.
     * @return A paragraph with date content.
     */
    public static Paragraph fromDate(final Date date) {
        require().notNull(date);
        final Paragraph p = new Paragraph();
        p._date = date;
        p._type = Type.DATE;
        return p;
    }

    /**
     * Accessor.
     *
     * @return The string representation of this paragraph.
     */
    public String text() {
        return _string;
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
}
