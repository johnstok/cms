/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.markup;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A SAX content handler that only operates on white-listed elements.
 * FIXME: Ignore all children of a blacklisted element? Stax may be a better
 * option in this case? Or count element depth and a 'mode' -> DISCARD,
 * BLOCK, ECHO, etc.
 * FIXME: Check how tag soup selects a charset (potential UTF-7 bug).
 *
 * @author Civic Computing Ltd.
 */
final class EchoContentHandler
    implements
        ContentHandler {

    private static final Logger LOG =
        Logger.getLogger(EchoContentHandler.class);

    private final StringBuffer _sb = new StringBuffer();
    private int _elementDepth = 0;


    /** {@inheritDoc} */
    @Override public void characters(final char[] ch,
                                     final int start,
                                     final int length) {
        for (int i=start; i<(start+length); i++) {
            _sb.append(XHTML.escape(ch[i]));
        }
        LOG.debug(new String(ch, start, length));
    }


    /** {@inheritDoc} */
    @Override public void endDocument() {
        /* NO OP */
    }


    /** {@inheritDoc} */
    @Override public void endElement(final String uri,
                                     final String localName,
                                     final String name) {
        _elementDepth--;
        _sb.append("</"+name+">");
        LOG.debug("End element: "+name);
    }


    private void indent() {
        for (int i=0; i<_elementDepth; i++) {_sb.append("  "); }
    }


    /** {@inheritDoc} */
    @Override public void endPrefixMapping(final String prefix) {
        /* NO OP */
    }


    /** {@inheritDoc} */
    @Override public void ignorableWhitespace(final char[] ch,
                                              final int start,
                                              final int length)
    throws SAXException {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override public void processingInstruction(final String target,
                                                final String data) {
        LOG.debug("Processing instruction: "+target);
    }


    /** {@inheritDoc} */
    @Override public void setDocumentLocator(final Locator locator) {
        /* NO OP */
    }


    /** {@inheritDoc} */
    @Override public void skippedEntity(final String name)
    throws SAXException {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override public void startDocument() {
        /* NO OP */
    }


    /** {@inheritDoc} */
    @Override public void startElement(final String uri,
                                       final String localName,
                                       final String name,
                                       final Attributes atts) {
        _sb.append('\n');
        indent();
        _sb.append("<"+name+">");
        LOG.debug("Start element: "+name);
        _elementDepth++;
    }


    /** {@inheritDoc} */
    @Override public void startPrefixMapping(final String prefix,
                                             final String uri) {
        /* NO OP */
    }


    /**
     * Accessor.
     *
     * @return This content handler's buffer.
     */
    StringBuffer buffer() { return _sb; }

}
