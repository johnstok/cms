/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration.ccc6.handlers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.h2.tools.Csv;

import ccc.migration.ResourceBean;


/**
 * Tests for the {@link ResourceSelector} class.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSelectorTest
    extends
        TestCase {

    /**
     * Test.
     * @throws IOException If reading CSV fails.
     * @throws SQLException From JDBC API.
     */
    public void testHandle() throws SQLException, IOException {

        // ARRANGE
        final Csv csv = Csv.getInstance();
        final ResultSet rs =
            csv.read(
                new StringReader(",,,,PUBLISHED,,,,,,"),
                new String[]{
                    "CONTENT_ID",
                    "CONTENT_TYPE",
                    "NAME",
                    "PAGE",
                    "STATUS",
                    "VERSION_ID",
                    "PERMISSION_NAME",
                    "INDEX_TITLE",
                    "USE_IN_INDEX",
                    "DESCRIPTION",
                    "EXPIRY_DATE"});

        // ACT
        final List<ResourceBean> result = new ResourceSelector().handle(rs);

        // ASSERT
        assertEquals(1, result.size());
        assertTrue(result.get(0).isPublished());
    }

    /**
     * Test.
     * @throws IOException If reading CSV fails.
     * @throws SQLException From JDBC API.
     */
    public void testIndexInTitle() throws SQLException, IOException {

        // ARRANGE
        final Csv csv = Csv.getInstance();
        final ResultSet rs =
            csv.read(
                new StringReader(",,,,PUBLISHED,,,TestTitle,,,"),
                new String[]{
                    "CONTENT_ID",
                    "CONTENT_TYPE",
                    "NAME",
                    "PAGE",
                    "STATUS",
                    "VERSION_ID",
                    "PERMISSION_NAME",
                    "INDEX_TITLE",
                "USE_IN_INDEX",
                "DESCRIPTION",
                "EXPIRY_DATE"});

        // ACT
        final List<ResourceBean> result = new ResourceSelector().handle(rs);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("TestTitle", result.get(0).title());
    }
}
