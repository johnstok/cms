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

import java.util.UUID;

import junit.framework.TestCase;


/**
 * Tests for the {@link ResourceRef} class.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceRefTest extends TestCase {

    /**
     * Test.
     */
    public void testAddMetadataToReference() {

        // ARRANGE
        final ResourceRef reference =
            new ResourceRef(
                new ResourceName("foo"),
                UUID.randomUUID(),
                ResourceType.FOLDER);

        // ACT
        reference.addMetadata("children-count", String.valueOf(2));

        // ASSERT
        assertEquals(1, reference.metadata().size());
        assertEquals("2", reference.metadata().get("children-count"));
    }
}
