/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import ccc.domain.CCCException;
import ccc.types.DBC;

/**
 * Helper methods for working with XHTML.
 *
 * @author Civic Computing Ltd
 */
public final class XHTML {

    private XHTML() { /* NO-OP */ }

    /**
     * An implementation of {@link EntityResolver} that reads xhtml dtd's from
     * the classpath.
     *
     * @author Civic Computing Ltd.
     */
    static class XhtmlEntityResolver implements EntityResolver {

        /** {@inheritDoc} */
        @Override
        public InputSource resolveEntity(final String publicId,
                                         final String systemId) {
            final InputStream stream =
                getClass().getResourceAsStream(dtdFilename(systemId));
            return new InputSource(new InputStreamReader(stream));
        }

        private String dtdFilename(final String systemId) {
            final String[] pathElements = systemId.split("/");
            return pathElements[pathElements.length - 1];
        }
    }

    /**
     * An implementation of {@link ErrorHandler} that collects errors.
     *
     * @author Civic Computing Ltd
     */
    static class XhtmlErrorHandler implements ErrorHandler {
        private Collection<String> _errors = new ArrayList<String>();

        /** {@inheritDoc} */
        public void warning(final SAXParseException e) {
            _errors.add(constructErrorMessage(e));
        }

        /** {@inheritDoc} */
        public void error(final SAXParseException e) {
            _errors.add(constructErrorMessage(e));
        }

        /** {@inheritDoc} */
        public void fatalError(final SAXParseException e) {
            _errors.add(constructErrorMessage(e));
        }

        /** {@inheritDoc} */
        Collection<String> getErrors() {
            return _errors;
        }

        private String constructErrorMessage(final SAXParseException e) {
            final StringBuffer fullMessage =
                new StringBuffer()
                    .append("Line: ") //$NON-NLS-1$
                    .append(e.getLineNumber())
                    .append(", Column: ") //$NON-NLS-1$
                    .append(e.getColumnNumber())
                    .append(", Error: ") //$NON-NLS-1$
                    .append(e.getMessage());
            return fullMessage.toString();
        }

        /**
         * Accessor for the errors collection.
         *
         * @return A collection of strings, one per error.
         */
        public Collection<String> errors() {
            return Collections.unmodifiableCollection(_errors);
        }
    }

    /**
     * An implementation of {@link NamespaceContext} that understands the
     * 'xhtml' namespace.
     *
     * @author Civic Computing Ltd
     */
    static class XHTMLContext implements NamespaceContext {

        /** {@inheritDoc} */
        public String getNamespaceURI(final String prefix) {
            DBC.require().notNull(prefix);
            if ("xhtml".equals(prefix)) {
                return "http://www.w3.org/1999/xhtml";
            } else if ("xml".equals(prefix)) {
                return XMLConstants.XML_NS_URI;
            } else {
                return XMLConstants.NULL_NS_URI;
            }
        }

        /** {@inheritDoc} */
        public String getPrefix(final String uri) {
            throw new UnsupportedOperationException();
        }

        /** {@inheritDoc} */
        public Iterator<?> getPrefixes(final String uri) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Test whether an xhtml page is valid.
     *
     * @param page The page to be validated, as an input stream.
     * @return True if the page is valid, false otherwise.
     */
    public static boolean isValid(final InputStream page) {

        try {
            final XhtmlErrorHandler errorHandler = new XhtmlErrorHandler();
            final XhtmlEntityResolver resolver = new XhtmlEntityResolver();
            final DocumentBuilder parser =
                createParser(errorHandler, resolver);
            parser.parse(page);
            return errorHandler.errors().size() == 0;
        } catch (final ParserConfigurationException e) {
            throw new CCCException(e);
        } catch (final SAXException e) {
            throw new CCCException(e);
        } catch (final IOException e) {
            throw new CCCException(e);
        }
    }

    /**
     * Apply an xPath expression to an xhtml page.
     *
     * @param page The page to which we'll apply the expression.
     * @param xpathExpression The expression to apply.
     * @return The results of evaluating the expression, as a String. See
     *      {@link XPath#evaluate(String, Object)} for further details.
     */
    public static String evaluateXPath(final InputStream page,
                                      final String xpathExpression) {

        try {
            final Document doc = parse(page);

            final XPath xpath = createXPath();

            final Object result = xpath.evaluate(xpathExpression, doc);
            return (String) result;

        } catch (final XPathExpressionException e) {
            throw new CCCException(e);
        }
    }

