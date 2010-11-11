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
package ccc.commons;

import java.util.List;

import junit.framework.TestCase;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class TaxonomyToolsTest extends TestCase {

    /**
     * Test.
     *
     */
    public void testResolveVocabularyPath() {

        // ARRANGE
        final TaxonomyTools tools = new TaxonomyTools();
        final String definitionXML = "<fields>"
            +"<field name=\"content\" type=\"html\"/>"
            +"<field name=\"category\" type=\"taxonomy\" vocabulary=\"123\"/>"
            +"</fields>";

        // ACT
        final String result =
            tools.resolveVocabularyPath("category", definitionXML);

        // ASSERT
        assertEquals("123", result);
    }


    /**
     * Test.
     *
     */
    public void testResolveTermTitle() {

        // ARRANGE
        final TaxonomyTools tools = new TaxonomyTools();
        final String vocabulary = "<vocabulary>"
            + "<term id=\"12.1\" title=\"ground\">"
            + "<term id=\"12.2\" title=\"car\"/>"
            + "<term id=\"12.3\" title=\"truck\"/>"
            + "</term></vocabulary>";

        // ACT
        final String result = tools.resolveTermTitle(vocabulary, "12.2");

        // ASSERT
        assertEquals("car", result);

    }


    /**
     * Test.
     *
     */
    public void testListTerms() {

        // ARRANGE
        final TaxonomyTools tools = new TaxonomyTools();
        final String vocabulary = "<vocabulary>"
            + "<term id=\"1.1\" title=\"ground\">"
                + "<term id=\"1.2\" title=\"car\"/>"
                + "<term id=\"1.3\" title=\"truck\"/>"
            + "</term></vocabulary>";

        // ACT
        final List<String> result = tools.listTerms(vocabulary);

        // ASSERT
        assertEquals(3, result.size());
        assertEquals(
            "<option class=\"taxonomy1\" value=\"1.1\">ground</option>",
            result.get(0));
        assertEquals("<option class=\"taxonomy2\" value=\"1.2\">car</option>",
            result.get(1));
    }

}
