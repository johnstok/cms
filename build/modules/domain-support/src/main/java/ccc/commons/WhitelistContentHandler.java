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
package ccc.commons;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A SAX content handler that only operates on white-listed elements.
 *
 * FIXME: Check how tag soup selects a charset (potential UTF-7 bug).
 * FIXME: Don't render closing tags for elements such as 'br'.
 *
 * @author Civic Computing Ltd.
 */
final class WhitelistContentHandler
    implements
        ContentHandler {

    private static final Logger LOG =
        Logger.getLogger(WhitelistContentHandler.class);

    private final StringBuffer _sb = new StringBuffer();
    private Set<String> _allowed = new HashSet<String>();
    private Set<String> _ignored = new HashSet<String>();
    private int _elementDepth = 0;
    private int _blockTo = Integer.MAX_VALUE;


    /** {@inheritDoc} */
    @Override public void characters(final char[] ch,
                                     final int start,
                                     final int length) {
        if (blocking()) {
            return;
        }

        /* TODO: Handle multi-char code points. How?! */
        for (int i=start; i<(start+length); i++) {
            _sb.append(XHTML.escape(ch[i]));
        }
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

        if (atBlockDepth()) {
            unBlock();
        } else if (blocking()) {
            return;
        }else if (isAllowed(name)) {
            _sb.append("</"+name+">");
        }
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
        LOG.debug("Ignoring processing instruction: "+target);
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
        if (blocking()) {
            _elementDepth++;
            return;
        }

        if (isAllowed(name)) {
            _sb.append("<"+name+">");
        } else if (isBlocked(name)) {
            LOG.debug("Blocking element: "+name);
            block();
        } else {
            LOG.debug("Ignoring element: "+name);
        }
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


    /**
     * Specify the element names allowed by this handler.
     *
     * @param elements An array of allowed element names.
     */
    public void setAllowedElements(final String... elements) {
        _allowed.clear();
        for (final String element : elements) {
            _allowed.add(element.toLowerCase());
        }
    }


    /**
     * Specify the element names ignored by this handler.
     *
     * @param ignored An array of ignored element names.
     */
    public void setIgnoredElements(final String... ignored) {
        _ignored.clear();
        for (final String element : ignored) {
            _ignored.add(element.toLowerCase());
        }
    }


    private boolean isIgnored(final String name) {
        return _ignored.contains(name.toLowerCase());
    }


    private boolean isAllowed(final String name) {
        return _allowed.contains(name.toLowerCase());
    }


    private boolean blocking() {
        return _blockTo < _elementDepth;
    }


    private void block() {
        _blockTo = _elementDepth;
    }


    private boolean isBlocked(final String name) {
        return !(isIgnored(name) || isAllowed(name));
    }


    private void unBlock() {
        _blockTo = Integer.MAX_VALUE;
    }


    private boolean atBlockDepth() {
        return _blockTo == _elementDepth;
    }
}
