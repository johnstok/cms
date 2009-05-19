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
import java.math.BigDecimal;
import java.util.Date;

import ccc.api.Decimal;
import ccc.api.Json;
import ccc.api.Jsonable;
import ccc.api.ParagraphType;

/**
 * A paragraph of HTML.
 *
 * @author Civic Computing Ltd
 */
public final class Paragraph implements Serializable, Jsonable {
    /** MAX_NAME_LENGTH : int. */
    static final int MAX_NAME_LENGTH = 256;

    private String  _text;
    private ParagraphType    _type;
    private Boolean _boolean;
    private Date    _date;
    private String  _name;
    private BigDecimal _number;

    private Paragraph() { super(); }


    private void name(final String name) {
        require().notEmpty(name);
        require().maxLength(name, MAX_NAME_LENGTH);
        _name = name;
    }

    private void text(final String text) {
        require().notNull(text);
        _text = text;
        _type = ParagraphType.TEXT;
    }

    private void bool(final Boolean b) {
        require().notNull(b);
        _boolean = b;
        _type = ParagraphType.BOOLEAN;
    }

    private void date(final Date date) {
        require().notNull(date);
        _date = date;
        _type = ParagraphType.DATE;
    }

    private void number(final BigDecimal number) {
        require().notNull(number);
        _number = number;
        _type = ParagraphType.NUMBER;
    }

    /**
     * Factory method. Creates a paragraph representing text.
     *
     * @param text The text for this paragraph.
     * @param name The name of the paragraph.
     * @return A paragraph with string content.
     */
    public static Paragraph fromText(final String name, final String text) {
        final Paragraph p = new Paragraph();

        p.name(name);
        p.text(text);

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
        final Paragraph p = new Paragraph();

        p.name(name);
        p.bool(b);

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
        final Paragraph p = new Paragraph();

        p.name(name);
        p.date(date);

        return p;
    }

    /**
     * Factory method. Creates a paragraph representing a number.
     *
     * @param number The number for this paragraph.
     * @param name The name of the paragraph.
     * @return A paragraph with numerical content.
     */
    public static Paragraph fromNumber(final String name, final long number) {
        final Paragraph p = new Paragraph();

        p.name(name);
        p.number(BigDecimal.valueOf(number));

        return p;
    }

    /**
     * Factory method. Creates a paragraph representing a number.
     *
     * @param number The number for this paragraph.
     * @param name The name of the paragraph.
     * @return A paragraph with numerical content.
     */
    public static Paragraph fromNumber(final String name, final double number) {
        final Paragraph p = new Paragraph();

        p.name(name);
        p.number(BigDecimal.valueOf(number));

        return p;
    }

    /**
     * Factory method. Creates a paragraph representing a number.
     *
     * @param number The number for this paragraph.
     * @param name The name of the paragraph.
     * @return A paragraph with numerical content.
     */
    public static Paragraph fromNumber(final String name,
                                       final BigDecimal number) {
        final Paragraph p = new Paragraph();

        p.name(name);
        p.number(number);

        return p;
    }

    /**
     * Accessor.
     *
     * @return The numerical representation of this paragraph.
     */
    public BigDecimal number() {
        return _number;
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
    public ParagraphType type() {
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

    /**
     * Create a paragraph from a snapshot.
     *
     * @param json The snapshot used to create a new paragraph object.
     * @throws InvalidSnapshotException If the snapshot data is insufficient to
     *  create a valid paragraph.
     * @return A valid paragraph.
     */
    public static Paragraph fromSnapshot(final Json json)
                                               throws InvalidSnapshotException {
        require().notNull(json);

        final Paragraph p = new Paragraph();
        p._name = json.getString("name");
        p._type = ParagraphType.valueOf(json.getString("type"));
        switch (p._type) {
            case BOOLEAN:
                p._boolean = json.getBool("bool");
                break;

            case DATE:
                p._date = json.getDate("date");
                break;

            case TEXT:
                p._text = json.getString("text");
                break;

            case NUMBER:
                p._number =
                    new BigDecimal(json.getDecimal("number").toString());
                break;

            default:
                throw new InvalidSnapshotException(
                    "Paragraph type unsupported.");
        }

        return p;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set("name", name());
        json.set("type", _type.name());
        json.set("text", text());
        json.set("bool", bool());
        json.set("date", date());
        json.set("number",
            (null==_number) ? null : new Decimal(_number.toString()));

    }
}
