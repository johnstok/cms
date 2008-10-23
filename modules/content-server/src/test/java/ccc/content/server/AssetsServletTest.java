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

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.commons.MapRegistry;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.AssetManagerLocal;
import ccc.services.DataManagerLocal;
import ccc.services.ServiceNames;


/**
 * Tests for the {@link AssetsServlet}.
 *
 * @author Civic Computing Ltd.
 */
public class AssetsServletTest extends TestCase {

    /**
     * Test.
     * @throws IOException If stream manipulation fails.
     * @throws MimeTypeParseException  If it can't create the mime type.
     */
    public void testGetForExistingFileReturnsData()
                                            throws IOException,
                                                   MimeTypeParseException {

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
                     dummyData,
                     2345L,
                     new MimeType("text", "plain"));

        final DataManagerLocal dm = createStrictMock(DataManagerLocal.class);
        dm.retrieve(dummyData, os);
        replay(dm);

        final AssetManagerLocal am = createStrictMock(AssetManagerLocal.class);
        expect(am.lookup(new ResourcePath("/foo.txt"))).andReturn(dummyFile);
        replay(am);

        final HttpServletRequest request =
            createStrictMock(HttpServletRequest.class);
        expect(request.getPathInfo()).andReturn("/foo.txt");
        replay(request);

        final HttpServletResponse response =
            createStrictMock(HttpServletResponse.class);
        response.setHeader("Pragma", "no-cache");
        response.setHeader(
            "Cache-Control",
            "private, must-revalidate, max-age=0");
        response.setHeader("Expires", "0");
        response.setHeader(
            "Content-Disposition",
            "inline; filename=\""+dummyFile.name()+"\"");
        response.setHeader(
            "Content-Type",
            dummyFile.mimeType().toString());
        response.setHeader(
            "Content-Description",
            dummyFile.description());
        response.setHeader(
            "Content-Length",
            String.valueOf(dummyFile.size()));

        expect(response.getOutputStream()).andReturn(os);
        replay(response);

        // ACT
        new AssetsServlet(
            new MapRegistry().put(ServiceNames.ASSET_MANAGER_LOCAL, am)
                             .put(ServiceNames.DATA_MANAGER_LOCAL, dm))
            .doGet(request, response);

        // ASSERT
        verify(dm, am, request, response);
    }
}
