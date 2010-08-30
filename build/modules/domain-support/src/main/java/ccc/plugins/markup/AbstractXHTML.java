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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
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

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import ccc.api.types.DBC;


/**
 * Abstract base class for {@link XHTML} implementations.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractXHTML
    implements
        XHTML {

    /**
     * An implementation of {@link EntityResolver} that reads xhtml dtd's from
     * the classpath.
     *
     * @author Civic Computing Ltd.
     */
    protected static class XhtmlEntityResolver implements EntityResolver {

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
            return "/ccc/plugins/markup/"+pathElements[pathElements.length - 1];
        }
    }

    /**
         * An implementation of {@link ErrorHandler} that collects errors.
         *
         * @author Civic Computing Ltd
         */
        protected static class XhtmlErrorHandler implements ErrorHandler {
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
        protected static class XHTMLContext implements NamespaceContext {

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

    private static Document parse(final InputStream page) {
        try {
            final DocumentBuilder builder =
                XML.createParser(new XhtmlErrorHandler(),
                             new XhtmlEntityResolver());
            return builder.parse(page);

        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isValid(final InputStream page) {

        try {
            final XhtmlErrorHandler errorHandler = new XhtmlErrorHandler();
            final XhtmlEntityResolver resolver = new XhtmlEntityResolver();
            final DocumentBuilder parser =
                XML.createParser(errorHandler, resolver);
            parser.parse(page);
            return errorHandler.errors().size() == 0;
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String evaluateXPath(final InputStream page, final String xpathExpression) {

        try {
            final Document doc = parse(page);

            final XPath xpath = createXPath();

            final Object result = xpath.evaluate(xpathExpression, doc);
            return (String) result;

        } catch (final XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public NodeList evaluateXPathToNodeList(final Document doc, final String xpathExpression) {
        try {
            final XPath xpath = createXPath();

            final Object result =
                xpath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
            return (NodeList) result;

        } catch (final XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static XPath createXPath() {
        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xpath = xPathFactory.newXPath();
        xpath.setNamespaceContext(new XHTMLContext());
        return xpath;
    }

    /**
     * Constructor.
     *
     */
    public AbstractXHTML() {

        super();
    }

    /** {@inheritDoc} */
    @Override
    public void printErrors(final InputStream page, final PrintStream out) {

        try {
            final XhtmlErrorHandler errorHandler = new XhtmlErrorHandler();
            final XhtmlEntityResolver resolver = new XhtmlEntityResolver();
            final DocumentBuilder parser =
                XML.createParser(errorHandler, resolver);
            parser.parse(page);
            for (final String error : errorHandler.errors()) {
                out.println(error);
            }
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String escape(final String string) {
        return string.replace("\u0026", "&amp;")        // &
                     .replace("\u005C\u0022", "&quot;") // "
                     .replace("\u003c", "&lt;")         // <
                     .replace("\u003e", "&gt;");        // >
    }

    /** {@inheritDoc} */
    @Override
    public char[] escape(final char character) {
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

    /** {@inheritDoc} */
    @Override
    public String cleanUpContent(final String content) {
        String result = content;
        if (result != null) {
            result = result.replaceAll("[\\x00-\\x1f]", " ");
            result = result.replaceAll("\\<.*?>", ""); // Scrub HTML
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String sanitize(final String raw) {

        final XMLReader reader = createHtmlParser();
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

    /** {@inheritDoc} */
    @Override
    public String sanitizeUrl(final String raw) {
        try {
            final URI rawUrl = new URI(raw);
            if (!"http".equalsIgnoreCase(rawUrl.getScheme())) {
                return "";
            }
            rawUrl.toURL();
            return raw;
        } catch (final URISyntaxException e) {
            return "";
        } catch (final MalformedURLException e) {
            return "";
        }
    }

    /** {@inheritDoc} */
    @Override
    public String fix(final String raw) {
        try {
            final InputSource is = new InputSource(new StringReader(raw));
            final XMLReader reader = createHtmlParser();
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

    /**
     * Create a HTML parser.
     *
     * @return An XML reader for the parsed HTML.
     */
    protected abstract XMLReader createHtmlParser();
}