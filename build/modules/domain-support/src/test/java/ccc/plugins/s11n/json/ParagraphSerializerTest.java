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
package ccc.plugins.s11n.json;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;


/**
 * Tests for the {@link ParagraphSerializer} class.
 *
 * @author Civic Computing Ltd.
 */
public class ParagraphSerializerTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testSerializeText() {

        // ARRANGE
        final Paragraph p = Paragraph.fromText("foo", "bar");

        // ACT
        final Json json = new JsonImpl(
            new ParagraphSerializer(new ServerTextParser()).write(p));

        // ASSERT
        assertEquals("foo", json.getString("name"));
        assertEquals("TEXT", json.getString("type"));
        assertEquals("bar", json.getString("text"));
        assertNull(json.getBool("boolean"));
        assertNull(json.getDate("date"));
    }


    /**
     * Test.
     */
    public void testSerializeNumber() {

        // ARRANGE
        final Paragraph p =
            Paragraph.fromNumber("foo", new BigDecimal("123.456"));

        // ACT
        final Json json = new JsonImpl(
            new ParagraphSerializer(new ServerTextParser()).write(p));

        // ASSERT
        assertEquals("foo", json.getString("name"));
        assertEquals("NUMBER", json.getString("type"));
        assertEquals("123.456", json.getString("text"));
        assertNull(json.getBool("boolean"));
        assertNull(json.getDate("date"));
    }


    /**
     * Test.
     */
    public void testDeserializeText() {

        // ARRANGE
        final ParagraphSerializer serializer =
            new ParagraphSerializer(new ServerTextParser());
        final String pSer =
            serializer.write(Paragraph.fromText("bar", "foo"));

        // ACT
        final Paragraph p = serializer.read(pSer);

        // ASSERT
        assertEquals("bar", p.getName());
        assertEquals(ParagraphType.TEXT, p.getType());
        assertEquals("foo", p.getText());
        assertNull(p.getDate());
        assertNull(p.getBoolean());
    }


    /**
     * Test.
     */
    public void testDeserializeNumber() {

        // ARRANGE
        final ParagraphSerializer serializer =
            new ParagraphSerializer(new ServerTextParser());
        final String pSer =
            serializer.write(Paragraph.fromNumber("bar", 123.456));

        // ACT
        final Paragraph p = serializer.read(pSer);

        // ASSERT
        assertEquals("bar", p.getName());
        assertEquals(ParagraphType.NUMBER, p.getType());
        assertEquals(new BigDecimal("123.456"), p.getNumber());
        assertEquals("123.456", p.getText());
        assertNull(p.getDate());
        assertNull(p.getBoolean());
    }


    /**
     * Test.
     */
    public void testSerializeBoolean() {

        // ARRANGE
        final Paragraph p = Paragraph.fromBoolean("foo", true);

        // ACT
        final Json json = new JsonImpl(
            new ParagraphSerializer(new ServerTextParser()).write(p));

        // ASSERT
        assertEquals("foo", json.getString("name"));
        assertEquals("BOOLEAN", json.getString("type"));
        assertNull(json.getString("text"));
        assertTrue(json.getBool("boolean").booleanValue());
        assertNull(json.getDate("date"));
    }


    /**
     * Test.
     */
    public void testSerializeDate() {

        // ARRANGE
        final Date now = new Date();
        final Paragraph p = Paragraph.fromDate("foo", now);

        // ACT
        final Json json = new JsonImpl(
            new ParagraphSerializer(new ServerTextParser()).write(p));

        // ASSERT
        assertEquals("foo", json.getString("name"));
        assertEquals("DATE", json.getString("type"));
        assertNull(json.getString("text"));
        assertNull(json.getBool("boolean"));
        assertEquals(now, json.getDate("date"));
    }
}
