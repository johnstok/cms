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

package ccc.domain;

import junit.framework.TestCase;


/**
 * Tests for the {@link Content} class.
 *
 * @author Civic Computing Ltd
 */
public final class ContentTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructorCanGenerateURL() {

        // ARRANGE
        String name = "!*'();:@&=+$,/\\?%#[]foo BAR_123.~-";

        // ACT
        Content content = new Content(name);

        // ASSERT
        assertEquals(name, content.name());
        assertEquals(ResourceName.escape(content.name()), content.url());
    }

    /**
     * Test.
     */
    public void testAddNewParagraph() {

        // ARRANGE
        Content content = new Content("Name");

        // ACT
        content.addParagraph("header", new Paragraph("<H1>Header</H1>"));

        // Assert
        assertEquals(1, content.paragraphs().size());

    }

    /**
     * Test.
     */
    public void testDeleteParagraph() {

        // ARRANGE
        Content content = new Content("Name");
        content.addParagraph("header", new Paragraph("<H1>Header</H1>"));
        content.addParagraph("footer", new Paragraph("<H1>Footer</H1>"));

        // ACT
        content.deleteParagraph("header");

        // Assert
        assertEquals(1, content.paragraphs().size());
        assertEquals(
            "<H1>Footer</H1>",
            content.paragraphs().get("footer").body());
    }
}
