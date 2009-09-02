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
        expect(_folders.roots()).andReturn(_rs);
        replay(_folders);

        // ACT
        final Collection<ResourceSummary> actual = _unit.roots();

        // ASSERT
        assertEquals(_rs, actual);
        verify(_folders);
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _folders = createStrictMock(Folders.class);
        _unit = new FoldersImpl();
        _unit.setFolderCommands(_folders);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _folders = null;
        _unit = null;
    }

    private Folders _folders;
    private FoldersImpl _unit;
    private Collection<ResourceSummary> _rs = new ArrayList<ResourceSummary>();
}
