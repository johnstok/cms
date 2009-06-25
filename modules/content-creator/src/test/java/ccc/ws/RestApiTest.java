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
package ccc.ws;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;
import ccc.api.Queries;
import ccc.api.ResourceSummary;


/**
 * Tests for the {@link RestApi} class.
 *
 * @author Civic Computing Ltd.
 */
public class RestApiTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRootsMethodCallsQueriesService() {

        // ARRANGE
        expect(_queries.roots()).andReturn(_rs);
        replay(_queries);

        // ACT
        final Collection<ResourceSummary> actual = _unit.roots();

        // ASSERT
        assertSame(_rs, actual);
        verify(_queries);
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _queries = createStrictMock(Queries.class);
        _unit = new RestApi(_queries);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _queries = null;
        _unit = null;
    }

    private Queries _queries;
    private RestApi _unit;
    private Collection<ResourceSummary> _rs = new ArrayList<ResourceSummary>();
}
