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

import static org.easymock.EasyMock.*;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.TestCase;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;
import ccc.plugins.s11n.Json;


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
    public void testCreateSnapshot() {

        // ARRANGE
        final Paragraph p = Paragraph.fromText("foo", "bar");

        _json.set("name", "foo");
        _json.set("type", "TEXT");
        _json.set("text", "bar");
        _json.set("boolean", (Boolean) null);
        _json.set("date", (Date) null);
        replay(_json);

        // ACT
        new ParagraphSerializer().write(_json, p);

        // ASSERT
        verify(_json);
    }


    /**
     * Test.
     */
    public void testCreateNumberSnapshot() {

        // ARRANGE
        final Paragraph p =
            Paragraph.fromNumber("foo", new BigDecimal("123.456"));

        _json.set("name", "foo");
        _json.set("type", "NUMBER");
        _json.set("text", "123.456");
        _json.set("boolean", (Boolean) null);
        _json.set("date", (Date) null);
        replay(_json);

        // ACT
        new ParagraphSerializer().write(_json, p);

        // ASSERT
        verify(_json);
    }


    /**
     * Test.
     */
    public void testFromSnapshot() {

        // ARRANGE
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("TEXT");
        expect(_json.getString("text")).andReturn("foo");
        replay(_json);

        // ACT
        final Paragraph p = new ParagraphSerializer().read(_json);

        // ASSERT
        verify(_json);
        assertEquals("bar", p.getName());
        assertEquals(ParagraphType.TEXT, p.getType());
        assertEquals("foo", p.getText());
        assertNull(p.getDate());
        assertNull(p.getBoolean());
    }


    /**
     * Test.
     */
    public void testFromNumberSnapshot() {

        // ARRANGE
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("NUMBER");
        expect(_json.getString("text")).andReturn("123.456");
        replay(_json);

        // ACT
        final Paragraph p = new ParagraphSerializer().read(_json);

        // ASSERT
        verify(_json);
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
    public void testParagraphConstructorBooleanTypeJSON() {

        final Paragraph p = Paragraph.fromText("foo", "bar");

        _json.set("name", "foo");
        _json.set("type", "TEXT");
        _json.set("text", "bar");
        _json.set("boolean", (Boolean) null);
        _json.set("date", (Date) null);
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("BOOLEAN");
        expect(_json.getBool("boolean")).andReturn(true);
        replay(_json);
        new ParagraphSerializer().write(_json, p);


        final Paragraph p2 = new ParagraphSerializer().read(_json);
        assertNull(p.getBoolean());
        assertEquals(p2.getBoolean(), new Boolean(true));

    }


    /**
     * Test.
     */
    public void testParagraphConstructorDateTypeJSON() {

        final Date testDate = new Date(System.currentTimeMillis());

        final Paragraph p = Paragraph.fromDate("foo", testDate);
        _json.set("name", "foo");
        _json.set("type", "DATE");
        _json.set("text", (String) null);
        _json.set("boolean", (Boolean) null);
        _json.set("date", testDate);
        expect(_json.getString("name")).andReturn("bar");
        expect(_json.getString("type")).andReturn("DATE");
        expect(_json.getDate("date")).andReturn(testDate);
        replay(_json);
        new ParagraphSerializer().write(_json, p);

        final Paragraph p2 = new ParagraphSerializer().read(_json);
        assertEquals(p2.getDate(), testDate);
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _json = createStrictMock(Json.class);
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _json = null;
    }


    private Json _json;
}
