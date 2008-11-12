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
package ccc.commons.serialisation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class JsonSerializer implements Serializer {

    private static final SimpleDateFormat DATE_FORMATTER =
        new SimpleDateFormat("dd.MM.yyyy HH.mm z");
    private final StringBuffer _buffer = new StringBuffer();

    /** {@inheritDoc} */
    @Override
    public void integer(final String string, final int version) {
        number(string, version);
    }


    /** {@inheritDoc} */
    @Override
    public void date(final String string, final Date on) {
        string(string);
        colon();
        if (null==on) {
            nil();
        } else {
            final String formattedDate = DATE_FORMATTER.format(on);
            string(formattedDate);
        }
        comma();
    }


    /** {@inheritDoc} */
    @Override
    public void number(final String string, final long value) {
        string(string);
        colon();
        number(value);
        comma();
    }


    /** {@inheritDoc} */
    @Override
    public void string(final String string, final String string2) {
        string(string);
        colon();
        if (null==string2) {
            nil();
        } else {
            string(string2);
        }
        comma();

    }


    /** {@inheritDoc} */
    @Override
    public void dict(final String string,
                     final Map<String, ? extends CanSerialize> paragraphs) {
        string(string);
        colon();
        startDict();
        for (final Map.Entry<String, ? extends CanSerialize> entry
                                                      : paragraphs.entrySet()) {
            string(entry.getKey());
            colon();
            dict(entry.getValue());
            comma();
        }
        endDict();
        comma();
    }

    /**
     * Create a JSON dictionary with the specified serializable class.
     *
     * @param value The class to serialize.
     */
    public JsonSerializer dict(final CanSerialize value) {
        startDict();
        value.serialize(this);
        endDict();
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _buffer.toString();
    }

    private void endDict() {
        if (_buffer.lastIndexOf(",") == (_buffer.length()-1)) {
            _buffer.deleteCharAt(_buffer.length()-1);
        }
        _buffer.append("}");
    }

    private void startDict() {
        _buffer.append("{");
    }

    private void number(final long version) {
        _buffer.append(version);
    }

    private void string(final String string) {
        _buffer.append('"');
        _buffer.append(string);
        _buffer.append('"');
    }

    private void colon() {
        _buffer.append(":");
    }

    private void comma() {
        _buffer.append(",");
    }

    private void nil() {
        _buffer.append((String) null);
    }
}
