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
import ccc.commons.Registry;
import ccc.commons.Testing;
import ccc.services.DataManager;
import ccc.services.ServiceNames;
import ccc.services.StatefulReader;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultRendererFactoryTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testNewInstance() {

        // ARRANGE
        expect(_registry.get(ServiceNames.DATA_MANAGER_LOCAL))
            .andReturn(Testing.dummy(DataManager.class));
        expect(_registry.get(ServiceNames.STATEFUL_READER))
            .andReturn(Testing.dummy(StatefulReader.class));
        replay(_registry);

        // ACT
        _factory.newInstance();

        // ASSERT
        verify(_registry);
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _registry = createStrictMock(Registry.class);
        _factory = new DefaultRendererFactory(_registry);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _factory = null;
        _registry = null;
    }

    private DefaultRendererFactory _factory;
    private Registry _registry;
}
