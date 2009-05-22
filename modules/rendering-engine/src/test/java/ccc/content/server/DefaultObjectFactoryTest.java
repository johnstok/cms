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

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultObjectFactoryTest
    extends
        TestCase {

    private static final String DATA_MANAGER_LOCAL =
        "hello-world/DataManager/local";
    private static final String SEARCH_ENGINE_LOCAL =
        "hello-world/Search/local";
    private static final String STATEFUL_READER =
        "hello-world/StatefulReader/local";
//    /**
//     * Test.
//     */
//    public void testRespectVisiblityIsTrueByDefault() {
//
//        // ARRANGE
//
//        // ACT
//
//        // ASSERT
//        assertTrue("Should be true.", _factory.getRespectVisibility());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRespectVisiblityCanBeSetToFalse() {
//
//        // ARRANGE
//        final String respectVisibility = "false";
//
//        // ACT
//        _factory.setRespectVisibility(respectVisibility);
//
//        // ASSERT
//        assertFalse("Should be false.", _factory.getRespectVisibility());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testTrueStringSetsRespectVisibilityToTrue() {
//
//        // ARRANGE
//        _factory.setRespectVisibility("false");
//        final String respectVisibility = "true";
//
//        // ACT
//        _factory.setRespectVisibility(respectVisibility);
//
//        // ASSERT
//        assertTrue("Should be true.", _factory.getRespectVisibility());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testInvalidStringsSetRespectVisibilityToTrue() {
//
//        // ARRANGE
//        final String respectVisibility = "foo";
//
//        // ACT
//        _factory.setRespectVisibility(respectVisibility);
//
//        // ASSERT
//        assertTrue("Should be true.", _factory.getRespectVisibility());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testEmptyStringsSetRespectVisibilityToTrue() {
//
//        // ARRANGE
//        final String respectVisibility = "";
//
//        // ACT
//        _factory.setRespectVisibility(respectVisibility);
//
//        // ASSERT
//        assertTrue("Should be true.", _factory.getRespectVisibility());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testNullStringsSetRespectVisibilityToTrue() {
//
//        // ARRANGE
//        final String respectVisibility = null;
//
//        // ACT
//        _factory.setRespectVisibility(respectVisibility);
//
//        // ASSERT
//        assertTrue("Should be true.", _factory.getRespectVisibility());
//    }


    /**
     * Test.
     */
    public void testNewInstance() {
//
//        // ARRANGE
//        expect(_registry.get(DATA_MANAGER_LOCAL))
//            .andReturn(Testing.dummy(DataManager.class));
//        expect(_registry.get(SEARCH_ENGINE_LOCAL))
//            .andReturn(Testing.dummy(SearchEngine.class));
//        replay(_registry);
//
//        // ACT
//        _factory.createRenderer(Testing.dummy(StatefulReader.class));
//
//        // ASSERT
//        verify(_registry);
    }


//    /** {@inheritDoc} */
//    @Override
//    protected void setUp() throws Exception {
//        _registry = createStrictMock(Registry.class);
//        _factory = new DefaultObjectFactory(_registry);
//    }
//
//    /** {@inheritDoc} */
//    @Override
//    protected void tearDown() throws Exception {
//        _factory = null;
//        _registry = null;
//    }
//
//    private DefaultObjectFactory _factory;
//    private Registry _registry;
}
