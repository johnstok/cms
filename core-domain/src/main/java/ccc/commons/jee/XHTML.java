/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.commons.jee;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;




/**
 * Helper methods for working with XHTML.
 *
 * @author Civic Computing Ltd
 */
public final class XHTML {

    private XHTML() { /* NO-OP */ }

    /**
     * An implementation of {@link ErrorHandler} that collects errors.
     *
     * @author Civic Computing Ltd
     */
    private static class XhtmlErrorHandler implements ErrorHandler {
        private Collection<String> errors = new ArrayList<String>();

        public void warning(final SAXParseException e) {
            errors.add(constructErrorMessage(e));
        }

        public void error(final SAXParseException e) {
            errors.add(constructErrorMessage(e));
        }

        public void fatalError(final SAXParseException e) {
            errors.add(constructErrorMessage(e));
        }

        Collection<String> getErrors() {
            return errors;
        }

        private String constructErrorMessage(final SAXParseException e) {
            final StringBuffer fullMessage = new StringBuffer().
                    append("Line: ").append(e.getLineNumber()).
                    append(", Column: ").append(e.getColumnNumber()).
                    append(", Error: ").append(e.getMessage());
            return fullMessage.toString();
        }
    }

    /**
     * An implementation of {@link NamespaceContext} that understands the
     * 'xhtml' namespace.
     *
     * @author Civic Computing Ltd
     */
    private static class XHTMLContext implements NamespaceContext {

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

        public String getPrefix(final String uri) {
            throw new UnsupportedOperationException();
        }

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
            final DocumentBuilder parser = createParser(errorHandler);
            parser.parse(page);
            return errorHandler.errors.size() == 0;
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
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
            final DocumentBuilder builder =
                createParser(new XhtmlErrorHandler());
            final Document doc = builder.parse(page);

            final XPath xpath = createXPath();

            final Object result = xpath.evaluate(xpathExpression, doc);
            return (String) result;

        } catch (final ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
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

    private static DocumentBuilder createParser(
                                        final XhtmlErrorHandler errorHandler)
                                       throws ParserConfigurationException {

        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        final DocumentBuilder parser = factory.newDocumentBuilder();
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
            final DocumentBuilder parser = createParser(errorHandler);
            parser.parse(page);
            for (final String error : errorHandler.errors) {
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
}