    /**
     * Apply an xPath expression to an xhtml page.
     * TODO: Rename method.
     *
     * @param doc The document to which we'll apply the expression.
     * @param xpathExpression The expression to apply.
     * @return The results of evaluating the expression, as a String. See
     *      {@link XPath#evaluate(String, Object)} for further details.
     */
    public static NodeList evaluateXPathToNodeList(
                                                 final Document doc,
                                                 final String xpathExpression) {
        try {
            final XPath xpath = createXPath();

            final Object result =
                xpath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
            return (NodeList) result;

        } catch (final XPathExpressionException e) {
            throw new CCCException(e);
        }
    }

    private static Document parse(final InputStream page) {
        try {
            final DocumentBuilder builder =
                createParser(new XhtmlErrorHandler(),
                             new XhtmlEntityResolver());
            return builder.parse(page);

        } catch (final ParserConfigurationException e) {
            throw new CCCException(e);
        } catch (final SAXException e) {
            throw new CCCException(e);
        } catch (final IOException e) {
            throw new CCCException(e);
        }
    }

    private static XPath createXPath() {
        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xpath = xPathFactory.newXPath();
        xpath.setNamespaceContext(new XHTMLContext());
        return xpath;
    }

    private static DocumentBuilder createParser(
                                    final XhtmlErrorHandler errorHandler,
                                    final XhtmlEntityResolver entityResolver)
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

    /**
     * Validate an xhtml page and print any errors to the specified
     * {@link PrintStream}.
     *
     * @param page The page to validate.
     * @param out The print stream to which errors will be written.
     */
    public static void printErrors(final InputStream page,
                                   final PrintStream out) {

        try {
            final XhtmlErrorHandler errorHandler = new XhtmlErrorHandler();
            final XhtmlEntityResolver resolver = new XhtmlEntityResolver();
            final DocumentBuilder parser = createParser(errorHandler, resolver);
            parser.parse(page);
            for (final String error : errorHandler.errors()) {
                out.println(error);
            }
        } catch (final ParserConfigurationException e) {
            throw new CCCException(e);
        } catch (final SAXException e) {
            throw new CCCException(e);
        } catch (final IOException e) {
            throw new CCCException(e);
        }
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
    public static char[] escape(final char character) {
        switch (character) {
            case '\u0026':
                return "&amp;".toCharArray();
            case '\u0022':
                return "&quot;".toCharArray();
            case '\u003c':
                return "&lt;".toCharArray();
            case '\u003e':
                return "&gt;".toCharArray();
            default:
                return new char[] {character};
        }
    }

    /**
     * Clean up invalid characters and HTML tags.
     *
     * @param content The content to clean up.
     * @return Cleaned up content.
     */
    public static String cleanUpContent(final String content) {
        String result = content;
        if (result != null) {
            result = result.replaceAll("[\\x00-\\x1f]", " ");
            result = result.replaceAll("\\<.*?>", ""); // Scrub HTML
        }
        return result;
    }


    /**
     * Sanitize a html/xhtml string.
     *
     * @param raw The un-sanitized string.
     * @return The sanitized string.
     */
    public static String sanitize(final String raw) {

        final XMLReader reader = new Parser();
        final WhitelistContentHandler ch = new WhitelistContentHandler();

        // FIXME: Allow lists? Allow tables?
        ch.setAllowedElements(
            "p",
            "b",
            "i",
            "strong",
            "em",
            "br",
            "h1",
            "h2",
            "h3",
            "h4",
            "h5",
            "h6");
        ch.setIgnoredElements("a", "html", "body");
        reader.setContentHandler(ch);

        try {
            reader.parse(new InputSource(new StringReader(raw)));
            return ch.buffer().toString();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Fix a html/xhtml string.
     *
     * @param raw The un-fixed string.
     * @return The fixed string.
     */
    static String fix(final String raw) {
        try {
            final InputSource is = new InputSource(new StringReader(raw));
            final XMLReader reader = new Parser();
            final Transformer transformer =
                TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            final StreamResult result = new StreamResult(new StringWriter());
            final SAXSource source = new SAXSource(reader, is);
            transformer.transform(source, result);
            return result.getWriter().toString();

        } catch (final TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final TransformerFactoryConfigurationError e) {
            throw new RuntimeException(e);
        } catch (final TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
