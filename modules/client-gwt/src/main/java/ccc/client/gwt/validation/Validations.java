/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.client.gwt.validation;


import ccc.client.validation.AbstractValidations;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;


/**
 * Client side validations library.
 *
 * @author Civic Computing Ltd.
 */
public final class Validations
    extends
        AbstractValidations {

    /** {@inheritDoc} */
    @Override
    public String notValidXML(final String definition) {
        try {
            final Document d = XMLParser.parse(definition);
            final NodeList l = d.getElementsByTagName("option");
            for (int n=0; n<l.getLength(); n++) {
                final NamedNodeMap al = l.item(n).getAttributes();
                final Node value = al.getNamedItem("value");
                if (value != null
                    && value.getNodeValue().indexOf(',') != -1) {
                    return
                        "XML option value "
                        + UI_CONSTANTS.mustNotContainComma();
                }
            }
        } catch (final DOMParseException e) {
            return "XML "+UI_CONSTANTS.isNotValid();
        }
        return null;
    }
}
