/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 1219 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2009-05-12 16:29:05 +0100 (Tue, 12 May 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;


/**
 * Tests for the {@link WordCharFixer}.
 *
 * @author Civic Computing Ltd.
 */
public class WordCharFixerTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCorrectMap() {

        // ARRANGE
        final StringBuffer bad =
            new StringBuffer("before\u007fmiddle\u008fend");
        final Map<String, StringBuffer> badMap =
            new HashMap<String, StringBuffer>();
        badMap.put("foo", bad);

        // ACT
        new WordCharFixer().warn(badMap);

        // ASSERT
        assertEquals("beforemiddleend", badMap.get("foo").toString());

    }

    /**
     * Test.
     */
    public void testStripBadChars() {

        // ARRANGE
        final StringBuffer bad =
            new StringBuffer("before\u007fmiddle\u008fend");

        // ACT
        final StringBuffer good = new WordCharFixer().correct(bad);

        // ASSERT
        assertEquals("beforemiddleend", good.toString());
    }

    /**
     * Test.
     */
    public void testReplaceBadChars() {

        // ARRANGE
        final StringBuffer bad =
            new StringBuffer("before\u0096middle\u0092end\u0086");

        // ACT
        final StringBuffer good = new WordCharFixer().correct(bad);

        // ASSERT
        assertEquals("before–middle’end†", good.toString());
    }

    /**
     * Test.
     */
    public void testReplacementsAreCorrect() {

        // ARRANGE
        final StringBuffer bad =
            new StringBuffer()
                .append('\u007f')
                .append('\u0080')
                .append('\u0081')
                .append('\u0082')
                .append('\u0083')
                .append('\u0084')
                .append('\u0085')
                .append('\u0086')
                .append('\u0087')
                .append('\u0088')
                .append('\u0089')
                .append('\u008a')
                .append('\u008b')
                .append('\u008c')
                .append('\u008d')
                .append('\u008e')
                .append('\u008f')
                .append('\u0090')
                .append('\u0091')
                .append('\u0092')
                .append('\u0093')
                .append('\u0094')
                .append('\u0095')
                .append('\u0096')
                .append('\u0097')
                .append('\u0098')
                .append('\u0099')
                .append('\u009a')
                .append('\u009b')
                .append('\u009c')
                .append('\u008d')
                .append('\u009e')
                .append('\u009f');

        // ACT
        final StringBuffer good = new WordCharFixer().correct(bad);

        // ASSERT
        assertEquals("€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘’“”•–—˜™š›œžŸ", good.toString());
    }


}
