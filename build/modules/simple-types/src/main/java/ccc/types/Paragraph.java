/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.types;

import static ccc.types.DBC.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;


/**
 * A paragraph of HTML.
 *
 * @author Civic Computing Ltd
 */
public final class Paragraph implements Serializable, Jsonable {
    /** MAX_NAME_LENGTH : int. */
    static final int MAX_NAME_LENGTH = 256;

    private String        _text;
    private ParagraphType _type;
    private Boolean       _boolean;
    private Date          _date;
    private String        _name;

    private Paragraph() { super(); }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a paragraph.
     */
    public Paragraph(final Json json) {
        require().notNull(json);

        _name = json.getString(JsonKeys.NAME);
        _type = ParagraphType.valueOf(json.getString(JsonKeys.TYPE));
        switch (_type) {
            case BOOLEAN:
                _boolean = json.getBool(JsonKeys.BOOLEAN);
                break;

            case DATE:
                _date = json.getDate(JsonKeys.DATE);
                break;

            case TEXT:
            case LIST:
                _text = json.getString(JsonKeys.TEXT);
                break;

            case NUMBER:
                _text = json.getBigDecimal(JsonKeys.NUMBER).toString();
                break;

            default:
                throw new IllegalArgumentException(
                    "Paragraph type unsupported: "+_type);
        }
    }


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

    private void list(final String text) {
        require().notNull(text);
        _text = text;
        _type = ParagraphType.LIST;
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
        _text = number.toString();
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
        p.text((text == null) ? "" : text);

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
        p.number(new BigDecimal(number));

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
        p.number(new BigDecimal(String.valueOf(number)));

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
     * Factory method. Creates a paragraph representing a list of strings.
     *
     * @param name The name of the paragraph.
     * @param list The list for the paragraph.
     * @return A paragraph with list content.
     */
    public static Paragraph fromList(final String name,
                                     final List<String> list) {
        final Paragraph p = new Paragraph();

        p.name(name);
        p.list(listToText(list));

        return p;
    }

    /**
     * Accessor.
     *
     * @return The numerical representation of this paragraph.
     */
    public BigDecimal getNumber() {
        return new BigDecimal(_text);
    }

    /**
     * Accessor.
     *
     * @return The string representation of this paragraph.
     */
    public String getText() {
        return _text;
    }

    /**
     * Accessor.
     *
     * @return The type of this paragraph.
     */
    public ParagraphType getType() {
        return _type;
    }

    /**
     * Accessor.
     *
     * @return The boolean representation of this paragraph.
     */
    public Boolean getBoolean() {
        return _boolean;
    }

    /**
     * Accessor.
     *
     * @return The date representation of this paragraph.
     */
    public Date getDate() {
        return (null==_date) ? null : new Date(_date.getTime());
    }

    /**
     * Accessor.
     *
     * @return The list representation of this paragraph.
     */
    public List<String> getList() {
        return textToList(getText());
    }

    /**
     * Accessor.
     *
     * @return The paragraph's name.
     */
    public String getName() {
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
     * @return A valid paragraph.
     */
    public static Paragraph fromSnapshot(final Json json) {
        return new Paragraph(json);
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.NAME, getName());
        json.set(JsonKeys.TYPE, _type.name());
        json.set(JsonKeys.TEXT, getText());
        json.set(JsonKeys.BOOLEAN, getBoolean());
        json.set(JsonKeys.DATE, getDate());
    }


    /**
     * Escape a html/xhtml string.
     * <p>
     * This method converts all HTML 4.01 'markup significant' characters to
     * their equivalent entities, as follows:
     * <ol>\u0022 -> &amp;quot;</ol>
     * <ol>\u0026 -> &amp;amp;</ol>
     * <ol>\u003c -> &amp;lt;</ol>
     * <ol>\u003e -> &amp;gt;</ol>
     *
     * @param string The string to escape.
     * @return The escaped string.
     */
    public static String escape(final String string) {
        return string.replace("\u0026", "&amp;")        // &
                     .replace("\u005C\u0022", "&quot;") // "
                     .replace("\u003c", "&lt;")         // <
                     .replace("\u003e", "&gt;");        // >
    }

    private static String listToText(final List<String> list) {
        final StringBuilder sb = new StringBuilder();
        for (final String cb : list) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            DBC.require().notEmpty(cb);
            if (cb.contains(",")) {
                throw new RuntimeException("The ',' char is not allowed.");
            }
            sb.append(cb);
        }
        return sb.toString();
    }

    private List<String> textToList(final String text) {
        final List<String> list = new ArrayList<String>();
        for (final String value : text.split(",")) {
            if (value.trim().length() > 0) {
                list.add(value);
            }
        }
        return list;
    }
}
