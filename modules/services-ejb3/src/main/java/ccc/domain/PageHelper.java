/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

package ccc.domain;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;


/**
 * Helper class for manipulating pages.
 * TODO: these methods could be moved to the {@link Page} class.
 *
 * @author Civic Computing Ltd.
 */
public class PageHelper {

    /**
     * Validate the fields of a page.
     *
     * @param delta The paragraphs to validate
     * @param t The template definition.
     */
    public void validateFieldsForPage(final Set<Paragraph> delta,
                                      final String t) {
        final String errors = validateFields(delta, t);
        if (!errors.isEmpty()) {
            throw new RuntimeException(
                "Field validation failed: " + errors);
        }
    }

    /**
     * Validate the fields of a page.
     *
     * @param delta The paragraphs to validate
     * @param t The template definition.
     *
     * @return The errors, as a list of strings.
     */
    public String validateFields(final Collection<Paragraph> delta,
                                 final String t) {
        Document document;
        final StringBuffer errors = new StringBuffer();

        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource s = new InputSource(new StringReader(t));
            document = builder.parse(s);
            final NodeList nl = document.getElementsByTagName("field");
            for (int n = 0; n < nl.getLength(); n++) {
                final NamedNodeMap nnm = nl.item(n).getAttributes();
                final Node regexp = nnm.getNamedItem("regexp");
                final Node name = nnm.getNamedItem("name");
                if (regexp != null && name != null) {
                    for (final Paragraph para : delta) {
                        if (name.getNodeValue().equals(para.getName())
                            && !para.getText().matches(regexp.getNodeValue())
                            && (para.getType() == ParagraphType.TEXT)) {
                            if (errors.length() > 0) {
                                errors.append("\n");
                            }
                            errors.append(para.getName()
                                +", regexp: "+regexp.getNodeValue());
                        }
                    }
                }
            }
        } catch (final ParserConfigurationException e) {
            throw new RuntimeException("Error with XML parsing ", e);
        } catch (final SAXException e) {
            throw new RuntimeException("Error with XML parsing ", e);
        } catch (final IOException e) {
            throw new RuntimeException("Error with XML parsing ", e);
        }
        return errors.toString();
    }
}
