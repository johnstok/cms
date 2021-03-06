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

import java.util.UUID;

import junit.framework.TestCase;
import ccc.api.core.Alias;


/**
 * Tests for the {@link AliasSerializer} class.
 *
 * @author Civic Computing Ltd.
 */
public class AliasSerializerTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testConvertToJson() {

        // ARRANGE
        final UUID targetId = UUID.randomUUID();
        final Alias a = new Alias();
        a.setTargetId(targetId);

        // ACT
        final Json json =  new JsonImpl(
            new AliasSerializer(new ServerTextParser()).write(a));

        // ASSERT
        assertEquals(targetId, json.getId(JsonKeys.TARGET_ID));
    }


    /**
     * Test.
     */
    public void testConvertToJsonWithMissingTarget() {

        // ARRANGE
        final Alias a = new Alias();

        // ACT
        final Json json =  new JsonImpl(
            new AliasSerializer(new ServerTextParser()).write(a));

        // ASSERT
        assertNull(json.getId(JsonKeys.TARGET_ID));
    }
}
