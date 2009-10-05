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
package ccc.persistence;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import ccc.domain.Data;
import ccc.persistence.DataRepositoryImpl;
import ccc.persistence.StreamAction;
import ccc.persistence.streams.CoreData;
import ccc.search.SearchEngine;


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

//    /**
//     * Test.
//     * @throws SQLException sometimes.
//     */
//    public void testCreateFileData() throws SQLException {
//
//        // ARRANGE
//        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
//        final File file = new File(
//            new ResourceName("file"), "title", "desc", new Data(), 0);
//
//        _ps.setString(eq(1), isA(String.class));
//        _ps.setInt(2, 0);
//        _ps.setBinaryStream(DataManagerEJB.STREAM_POSITION_CREATE,
//                           _dummyStream,
//                           Integer.MAX_VALUE);
//        expect(Boolean.valueOf(_ps.execute())).andReturn(Boolean.TRUE);
//        _ps.close();
//
//        expect(_c.prepareStatement(DataManagerEJB.CREATE_STATEMENT))
//            .andReturn(_ps);
//        _c.close();
//
//        expect(_ds.getConnection()).andReturn(_c);
//
//        _al.create(assetRoot.id(), file);
//
//        replayAll();
//
//
//        // ACT
//        _dm.createFile(file, assetRoot.id(), _dummyStream);
//
//        // VERIFY
//        verifyAll();
//    }

//    /**
//     * Test.
//     * @throws SQLException From JDBC.
//     */
//    public void testUpdateFile() throws SQLException {
//
//        // ARRANGE
//        _ps.setString(eq(1), isA(String.class));
//        _ps.setInt(2, 0);
//        _ps.setBinaryStream(DataManagerEJB.STREAM_POSITION_CREATE,
//                           _dummyStream,
//                           Integer.MAX_VALUE);
//        expect(Boolean.valueOf(_ps.execute())).andReturn(Boolean.TRUE);
//        _ps.close();
//
//        expect(_c.prepareStatement(DataManagerEJB.CREATE_STATEMENT))
//            .andReturn(_ps);
//        _c.close();
//
//        expect(_ds.getConnection()).andReturn(_c);
//
//        final File f =
//            new File(new ResourceName("foo"), "bar", "x", new Data(), 0);
//        expect(_al.findLocked(File.class, f.id())).andReturn(f);
//        _al.update(f);
//
//        expect(_ds.getConnection()).andReturn(_c);
//
//
//        replayAll();
//
//        // ACT
//        _dm.updateFile(f.id(), "x", "x", new MimeType(), 1, _dummyStream);
//
//        // ASSERT
//        verifyAll();
//    }

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
