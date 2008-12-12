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
package ccc.services.ejb3.local;

import static org.easymock.EasyMock.*;

import javax.persistence.EntityManager;

import junit.framework.TestCase;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.services.AliasDao;
import ccc.services.AuditLog;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AliasDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCreateAlias() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder("foo");
        contentRoot.add(foo);
        final Alias alias = new Alias("bar", foo);

        _al.recordCreate(alias);
        expect(_em.find(Folder.class, contentRoot.id()))
            .andReturn(contentRoot);
        _em.persist(alias);
        replay(_em, _al);


        // ACT
        _cm.create(contentRoot.id(), alias);


        // ASSERT
        verify(_em, _al);
        assertEquals(2, contentRoot.size());
        assertEquals(alias, contentRoot.entries().get(1));
    }

    /**
     * Test.
     */
    public void testCreateAliasFailsForDuplicates() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder("foo");
        contentRoot.add(foo);
        final Alias alias = new Alias("bar", foo);
        contentRoot.add(alias);
        final Alias aliasCopy = new Alias("bar", foo);

        expect(_em.find(Folder.class, contentRoot.id()))
            .andReturn(contentRoot);
        replay(_em, _al);


        // ACT
        try {
            _cm.create(contentRoot.id(), aliasCopy);
            fail("Alias creation should fail for duplicate name");


        // ASSERT
        } catch (final CCCException e) {
            assertEquals("Folder already contains a resource with name 'bar'.",
                e.getMessage());
        }
        verify(_em, _al);
    }

    /**
     * Test.
     */
    public void testUpdateAlias() {

        // ARRANGE
        final Page resource = new Page("foo");
        final Page r2 = new Page("baa");
        final Alias alias = new Alias("alias", resource);

        expect(_em.find(Resource.class, r2.id())).andReturn(r2);
        expect(_em.find(Alias.class, alias.id())).andReturn(alias);
        _al.recordUpdate(alias);
        replay(_em, _al);

        // ACT
        _cm.updateAlias(r2.id(), alias.id(), alias.version());

        // ASSERT
        verify(_em, _al);
        assertEquals(r2, alias.target());
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _al = createStrictMock(AuditLog.class);
        _cm = new AliasDaoImpl(_em, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _al = null;
        _cm = null;
    }

    private EntityManager _em;
    private AuditLog _al;
    private AliasDao _cm;
}
