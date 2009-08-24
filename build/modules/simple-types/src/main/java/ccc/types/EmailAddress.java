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
package ccc.types;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * An email address represents the textual string of an
 * <a href="http://www.ietf.org/rfc/rfc2822.txt">RFC 2822</a> email address and
 * other corresponding information of interest.
 *
 * <p>If you use this code, please keep the author information intact and
 * reference my site at
 * <a href="http://www.leshazlewood.com">leshazlewood.com</a>.  Thanks!
 *
 * @version http://www.leshazlewood.com/?p=23 on 29th September 2008.
 *
 * @author Les Hazlewood, Civic Computing Ltd.
 */
public class EmailAddress implements Serializable {

    /**
     * This constant states that domain literals are allowed in the email
     * address, e.g.:
     *
     * <p><tt>someone@[192.168.1.100]</tt> or <br/>

     * <tt>john.doe@[23:33:A2:22:16:1F]</tt> or <br/>
     * <tt>me@[my computer]</tt></p>
     *
     * <p>The RFC says these are valid email addresses, but most people don't
     * like allowing them. If you don't want to allow them, and only want to
     * allow valid domain names
     * (<a href="http://www.ietf.org/rfc/rfc1035.txt">RFC 1035</a>,
     * x.y.z.com, etc), change this constant to <tt>false</tt>.
     *
     * <p>Its default value is <tt>true</tt> to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */
    private static final boolean ALLOW_DOMAIN_LITERALS = true;

    /**
     * This constant states that quoted identifiers are allowed (using quotes
     * and angle brackets around the raw address) are allowed, e.g.:
     *
     * <p><tt>"John Smith" &lt;john.smith@somewhere.com&gt;</tt>
     *
     * <p>The RFC says this is a valid mailbox.  If you don't want to
     * allow this, because for example, you only want users to enter in
     * a raw address (<tt>john.smith@somewhere.com</tt> - no quotes or angle
     * brackets), then change this constant to <tt>false</tt>.
     *
     * <p>Its default value is <tt>true</tt> to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */
    private static final boolean ALLOW_QUOTED_IDENTIFIERS = true;

    // RFC 2822 2.2.2 Structured Header Field Bodies
    private static final String WHITESPACE_CHARS = "[ \\t]"; //space or tab
    private static final String WHITESPACE = WHITESPACE_CHARS + "*";

    //RFC 2822 3.2.1 Primitive tokens
    private static final String QUOTE = "\\\"";
    //ASCII Control characters excluding white space:
    private static final String NON_WHITESPACE_CONTROL =
        "\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F";
    //all ASCII characters except CR and LF:
    private static final String ASCII_TEXT =
        "[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F]";

    // RFC 2822 3.2.2 Quoted characters:
    //single backslash followed by a text char
    private static final String QUOTED_PAIR = "(\\\\" + ASCII_TEXT + ")";

    //RFC 2822 3.2.4 Atom:
    private static final String ATOM_TEXT =
        "[a-zA-Z0-9\\!\\#\\$\\%\\&amp;\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}"
        + "\\~]";
    private static final String ATOM =
        WHITESPACE + ATOM_TEXT + "+" + WHITESPACE;
    private static final String DOT_ATOM_TEXT =
        ATOM_TEXT + "+" + "(" + "\\." + ATOM_TEXT + "+)*";
    private static final String DOT_ATOM =
        WHITESPACE + "(" + DOT_ATOM_TEXT + ")" + WHITESPACE;

    /*
     * RFC 2822 3.2.5 Quoted strings:
     * NON_WHITESPACE_CONTROL and the rest of ASCII except the double quote and
     * backslash characters:
     */
    private static final String QUOTED_TEXT =
        "[" + NON_WHITESPACE_CONTROL + "\\x21\\x23-\\x5B\\x5D-\\x7E]";
    private static final String QUOTED_CONTENT =
        "(" + QUOTED_TEXT + "|" + QUOTED_PAIR + ")";
    private static final String QUOTED_STRING =
        QUOTE + "(" + WHITESPACE + QUOTED_CONTENT + ")*" + WHITESPACE + QUOTE;

