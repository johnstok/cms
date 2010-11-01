/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.tests.acceptance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import ccc.api.core.File;
import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.PageCriteria;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;


/**
 * Tests for the taxonomy features.
 *
 * @author Civic Computing Ltd.
 */
public class TaxonomyAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     */
    public void testTaxonomySearch() {

        // ARRANGE
        final String id1 = UUID.randomUUID().toString();
        final String id2 = UUID.randomUUID().toString();

        final Folder f = tempFolder();
        final String pname = UUID.randomUUID().toString();
        final Page page = new Page(f.getId(),
            pname,
            null,
            "title",
            "",
            true);
        final List<String> terms = new ArrayList<String>();
        terms.add(id1.toString());
        terms.add(id2.toString());

        page.setParagraphs(new HashSet<Paragraph>(){{
            add(Paragraph.fromTaxonomy("category", terms));
        }});

        final Page ps = getPages().create(page);

        final PageCriteria criteria = new PageCriteria();
        criteria.matchParagraph("category",
                                "%"+id1.toString()+"%");

        // ACT
        final PagedCollection<ResourceSummary> pages =
            getPages().list(criteria, 1, 20);

        // ASSERT
        assertEquals(1, pages.getElements().size());
        assertEquals(ps.getId(), pages.getElements().get(0).getId());
    }

    /**
     * Test.
     */
    public void testTaxonomyRendering() {

        // ARRANGE
        // create vocabulary
        final String id1 = UUID.randomUUID().toString();
        final String id2 = UUID.randomUUID().toString();
        final String id3 = UUID.randomUUID().toString();

        final String fName = UUID.randomUUID().toString();
        final Resource filesFolder =
            getCommands().resourceForPath("/files");

        final String vocabulary = "<vocabulary>"
            +"<term id=\""+id1+"\" title=\"ground\">"
            +"<term id=\""+id2+"\" title=\"car\"/>"
            +"<term id=\""+id3+"\" title=\"truck\"/>"
            +"</term>"
            +"</vocabulary>";

        final File rs =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    vocabulary));

        // create template
        final Resource templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final String fields = "<fields>"
            + "<field name=\"category\" type=\"taxonomy\" vocabulary=\""
            +rs.getId().toString()+"\"/>"
            + "</fields>";

        final String body = ""
       		+ "#set($resources = $services.getResources())"
       		+ "#set($pages = $services.getPages())"
       		+ "#set($files = $services.getFiles())"
       		+ "#set($templates = $services.getTemplates())"
       		+ "#set($templateid = $resource.getTemplate())"
       		+ "#set($template = $templates.retrieve($templateid))"
       		+ "#set($definition = $template.getDefinition())"
       		+ "#set($vocabularyId = $uuid.fromString($taxonomy.resolveVocabularyID(\"category\", $definition)))"
       		+ "#set($vocabulary = $files.retrieve($vocabularyId).getContent())"
       		+ "#set($terms = $resource.getParagraph(\"category\").getList())"
       		+ "#foreach($term in $terms)"
       		+ "$taxonomy.resolveTermTitle($vocabulary, $term) "
       		+ "#end";

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(body);
        t.setDefinition(fields);
        t.setMimeType(MimeType.HTML);

        final Template ts = getTemplates().create(t);


        // create page
        final String pname = UUID.randomUUID().toString();
        final Page page = new Page(templateFolder.getId(),
            pname,
            ts.getId(),
            "title",
            "",
            true);
        final List<String> terms = new ArrayList<String>();
        terms.add(id1.toString());
        terms.add(id2.toString());

        page.setParagraphs(new HashSet<Paragraph>(){{
            add(Paragraph.fromTaxonomy("category", terms));
        }});

        final Page ps = getPages().create(page);
        getCommands().lock(ps.getId());
        getCommands().publish(ps.getId());

        // ACT
        // render page
        final String pContent = getBrowser().previewContent(ps, false);

        // ASSERT
        assertEquals("ground car ", pContent);

    }


}
