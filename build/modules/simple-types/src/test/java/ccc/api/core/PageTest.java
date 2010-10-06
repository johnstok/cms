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
package ccc.api.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ccc.api.types.Paragraph;


/**
 * Tests for the {@link Page} class.
 *
 * @author Civic Computing Ltd.
 */
public class PageTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testConstructor() {

        // ARRANGE
        final String comment = "comment";

        // ACT
        final Page p = new Page(null, "name", null, null, comment, true);

        // ASSERT
        assertEquals(comment, p.getComment());
        assertEquals(true, p.isMajorChange());
        assertEquals(new HashSet<String>(), p.getParagraphs());
        assertNull(p.getParagraph("p2"));
        assertNull(p.getParagraph(null));
    }

    /**
     * Test.
     */
    public void testProperties() {

        // ARRANGE
        final String comment = "comment";
        final Set<Paragraph> paras =
            Collections.singleton(Paragraph.fromText("p1", "foo"));
        final Page p = new Page();

        // ACT
        p.setComment(comment);
        p.setMajorChange(true);
        p.setParagraphs(paras);

        // ASSERT
        assertEquals(comment, p.getComment());
        assertEquals(true, p.isMajorChange());
        assertEquals(paras, p.getParagraphs());
        assertNotNull(p.getParagraph("p1"));
        assertNull(p.getParagraph("p2"));
        assertNull(p.getParagraph(null));
    }

    /**
     * Test.
     */
    public void testDefaultProperties() {

        // ARRANGE

        // ACT
        final Page p = new Page();

        // ASSERT
        assertEquals(null, p.getComment());
        assertEquals(false, p.isMajorChange());
        assertEquals(new HashSet<Paragraph>(), p.getParagraphs());
        assertNull(p.getParagraph("p2"));
        assertNull(p.getParagraph(null));
    }

    /**
     * Test.
     */
    public void testDelta() {

        // ARRANGE
        final Set<Paragraph> paras =
            Collections.singleton(Paragraph.fromText("p1", "foo"));

        // ACT
        final Page p = Page.delta(paras);

        // ASSERT
        assertEquals(null, p.getComment());
        assertEquals(false, p.isMajorChange());
        assertEquals(paras, p.getParagraphs());
        assertNotNull(p.getParagraph("p1"));
        assertNull(p.getParagraph("p2"));
        assertNull(p.getParagraph(null));
    }
}