    //RFC 2822 3.2.6 Miscellaneous tokens
    private static final String WORD =
        "((" + ATOM + ")|(" + QUOTED_STRING + "))";
    private static final String PHRASE = WORD + "+"; //one or more words.

    //RFC 1035 tokens for domain names:
    private static final String LETTER = "[a-zA-Z]";
    private static final String LETTER_OR_DIGIT = "[a-zA-Z0-9]";
    private static final String LETTER_DIGIT_OR_HYPHEN = "[a-zA-Z0-9-]";
    private static final String RFC_LABEL =
        LETTER_OR_DIGIT
        + "("
        + LETTER_DIGIT_OR_HYPHEN
        + "{0,61}"
        + LETTER_OR_DIGIT
        + ")?";
    private static final String RFC_1035_DOMAIN_NAME =
        RFC_LABEL + "(\\." + RFC_LABEL + ")*\\." + LETTER + "{2,6}";

    /*
     * RFC 2822 3.4 Address specification
     * domain text - non white space controls and the rest of ASCII chars not
     * including [, ], or \:
     */
    private static final String DOMAIN_TEXT =
        "[" + NON_WHITESPACE_CONTROL + "\\x21-\\x5A\\x5E-\\x7E]";
    private static final String DOMAIN_CONTENT =
        DOMAIN_TEXT + "|" + QUOTED_PAIR;
    private static final String DOMAIN_LITERAL =
        "\\[" + "(" + WHITESPACE + DOMAIN_CONTENT + "+)*" + WHITESPACE + "\\]";
    private static final String RFC_2822_DOMAIN =
        "(" + DOT_ATOM + "|" + DOMAIN_LITERAL + ")";

    private static final String DOMAIN =
        ALLOW_DOMAIN_LITERALS ? RFC_2822_DOMAIN : RFC_1035_DOMAIN_NAME;

    private static final String LOCAL_PART =
        "((" + DOT_ATOM + ")|(" + QUOTED_STRING + "))";
    private static final String ADDRESS_SPEC = LOCAL_PART + "@" + DOMAIN;
    private static final String ANGLE_ADDRESS = "<" + ADDRESS_SPEC + ">";
    private static final String NAME_ADDRESS =
        "(" + PHRASE + ")?" + WHITESPACE + ANGLE_ADDRESS;
    private static final String MAILBOX = NAME_ADDRESS + "|" + ADDRESS_SPEC;

    //now compile a pattern for efficient re-use:
    //if we're allowing quoted identifiers or not:
    private static final String PATTERN_STRING =
        ALLOW_QUOTED_IDENTIFIERS ? MAILBOX : ADDRESS_SPEC;
    private static final Pattern VALID_PATTERN =
        Pattern.compile(PATTERN_STRING);

    //class attributes
    private String _text;

    /**
     * Constructor.
     */
    public EmailAddress() {
        super();
    }


    /**
     * Constructor.
     *
     * @param newText The text representation of the email address.
     */
    public EmailAddress(final String newText) {
        super();
        setText(newText);
    }


    /**
     * Returns the actual email address string, e.g.
     * <tt>someone@somewhere.com</tt>
     *
     * @return the actual email address string.
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
        _text = text;
    }


    /**
     * Returns whether or not the text represented by this object instance is
     * valid according to the <tt>RFC 2822</tt> rules.
     *
     * @return true if the text represented by this instance is valid according
     *         to RFC 2822, false otherwise.
     */
    public boolean isValid() {
        return isValidText(getText());
    }


    /**
     * Utility method that checks to see if the specified string is a valid
     * email address according to the * RFC 2822 specification.
     *
     * @param email the email address string to test for validity.
     * @return true if the given text valid according to RFC 2822, false
     * otherwise.
     */
    public static boolean isValidText(final String email) {
        return (email != null) && VALID_PATTERN.matcher(email).matches();
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
