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
package ccc.content.server;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;
import ccc.domain.Page;
import ccc.services.StatefulReader;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultLocatorTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testLocateByPath() {

        // ARRANGE
        final Page p = new Page("wow");
        expect(_reader.lookup("foo_root", p.absolutePath()))
            .andReturn(p);
        replay(_reader);

        // ACT
        _locator.locate(p.absolutePath());

        // ASSERT
        verify(_reader);

    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _reader = createStrictMock(StatefulReader.class);
        _locator = new DefaultLocator(_reader, "foo_root");
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _locator = null;
        _reader = null;
    }

    private DefaultLocator _locator;
    private StatefulReader _reader;
}
