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

import java.util.HashMap;
import java.util.Map;

import ccc.api.types.Link;
import ccc.commons.NormalisingEncoder;

import junit.framework.TestCase;


/**
 * Tests for the {@link Link} class.
 * <br>These tests use the {@link NormalisingEncoder}.
 *
 * @author Civic Computing Ltd.
 */
public class URIBuilderTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testReplace() {

        // ARRANGE
        final Link b = new Link("/foo/{id}/baz");

        // ACT
        String uri = b.build("id", "bar", new NormalisingEncoder());

        // ASSERT
        assertEquals("/foo/bar/baz", uri);
    }
    
    public void testDraft3Tests() throws Exception {

        // ARRANGE
        Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("foo",     new String[] {"\u03d3"});
        params.put("bar",     new String[] {"fred"});
        params.put("baz",     new String[] {"10,20,30"});
        params.put("qux",     new String[] {"10","20","30"});
        params.put("corge",   new String[] {});
        params.put("grault",  new String[] {""});
        params.put("garply",  new String[] {"a/b/c"});
        params.put("waldo",   new String[] {"ben & jerrys"});
        params.put("fred",    new String[] {"fred", "", "wilma"});
        params.put("plugh",   new String[] {"\u017F\u0307", "\u0073\u0307"});
        params.put("1-a_b.c", new String[] {"200"});
        String[][] tests = {
            {"http://example.org/?q={bar}", "http://example.org/?q=fred"},
            {"/{xyzzy}", "/"},
            {"http://example.org/?{-join|&|foo,bar,xyzzy,baz}", "http://example.org/?foo=%CE%8E&bar=fred&baz=10%2C20%2C30"},
            {"http://example.org/?d={-list|,|qux}", "http://example.org/?d=10,20,30"},
            {"http://example.org/?d={-list|&d=|qux}", "http://example.org/?d=10&d=20&d=30"},
            {"http://example.org/{bar}{bar}/{garply}", "http://example.org/fredfred/a%2Fb%2Fc"},
            {"http://example.org/{bar}{-prefix|/|fred}", "http://example.org/fred/fred//wilma"},
            {"{-neg|:|corge}{-suffix|:|plugh}", ":%E1%B9%A1:%E1%B9%A1:"},
            {"../{waldo}/", "../ben%20%26%20jerrys/"},
            {"telnet:192.0.2.16{-opt|:80|grault}", "telnet:192.0.2.16:80"},
            {":{1-a_b.c}:", ":200:"},
        };

        // ACT
        for (String[] test : tests) {
            
        // ASSERT
            assertEquals(test[1], new Link(test[0]).build(params, new NormalisingEncoder()));
        }
    }
}
