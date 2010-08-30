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

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * A helper class for working with XML.
 *
 * @author Civic Computing Ltd.
 */
public final class XML {


    private XML() { super(); }


    /**
     * Parse a string as an XML document.
     *
     * @param xmlString The string to parse.
     *
     * @return The string as DOM document.
     */
    public static Document parse(final String xmlString) {
        try {
            final DocumentBuilder builder = createParser(null, null);
            return builder.parse(new InputSource(new StringReader(xmlString)));

        // FIXME Improve exception handling.
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Create a JAXP document builder.
     *
     * @param errorHandler The error handler to use.
     * @param entityResolver The entity resolver to use.
     *
     * @return A valid JAXP document builder.
     *
     * @throws ParserConfigurationException If the builder cannot be created.
     */
    static DocumentBuilder createParser(
                                    final ErrorHandler errorHandler,
                                    final EntityResolver entityResolver)
                                       throws ParserConfigurationException {
        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        final DocumentBuilder parser = factory.newDocumentBuilder();
        parser.setEntityResolver(entityResolver);
        parser.setErrorHandler(errorHandler);
        return parser;
    }
}
