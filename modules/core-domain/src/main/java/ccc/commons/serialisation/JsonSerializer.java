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

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


/**
 * A {@link Serializer} that generates valid JSON output.
 * TODO: Support characters not in the basic Multi-lingual plane.
 *
 * @author Civic Computing Ltd.
 */
public class JsonSerializer implements Serializer {

    private final StringBuffer _buffer = new StringBuffer();

    /** {@inheritDoc} */
    @Override
    public void uuid(final String key, final UUID value) {
        string(key);
        colon();
        if (null==value) {
            nil();
        } else {
            value.toString();
        }
        comma();
    }

    /** {@inheritDoc} */
    @Override
    public void integer(final String string, final int version) {
        number(string, version);
    }


    /** {@inheritDoc} */
    @Override
    public void date(final String string, final Date on) {
        if (null==on) {
            string(string);
            colon();
            nil();
            comma();
        } else {
            number(string, on.getTime());
        }
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
     * @return Returns 'this' to allow method chaining.
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

    private void number(final double number) {
        _buffer.append(escape(number));
    }

    private void string(final String string) {
        _buffer.append('"');
        _buffer.append(escape(string));
        _buffer.append('"');
    }

    private void colon() {
        _buffer.append(":");
    }

    private void comma() {
        _buffer.append(",");
    }

    private void nil() {
        _buffer.append("null");
    }

    /**
     * Escape strings using JSON escape rules.
     *
     * @param s The string to escape.
     * @return The escaped string.
     */
    public String escape(final String s){
        if(s==null) {
            return null;
        }
        final StringBuffer sb=new StringBuffer();
        for(int i=0; i<s.length(); i++){
            final char ch=s.charAt(i);
            switch(ch){
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if(isAsciiControlChar(ch)){
                    unicodeEscapeThenAppend(ch, sb);
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Convert a character to its unicode escaped character.
     *
     * @param ch The char to escape.
     * @param sb The escaped equivalent as a String, in the form '\u0001'.
     */
    private void unicodeEscapeThenAppend(final char ch,
                                         final StringBuffer sb) {
        final int maxZeroPadding = 4;
        final String ss=Integer.toHexString(ch);
        sb.append("\\u"); //$NON-NLS-1$
        for(int k=0; k<maxZeroPadding-ss.length(); k++){
            sb.append('0');
        }
        sb.append(ss.toUpperCase(Locale.US));
    }

    /**
     * Test to see if a character is an ASCII escape character.
     *
     * @param ch The char to test.
     * @return True if the character is an escape char, false otherwise.
     */
    private boolean isAsciiControlChar(final char ch) {
        return ch>='\u0000' && ch<='\u001F';
    }

    /**
     * Produce a string from a double.
     *
     * @param  d The double to escape.
     * @return The escaped string.
     */
    public String escape(final double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new IllegalArgumentException(
                "Double may not be infinite or NAN");
        }

        String s = Double.toString(d);
        if (s.indexOf('.') > 0 && s.indexOf('e') < 0 && s.indexOf('E') < 0) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
        }
        return s;
    }
}
