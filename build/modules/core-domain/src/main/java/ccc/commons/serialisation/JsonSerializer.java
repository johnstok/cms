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

import java.util.Map;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class JsonSerializer implements Serializer {

    private final StringBuffer _buffer = new StringBuffer();

    /** {@inheritDoc} */
    @Override
    public void integer(final String string, final int version) {
        string(string);
        colon();
        integer(version);
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
     * TODO: Add a description of this method.
     *
     * @param value
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

    private void integer(final int version) {
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
