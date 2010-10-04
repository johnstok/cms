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
package ccc.persistence;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import ccc.api.core.SearchEngine;
import ccc.api.types.StreamAction;
import ccc.domain.Data;
import ccc.persistence.streams.CoreData;


/**
 * Tests for the {@link DataRepositoryImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class DataRepositoryImplTest extends TestCase {

    private final InputStream _dummyStream =
        new ByteArrayInputStream(new byte[]{1});

    private DataRepositoryImpl _dm;
    private SearchEngine _se;
    private CoreData _cd;


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
         _se = createStrictMock(SearchEngine.class);
         _cd = createStrictMock(CoreData.class);
         _dm = new DataRepositoryImpl(_cd);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _dm = null;
        _cd = null;
    }

    private void replayAll() {
        replay(_se, _cd);
    }

    private void verifyAll() {
        verify(_se, _cd);
    }

    /**
     * Test.
     */
    public void testCreate() {

        // ARRANGE
        final Data d = new Data();
        expect(_cd.create(_dummyStream, 1)).andReturn(d);
        replayAll();

        // ACT
        final Data actual = _dm.create(_dummyStream, 1);

        // ASSERT
        verifyAll();
        assertSame(d, actual);
    }

    /**
     * Test.
     */
    public void testRetrieve() {

        // ARRANGE
        final Data d = new Data();
        final StreamAction sa = new StreamAction(){
            @Override
            public void execute(final InputStream is) {
                // No Op
            }};
        _cd.retrieve(d, sa);
        replayAll();

        // ACT
        _dm.retrieve(d, sa);

        // ASSERT
        verifyAll();
    }
}
