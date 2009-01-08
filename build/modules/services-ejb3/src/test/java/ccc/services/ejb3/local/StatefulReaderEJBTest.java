/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3.local;

import static org.easymock.EasyMock.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.StatefulReader;


/**
 * Tests for the {@link ContentManagerEJB} class.
 *
 * @author Civic Computing Ltd
 */
public final class StatefulReaderEJBTest extends TestCase {



    /**
     * Test.
     */
    public void testLookup() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder("foo");
        final Page bar =
            new Page("bar")
                .addParagraph(
                    Paragraph.fromText("default", "<H1>Default</H!>"));
        contentRoot.add(foo);
        foo.add(bar);

        expect(_em.createNamedQuery("rootByName")).andReturn(_q);
        expect(
            _q.setParameter(1,
                            new ResourceName(PredefinedResourceNames.CONTENT)))
            .andReturn(_q);
        expect(_q.getSingleResult()).andReturn(contentRoot);
        replay(_em, _q);


        // ACT
        final Resource resource =
            _cm.lookup(new ResourcePath("/foo/bar"));


        // ASSERT
        verify(_em, _q);
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.paragraphs().size());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _q = createStrictMock(Query.class);
        _cm = new StatefulReaderEJB(_em);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _q = null;
        _cm = null;
    }

    private EntityManager _em;
    private Query _q;
    private StatefulReader _cm;
}
