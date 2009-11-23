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
import java.util.UUID;

import ccc.rest.RestException;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.serialization.Json;
import ccc.serialization.JsonImpl;
import ccc.serialization.JsonKeys;
import ccc.types.MimeType;
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
        final PageDelta pd = getPages().pageDelta(page.getId());

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
        final Paragraph testPara =
            Paragraph.fromText("foo", "long story short");
        paras.add(testPara);
        final PageDelta modified = new PageDelta(paras);

        // ACT
        getCommands().lock(page.getId());

        final Json json = new JsonImpl();
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.TRUE);
        json.set(JsonKeys.COMMENT, "");
        json.set(JsonKeys.DELTA, modified);

        getPages().updatePage(page.getId(), json);
        final PageDelta pd = getPages().pageDelta(page.getId());

        // ASSERT
        assertNotNull("Page delta must not be null", pd);
        assertEquals(1, pd.getParagraphs().size());
        final List<Paragraph> results =
            new ArrayList<Paragraph>(pd.getParagraphs());
        assertEquals(testPara, results.get(0));
    }

    /**
     * Test.
     */
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
        final String okResult = getPages().validateFields(okjson);

        final Json nokjson = new JsonImpl();
        nokjson.set(JsonKeys.DEFINITION, definition);
        nokjson.set(JsonKeys.PARAGRAPHS, invalidParas);
        final String nokResult =  getPages().validateFields(nokjson);

        // ASSERT
        assertTrue("Errors should be empty", okResult.isEmpty());
        assertEquals("test, regexp: \\d{1,3}", nokResult);

    }

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testUpdateWorkingCopy() throws Exception {

        // ARRANGE
        final ResourceSummary templateFolder =
            resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final TemplateDelta newTemplate =
            new TemplateDelta("$resource.getParagraph(\"foo\").text()",
                "<fields><field name=\"foo\" type=\"html\"/></fields>",
                MimeType.HTML);

        final ResourceSummary ts =
            getTemplates().createTemplate(
                new TemplateDto(
                    templateFolder.getId(),
                    newTemplate,
                    "t-title",
                    "t-desc",
                    name));

        final ResourceSummary f = tempFolder();
        final ResourceSummary page = tempPage(f.getId(), ts.getId());


        final Set<Paragraph> paras = new HashSet<Paragraph>();
        final Paragraph testPara = Paragraph.fromText("foo", "original");
        paras.add(testPara);
        final PageDelta orignal = new PageDelta(paras);

        final Json json = new JsonImpl();
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.TRUE);
        json.set(JsonKeys.COMMENT, "");
        json.set(JsonKeys.DELTA, orignal);

        // ACT
        getCommands().lock(page.getId());
        getPages().updatePage(page.getId(), json);

        final Set<Paragraph> modparas = new HashSet<Paragraph>();
        final Paragraph modPara = Paragraph.fromText("foo", "working copy");
        modparas.add(modPara);
        final PageDelta modified = new PageDelta(modparas);

        getPages().updateWorkingCopy(page.getId(), modified);

        // ASSERT
        final String original = previewContent(page, false);
        final String wc = previewContent(page, true);
        assertEquals("original", original);
        assertEquals("working copy", wc);
    }

}
