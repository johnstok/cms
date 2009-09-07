/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.impl;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;


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
        _foldersExt = createStrictMock(FoldersExt.class);
        _unit = new FoldersImpl();
        _unit.setFolderCommands(_foldersExt);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _foldersExt = null;
        _unit = null;
    }

    private FoldersExt _foldersExt;
    private FoldersImpl _unit;
    private Collection<ResourceSummary> _rs = new ArrayList<ResourceSummary>();
}
