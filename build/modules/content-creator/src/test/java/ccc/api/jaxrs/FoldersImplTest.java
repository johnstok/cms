/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.jaxrs;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import ccc.api.jaxrs.FoldersImpl;
import ccc.rest.Folders;
import ccc.rest.dto.ResourceSummary;


/**
 * Tests for the {@link FoldersImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class FoldersImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRootsMethodCallsFoldersService() {

        // ARRANGE
        expect(_foldersExt.roots()).andReturn(_rs);
        replay(_foldersExt);

        // ACT
        final Collection<ResourceSummary> actual = _unit.roots();

        // ASSERT
        assertEquals(_rs, actual);
        verify(_foldersExt);
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _foldersExt = createStrictMock(Folders.class);
        _unit = new FoldersImpl();
        _unit.setFolders(_foldersExt);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _foldersExt = null;
        _unit = null;
    }

    private Folders _foldersExt;
    private FoldersImpl _unit;
    private Collection<ResourceSummary> _rs = new ArrayList<ResourceSummary>();
}
