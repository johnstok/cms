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
package ccc.api.types;

import junit.framework.TestCase;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class EnumTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testReadSortOrder() {

        // ARRANGE

        // ACT
        for (final SortOrder o : SortOrder.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadActionStatus() {

        // ARRANGE

        // ACT
        for (final ActionStatus o : ActionStatus.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadCommentStatus() {

        // ARRANGE

        // ACT
        for (final CommentStatus o : CommentStatus.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadCommandType() {

        // ARRANGE

        // ACT
        for (final CommandType o : CommandType.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadFailureCode() {

        // ARRANGE

        // ACT
        for (final FailureCode o : FailureCode.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadResourceType() {

        // ARRANGE

        // ACT
        for (final ResourceType o : ResourceType.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadResourceOrder() {

        // ARRANGE

        // ACT
        for (final ResourceOrder o : ResourceOrder.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadParagraphType() {

        // ARRANGE

        // ACT
        for (final ParagraphType o : ParagraphType.values()) {
            o.toString();
        }
        // ASSERT

    }


    /**
     * Test.
     */
    public void testReadPermission() {

        // ARRANGE

        // ACT
        for (final String o : Permission.ALL) {
            o.toString();
        }
        // ASSERT

    }
}
