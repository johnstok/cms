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
package ccc.services.ejb3;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;

import junit.framework.TestCase;
import ccc.domain.Data;
import ccc.services.CoreData;
import ccc.services.Dao;
import ccc.services.SearchEngine;
import ccc.services.DataManager.StreamAction;
import ccc.services.impl.DataManagerEJB;


/**
 * Tests for the {@link DataManagerEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class DataManagerEJBTest extends TestCase {

    private final InputStream _dummyStream =
        new ByteArrayInputStream(new byte[]{1});

    private Dao _dao;
    private DataManagerEJB _dm;
    private SearchEngine _se;
    private CoreData _cd;


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
         _dao = createStrictMock(Dao.class);
         _se = createStrictMock(SearchEngine.class);
         _cd = createStrictMock(CoreData.class);
         _dm = new DataManagerEJB(_cd, _dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _dm = null;
        _cd = null;
    }

    private void replayAll() {
        replay(_dao, _se, _cd);
    }

    private void verifyAll() {
        verify(_dao, _se, _cd);
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
     * @throws SQLException sometimes.
     */
    @SuppressWarnings("boxing")
    public void testCreate() throws SQLException {

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
     * @throws SQLException sometimes.
     */
    @SuppressWarnings("boxing")
    public void testRetrieve() throws SQLException {

        // ARRANGE
        final Data d = new Data();
        final StreamAction sa = new StreamAction(){
            @Override
            public void execute(final InputStream is) throws Exception {
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
