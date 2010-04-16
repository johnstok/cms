/*
 * Copyright 2008 Les Hazlewood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ccc.api.types;

import java.io.Serializable;

/**
 * An email address.
 * <p>Represents the textual string of an
 * <a href="http://www.ietf.org/rfc/rfc2822.txt">RFC 2822</a> email address.
 * Domain literals and quoted identifiers are not supported. Only literal
 * equality (i.e. not logical equality) is provided by this class.
 *
 * @author Civic Computing Ltd.
 */
public class EmailAddress implements Serializable {

    private static final String VALID_EMAIL =
        "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*"
        + "@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:"
        +"[a-zA-Z0-9-]*[a-zA-Z0-9])?";

    private String _text;


    /**
     * Constructor.
     */
    protected EmailAddress() {
        setText("unknown@example.com");
    }


    /**
     * Constructor.
     *
     * @param newText The text representation of the email address.
     */
    public EmailAddress(final String newText) {
        setText(newText);
    }


    /**
     * Accessor.
     *
     * @return The text representation of the email.
     */
    public String getText() {
        return _text;
    }


    /**
     * Set the text for this email address.
     *
     * @param text The text representation.
     */
    public void setText(final String text) {
        DBC.require().toBeTrue(isValidText(text), "Invalid email: "+text);
        _text = text;
    }


    /**
     * Utility method that checks to see if the specified string is a valid
     * email address according to the RFC 2822 specification.
     *
     * @param email The email address string to test for validity.
     * @return True if the given text valid according to RFC 2822, false
     * otherwise.
     */
    public static boolean isValidText(final String email) {
        return (email != null) && email.matches(VALID_EMAIL);
    }


    /** {@inheritDoc} */
    @Override public boolean equals(final Object o) {
        if (o instanceof EmailAddress) {
            final EmailAddress ea = (EmailAddress) o;
            return getText().equals(ea.getText());
        }
        return false;
    }


    /** {@inheritDoc} */
    @Override public int hashCode() {
        return getText().hashCode();
    }


    /** {@inheritDoc} */
    @Override public String toString() {
        return getText();
    }
}
