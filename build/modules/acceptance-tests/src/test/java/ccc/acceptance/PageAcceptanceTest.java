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
package ccc.acceptance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.rest.RestException;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.Json;
import ccc.serialization.JsonImpl;
import ccc.serialization.JsonKeys;
import ccc.types.Paragraph;


/**
 * Acceptance tests for Pages REST methods.
 *
 * @author Civic Computing Ltd.
 */
public class PageAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testPageDelta() throws RestException {

        // ARRANGE
        final ResourceSummary f = tempFolder();
        final ResourceSummary template =
            dummyTemplate(resourceForPath("/content"));
        final ResourceSummary page = tempPage(f.getId(), template.getId());

        // ACT
        final PageDelta pd = _pages.pageDelta(page.getId());

        // ASSERT
        assertNotNull("Page delta must not be null", pd);
    }

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUpdatePage() throws RestException {

        // ARRANGE
        final ResourceSummary f = tempFolder();
        final ResourceSummary template =
            dummyTemplate(resourceForPath("/content"));
        final ResourceSummary page = tempPage(f.getId(), template.getId());

        final Set<Paragraph> paras = new HashSet<Paragraph>();
        final Paragraph testPara = Paragraph.fromText("foo", "long story short");
        paras.add(testPara);
        final PageDelta modified = new PageDelta(paras);

        // ACT
        _commands.lock(page.getId());

        final Json json = new JsonImpl();
        json.set(JsonKeys.MAJOR_CHANGE, true);
        json.set(JsonKeys.COMMENT, "");
        json.set(JsonKeys.DELTA, modified);

        _pages.updatePage(page.getId(), json);
        final PageDelta pd = _pages.pageDelta(page.getId());

        // ASSERT
        assertNotNull("Page delta must not be null", pd);
        assertEquals(1, pd.getParagraphs().size());
        final List<Paragraph> results =
            new ArrayList<Paragraph>(pd.getParagraphs());
        assertEquals(testPara, results.get(0));
    }

    public void testValidateFields() {

        // ARRANGE
        final String definition = "<fields><field name=\"test\" "
        		+ "type=\"text_field\" regexp=\"\\d{1,3}\"/></fields>";
        final Paragraph invalidPara = Paragraph.fromText("test", "fail");
        final Paragraph validPara = Paragraph.fromText("test", "12");

        final Set<Paragraph> invalidParas = new HashSet<Paragraph>();
        invalidParas.add(invalidPara);
        final Set<Paragraph> validParas = new HashSet<Paragraph>();
        validParas.add(validPara);

        // ACT
        final Json okjson = new JsonImpl();
        okjson.set(JsonKeys.DEFINITION, definition);
        okjson.set(JsonKeys.PARAGRAPHS, validParas);
        final String okResult = _pages.validateFields(okjson);

        final Json nokjson = new JsonImpl();
        nokjson.set(JsonKeys.DEFINITION, definition);
        nokjson.set(JsonKeys.PARAGRAPHS, invalidParas);
        final String nokResult =  _pages.validateFields(nokjson);

        // ASSERT
        assertTrue("Errors should be empty", okResult.isEmpty());
        assertEquals("test, regexp: \\d{1,3}", nokResult);

    }

    //updateWorkingCopy

}
