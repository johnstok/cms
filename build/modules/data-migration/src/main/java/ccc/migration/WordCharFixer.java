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
package ccc.migration;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class WordCharFixer {
    private static final Logger  LOG = Logger.getLogger(WordCharFixer.class);
    private static final Pattern CP_1252_BAD =
        Pattern.compile("[\\x7F-\\x9F]{1}");

    /**
     * TODO: Add a description of this method.
     *
     * @param map
     */
    public void warn(final Map<String, StringBuffer> map) {
        for (final Map.Entry<String, StringBuffer> para : map.entrySet()) {
            final StringBuffer uncorrectedPara = para.getValue();
            final StringBuffer correctedPara = correct(uncorrectedPara);
            para.setValue(correctedPara);
        }
    }

    /**
     * Performs character correction for char's \\x7F-\\x9F based on their
     * corresponding values in the CP-1252 character set.
     *
     * @param buffer The string buffer to correct.
     * @return The corrected string buffer.
     */
    StringBuffer correct(final StringBuffer buffer) {

        final StringBuffer correctedPara = new StringBuffer();

        final Matcher hrefMatcher = CP_1252_BAD.matcher(buffer);

        while (hrefMatcher.find()) { // search for bad characters
            final String badString = hrefMatcher.group();
            final char badChar = badString.charAt(0);
            switch (badChar) {
                case '\u0080':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u20ac");
                    break;
                case '\u0082':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u201a");
                    break;
                case '\u0083':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u0192");
                    break;
                case '\u0084':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u201e");
                    break;
                case '\u0085':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2026");
                    break;
                case '\u0086':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2020");
                    break;
                case '\u0087':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2021");
                    break;
                case '\u0088':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u02c6");
                    break;
                case '\u0089':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2030");
                    break;
                case '\u008a':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u0160");
                    break;
                case '\u008b':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2039");
                    break;
                case '\u008c':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u0152");
                    break;
                case '\u008e':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u017d");
                    break;
                case '\u0091':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2018");
                    break;
                case '\u0092':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2019");
                    break;
                case '\u0093':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u201c");
                    break;
                case '\u0094':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u201d");
                    break;
                case '\u0095':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2022");
                    break;
                case '\u0096':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2013");
                    break;
                case '\u0097':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2014");
                    break;
                case '\u0098':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u02dc");
                    break;
                case '\u0099':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u2122");
                    break;
                case '\u009a':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u0161");
                    break;
                case '\u009b':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u203a");
                    break;
                case '\u009c':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u0153");
                    break;
                case '\u009e':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u017e");
                    break;
                case '\u009f':
                    LOG.warn("Replaced bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "\u0178");
                    break;
                default:
                    LOG.warn("Removing bad char: "+badChar);
                    hrefMatcher.appendReplacement(correctedPara, "");
                    break;

            }
        }
        hrefMatcher.appendTail(correctedPara);
        return correctedPara;
    }
}
