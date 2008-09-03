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
package ccc.content.server;

import static org.easymock.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.commons.MapRegistry;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.AssetManager;
import ccc.services.DataManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AssetsServletTest extends TestCase {

    /**
     * Test.
     * @throws IOException If stream manipulation fails.
     */
    public void testGetForExistingFileReturnsData() throws IOException {

        // ARRANGE
        final ByteArrayOutputStream actual = new ByteArrayOutputStream();
        final ServletOutputStream os =
            new ServletOutputStream(){
                @Override public void write(final int b) throws IOException {
                    actual.write(b);
                }};
        final Data dummyData = new Data();
        final File dummyFile =
            new File(new ResourceName("foo.txt"),
                     "foo.txt",
                     "my desc",
                     dummyData);

        final DataManager dm = createStrictMock(DataManager.class);
        dm.retrieve(dummyData, os);
        replay(dm);

        final AssetManager am = createStrictMock(AssetManager.class);
        expect(am.lookup(new ResourcePath("/foo.txt/"))).andReturn(dummyFile);
        replay(am);

        final HttpServletRequest request =
            createStrictMock(HttpServletRequest.class);
        expect(request.getPathInfo()).andReturn("/foo.txt/");
        replay(request);

        final HttpServletResponse response =
            createStrictMock(HttpServletResponse.class);
        // response.setHeader(
//                "Content-Disposition",
//                "filename=\""+dummyFile.name()+"\"" ) ;
        // Content-Type: text/html; charset=ISO-8859-4
        // Content-Description: just a small picture of me
        // Content-Length

        expect(response.getOutputStream()).andReturn(os);
        replay(response);

        // ACT
        new AssetsServlet(new MapRegistry().put("AssetManagerEJB/local", am)
                                           .put("DataManagerEJB/local", dm))
            .doGet(request, response);

        // ASSERT
        verify(dm, am, request, response);
    }
}
