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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ccc.types.DBC;


/**
 * Corrects relative URLs stored in href attributes.
 *
 * @author Civic Computing Ltd.
 */
public class LinkFixer {
    private static final Logger LOG = Logger.getLogger(LinkFixer.class);
    private static List<String> uncorrectedLinks = new ArrayList<String>();
    private static final Pattern OLDPAGE_PATTERN =
        Pattern.compile(".*pContentID=(\\d+).*");
    private static final Pattern PAGE_PATTERN =
        Pattern.compile("(\\d+\\.)(\\d+\\.)*html");
    private static final Pattern FILE_PATTERN =
        Pattern.compile("(files|images)/.+");
    /** HREF_PATTERN : Pattern. */
    static final Pattern HREF_PATTERN =
        Pattern.compile("href\\s*=\\s*\"(.*?)\"");
    /** SRC_PATTERN : Pattern. */
    static final Pattern SRC_PATTERN =
        Pattern.compile("src\\s*=\\s*\"(.*?)\"");
    private final String _prefix;
    private final String _sourceRef;

    /**
     * Constructor.
     *
     * @param prefix The prefix used when correcting links.
     */
    public LinkFixer(final String prefix, final String sourceRef) {
        DBC.require().notNull(prefix);
        DBC.require().notNull(sourceRef);
        _prefix = prefix;
        _sourceRef = sourceRef;
    }

    /**
     * Correct all the links in a collection of paragraphs.
     *
     * @param map The paragraphs to correct.
     */
    void extractURLs(final Map<String, StringBuffer> map) {

        for (final Map.Entry<String, StringBuffer> para : map.entrySet()) {
            StringBuffer correctedPara =
                correct(HREF_PATTERN.matcher(para.getValue()), "href");
            correctedPara =
                correct(SRC_PATTERN.matcher(correctedPara), "src");
            para.setValue(correctedPara);
        }
    }

    private StringBuffer correct(final Matcher hrefMatcher,
                                 final String attName) {

        final StringBuffer correctedPara = new StringBuffer();
        while (hrefMatcher.find()) { // search for href attributes
            final String url = hrefMatcher.group(1);

            if (url.startsWith("mailto:")     // net URL
                || url.startsWith("http://")  // net URL
                || url.startsWith("/")        // Absolute URL
                || url.startsWith("#")) {     // link on the same page
                hrefMatcher.appendReplacement(// no correction
                    correctedPara,
                    Matcher.quoteReplacement(hrefMatcher.group()));

            } else {
                final String correctedHref = attName+"=\""+correct(url)+"\"";
                hrefMatcher.appendReplacement(
                    correctedPara,
                    Matcher.quoteReplacement(correctedHref));

            }
        }
        hrefMatcher.appendTail(correctedPara);
        return correctedPara;
    }

    /**
     * Correct a single link.
     *
     * @param link The link to correct.
     * @return The corrected link.
     */
    String correct(final String link) {
        final Matcher pm = PAGE_PATTERN.matcher(link);
        final Matcher opm = OLDPAGE_PATTERN.matcher(link);

        String corrected = link;

        if (pm.matches()) {
            corrected = _prefix+pm.group(1)+"html";
            LOG.debug("Corrected "+link+" to "+corrected);

        } else if (opm.matches()) {
            corrected = _prefix+opm.group(1)+".html";
            LOG.debug("Corrected "+link+" to "+corrected);

        } else if (FILE_PATTERN.matcher(link).matches()) {
            corrected = _prefix+link;
            LOG.debug("Corrected "+link+" to "+corrected);

        } else {
            uncorrectedLinks.add(link);
            LOG.info("Source: "+_sourceRef+", didn't correct "+link);

        }
        return corrected;
    }

    /**
     * Write uncorrected links to a file.
     */
    public static void writeLinks() {
        try {
            final File f = new File("links.txt");
            final PrintWriter pw =
                new PrintWriter(f, "UTF-8");
            Collections.sort(uncorrectedLinks);
            for (final String link : uncorrectedLinks) {
                pw.println(link);
            }
            pw.close();
        } catch (final FileNotFoundException e) {
            LOG.error("Error writing links.", e);
        } catch (final UnsupportedEncodingException e) {
            LOG.error("Error writing links.", e);
        }
    }
}
