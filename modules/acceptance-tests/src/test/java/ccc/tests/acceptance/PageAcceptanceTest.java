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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.Page;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;


/**
 * Acceptance tests for Pages REST methods.
 *
 * @author Civic Computing Ltd.
 */
public class PageAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     */
    public void testCreatePage() {

        // ARRANGE
        final String hw = "Hello World"; // Unicode non-breaking space.
        final ResourceSummary f = tempFolder();
        final String name = UUID.randomUUID().toString();
        final Page page = new Page(f.getId(),
            name,
            null,
            "title",
            "",
            true);
        page.setParagraphs(new HashSet<Paragraph>(){{
            add(Paragraph.fromText("test", hw));
        }});

        // ACT
        final ResourceSummary ps = getPages().createPage(page);

        // ASSERT
        final Page pd = getPages().pageDelta(ps.getId());
        assertEquals(hw, pd.getParagraph("test").getText());
    }

    /**
     * Test.
     */
    public void testPageDelta() {

        // ARRANGE
        final ResourceSummary f = tempFolder();
        final ResourceSummary template =
            dummyTemplate(getCommands().resourceForPath(""));
        final ResourceSummary page = tempPage(f.getId(), template.getId());

        // ACT
        final Page pd = getPages().pageDelta(page.getId());

        // ASSERT
        assertNotNull("Page delta must not be null", pd);
    }

    /**
     * Test.
     */
    public void testUpdatePage() {

        // ARRANGE
        final ResourceSummary f = tempFolder();
        final ResourceSummary template =
            dummyTemplate(getCommands().resourceForPath(""));
        final ResourceSummary page = tempPage(f.getId(), template.getId());

        final Set<Paragraph> paras = new HashSet<Paragraph>();
        final Paragraph testPara =
            Paragraph.fromText("foo", "long story short");
        paras.add(testPara);
        final Page modified = Page.delta(paras);
        modified.setMajorChange(true);
        modified.setComment("");

        // ACT
        getCommands().lock(page.getId());


        getPages().updatePage(page.getId(), modified);
        final Page pd = getPages().pageDelta(page.getId());

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
        final ResourceSummary f = tempFolder();
        final Template t = new Template();
        t.setParent(f.getId());
        t.setName(new ResourceName("example"));
        t.setTitle("example");
        t.setDescription("example");
        t.setMimeType(MimeType.HTML);
        t.setDefinition(
            "<fields>"
            + "<field name=\"test\" type=\"text_field\" regexp=\"\\d{1,3}\"/>"
            + "</fields>");
        t.setBody("empty");
        final ResourceSummary ts = getTemplates().createTemplate(t);

        final Paragraph invalidPara = Paragraph.fromText("test", "fail");
        final Paragraph validPara = Paragraph.fromText("test", "12");

        final Set<Paragraph> invalidParas = new HashSet<Paragraph>();
        invalidParas.add(invalidPara);
        final Set<Paragraph> validParas = new HashSet<Paragraph>();
        validParas.add(validPara);

        // ACT
        final Page okParas = new Page();
        okParas.setTemplate(ts.getId());
        okParas.setParagraphs(validParas);
        final String okResult = getPages().validateFields(okParas);

        final Page nokParas = new Page();
        nokParas.setTemplate(ts.getId());
        nokParas.setParagraphs(invalidParas);
        final String nokResult =  getPages().validateFields(nokParas);

        // ASSERT
        assertTrue("Errors should be empty", okResult.isEmpty());
        assertEquals("test, regexp: \\d{1,3}", nokResult);

    }

    /**
     * Test.
     */
    public void testUpdateWorkingCopy() {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$resource.getParagraph(\"foo\").getText()");
        t.setDefinition("<fields><field name=\"foo\" type=\"html\"/></fields>");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary ts = getTemplates().createTemplate(t);

        final ResourceSummary f = tempFolder();
        final ResourceSummary page = tempPage(f.getId(), ts.getId());

        final Page update = new Page();
        update.setMajorChange(true);
        update.setComment("");
        final Set<Paragraph> paras = new HashSet<Paragraph>();
        final Paragraph testPara = Paragraph.fromText("foo", "original");
        paras.add(testPara);
        update.setParagraphs(paras);

        // ACT
        getCommands().lock(page.getId());
        getPages().updatePage(page.getId(), update);

        final Set<Paragraph> modparas = new HashSet<Paragraph>();
        final Paragraph modPara = Paragraph.fromText("foo", "working copy");
        modparas.add(modPara);
        final Page modified = Page.delta(modparas);

        getPages().updateWorkingCopy(page.getId(), modified);

        // ASSERT
        final String original = getBrowser().previewContent(page, false);
        final String wc = getBrowser().previewContent(page, true);
        assertEquals("original", original);
        assertEquals("working copy", wc);
    }

}
