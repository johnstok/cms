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
package ccc.domain;

import java.util.Date;

import junit.framework.TestCase;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;


/**
 * Tests for the {@link TemplateEntity} class.
 *
 * @author Civic Computing Ltd
 */
public final class TemplateTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructor(){

        // ARRANGE

        // ACT
        final TemplateEntity t = new TemplateEntity(
            "foo!",
            "bar",
            "Hello world",
            "<fields/>",
            MimeType.HTML,
            _rm);

        // ASSERT
        assertEquals(new ResourceName("foo_"), t.getName());
        assertEquals("foo!", t.getTitle());
        assertEquals("bar", t.getDescription());
        assertEquals("Hello world", t.getBody());
        assertEquals("<fields/>", t.getDefinition());
        assertEquals(ResourceType.TEMPLATE, t.getType());
    }

    /**
     * Test.
     */
    public void testConstructorWithName(){

        // ARRANGE

        // ACT
        final TemplateEntity t =
            new TemplateEntity(
                new ResourceName("testName"),
                "foo!",
                "bar",
                "Hello world",
                "<fields/>",
                MimeType.HTML,
                _rm);

        // ASSERT
        assertEquals(new ResourceName("testName"), t.getName());
        assertEquals("foo!", t.getTitle());
        assertEquals("bar", t.getDescription());
        assertEquals("Hello world", t.getBody());
        assertEquals("<fields/>", t.getDefinition());
        assertEquals(ResourceType.TEMPLATE, t.getType());
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), UserEntity.SYSTEM_USER, true, "Created.");
}
