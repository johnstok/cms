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

import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.PageCriteria;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.types.Paragraph;


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

}
