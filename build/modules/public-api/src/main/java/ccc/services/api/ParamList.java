/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
import java.util.Map;
import java.util.TreeMap;


/**
 * A list of parameters, accessible by key and with a simple to parse string
 * representation.
 * <p>
 *
 * @author Civic Computing Ltd.
 */
public class ParamList implements Serializable {
    private TreeMap<String, String> _values = new TreeMap<String, String>();

    /**
     * Constructor.
     */
    public ParamList() { super(); }

    /**
     * Constructor.
     *
     * @param canonicalForm The canonical form of this param list.
     */
    public ParamList(final String canonicalForm) {
        setCanonicalForm(canonicalForm);
    }

    /**
     * Set the value for the specified key.
     *
     * @param key The name of the parameter.
     * @param value The value of the parameter.
     *
     * @return Returns 'this'.
     */
    public ParamList set(final String key, final String value) {
        if (key.contains("=") || value.contains("=")) {
            throw new IllegalArgumentException("The char '=' is not allowed");
        } else if (key.contains("\n") || value.contains("\n")) {
            throw new IllegalArgumentException("The char '\\n' is not allowed");
        }
        _values.put(key, value);
        return this;
    }

    /**
     * Get the value for the specified key.
     *
     * @param key The name of the parameter
     * @return The value of the parameter.
     */
    public String get(final String key) {
        return _values.get(key);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder canonicalForm = new StringBuilder();
        for (final Map.Entry<String, String> value : _values.entrySet()) {
            canonicalForm.append(value.getKey());
            canonicalForm.append('=');
            canonicalForm.append(value.getValue());
            canonicalForm.append('\n');
        }
        return canonicalForm.toString();
    }

    /**
     * Accessor.
     *
     * @return Returns the canonical form for this parameter list.
     */
    public String getCanonicalForm() {
        return toString();
    }

    /**
     * Mutator.
     *
     * @param canonicalForm The canonical form to set.
     */
    public void setCanonicalForm(final String canonicalForm) {
        _values.clear();
        if (null==canonicalForm) {
            return;
        }
        final String[] params = canonicalForm.split("\\n");
        for (final String param : params) {
            if (1>param.length()) { // Ignore empty lines
                continue;
            }
            final String [] kvPair = param.split("=");
            set(kvPair[0], kvPair[1]);
        }
    }

    /**
     * Size of the list.
     *
     * @return Integer, the number of param's in the list.
     */
    public int size() {
        return _values.size();
    }
}
