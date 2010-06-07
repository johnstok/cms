/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.tests.acceptance;

import java.util.Collections;
import java.util.HashSet;

import ccc.api.core.File;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.MimeType;
import ccc.commons.Environment;


/**
 * Acceptance tests for Rhino plugin.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptingAcceptanceTest
    extends
        AbstractAcceptanceTest {



    /**
     * Test.
     */
    public void testHostnameProperty() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final Resource metadata = new Resource();
        metadata.setTitle("fail.js");
        metadata.setDescription("fail.js");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final ResourceSummary script =
            getFiles().createTextFile(
                new File(
                    folder.getId(),
                    "fail.js",
                    MimeType.TEXT,
                    true,
                    "",
                    "print(hostname);"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);

        // ACT
        final String pContent = getBrowser().previewContent(script, false);

        // ASSERT
        assertTrue(pContent.startsWith(Environment.getHostname()));
    }


    /**
     * Test.
     */
    public void testRandomProperty() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final Resource metadata = new Resource();
        metadata.setTitle("fail.js");
        metadata.setDescription("fail.js");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final ResourceSummary script =
            getFiles().createTextFile(
                new File(
                    folder.getId(),
                    "fail.js",
                    MimeType.TEXT,
                    true,
                    "",
                    "print(random.nextInt(10));"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);

        // ACT
        final String pContent = getBrowser().previewContent(script, false);

        // ASSERT
        assertTrue(0 < Integer.valueOf(pContent).intValue());
    }
}
