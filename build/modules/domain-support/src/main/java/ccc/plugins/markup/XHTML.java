/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.plugins.markup;

import java.io.InputStream;
import java.io.PrintStream;

import javax.xml.xpath.XPath;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface XHTML {

    /**
     * Test whether an xhtml page is valid.
     *
     * @param page The page to be validated, as an input stream.
     * @return True if the page is valid, false otherwise.
     */
    boolean isValid(final InputStream page);

    /**
     * Apply an xPath expression to an xhtml page.
     *
     * @param page The page to which we'll apply the expression.
     * @param xpathExpression The expression to apply.
     * @return The results of evaluating the expression, as a String. See
     *      {@link XPath#evaluate(String, Object)} for further details.
     */
    String evaluateXPath(final InputStream page, final String xpathExpression);

    /**
     * Apply an xPath expression to an xhtml page.
     * TODO: Rename method.
     *
     * @param doc The document to which we'll apply the expression.
     * @param xpathExpression The expression to apply.
     * @return The results of evaluating the expression, as a String. See
     *      {@link XPath#evaluate(String, Object)} for further details.
     */
    NodeList evaluateXPathToNodeList(final Document doc,
                                     final String xpathExpression);

    /**
     * Validate an xhtml page and print any errors to the specified
     * {@link PrintStream}.
     *
     * @param page The page to validate.
     * @param out The print stream to which errors will be written.
     */
    void printErrors(final InputStream page, final PrintStream out);

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
    String escape(final String string);

    /**
     * Escape a html/xhtml character.
     * <p>
     * This method converts all HTML 4.01 'markup significant' characters to
     * their equivalent entities, as follows:
     * <ol>\u0022 -> &amp;quot;</ol>
     * <ol>\u0026 -> &amp;amp;</ol>
     * <ol>\u003c -> &amp;lt;</ol>
     * <ol>\u003e -> &amp;gt;</ol>
     *
     * @param character The character to escape.
     * @return The equivalent escaped characters.
     */
    char[] escape(final char character);

    /**
     * Clean up invalid characters and HTML tags.
     *
     * @param content The content to clean up.
     * @return Cleaned up content.
     */
    String cleanUpContent(final String content);

    /**
     * Sanitize a html/xhtml string.
     *
     * @param raw The un-sanitized string.
     * @return The sanitized string.
     */
    String sanitize(final String raw);

    /**
     * Sanitize a string representation of a URL.
     * <p>This method only allows correctly form HTTP URLs.
     *
     * @param raw The un-sanitized string.
     * @return The input string or a zero length string if the URL is sanitized.
     */
    String sanitizeUrl(final String raw);

    /**
     * Fix a html/xhtml string.
     *
     * @param raw The un-fixed string.
     * @return The fixed string.
     */
    String fix(final String raw);

}
