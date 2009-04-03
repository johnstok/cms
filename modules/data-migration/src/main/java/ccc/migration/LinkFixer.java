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


/**
 * Corrects relative URLs stored in href attributes.
 *
 * @author Civic Computing Ltd.
 */
public class LinkFixer {
    private static Logger log = Logger.getLogger(LinkFixer.class);
    private static List<String> links = new ArrayList<String>();
    private static final Pattern OLDPAGE_PATTERN =
        Pattern.compile(".*pContentID=(\\d+).*");
    private static final Pattern PAGE_PATTERN =
        Pattern.compile("(\\d+\\.)(\\d+\\.)*html");
    private static final Pattern FILE_PATTERN =
        Pattern.compile("files/.+");
    static final Pattern HREF_PATTERN =
        Pattern.compile("href\\s*=\\s*\"(.*?)\"");
    private final String _prefix;

    /**
     * Constructor.
     *
     * @param prefix The prefix used when correcting links.
     */
    public LinkFixer(final String prefix) {
        _prefix = prefix;
        // TODO: Warn if prefix is a ZLS.
    }

    void extractURLs(final Map<String, StringBuffer> map) {

        for (final Map.Entry<String, StringBuffer> para : map.entrySet()) {
            final StringBuffer correctedPara = new StringBuffer();
            final Matcher hrefMatcher = HREF_PATTERN.matcher(para.getValue());

            while (hrefMatcher.find()) { // search for href attributes
                final String url = hrefMatcher.group(1);

                if (url.startsWith("mailto:")    // email address
                    || url.startsWith("http://") // absolute URL
                    || url.startsWith("/")       // absolute URL
                    ||url.startsWith("#")) {     // link on the same page
                    hrefMatcher.appendReplacement(
                        correctedPara, hrefMatcher.group()); // no correction

                } else {
                    final String correctedHref = "href=\""+correct(url)+"\"";
                    hrefMatcher.appendReplacement(correctedPara, correctedHref);

                }
            }
            hrefMatcher.appendTail(correctedPara);
            para.setValue(correctedPara);
        }
    }

    String correct(final String link) {
        final Matcher pm = PAGE_PATTERN.matcher(link);
        final Matcher opm = OLDPAGE_PATTERN.matcher(link);

        String corrected = link;

        if (pm.matches()) {
            corrected = _prefix+pm.group(1)+"html";
            log.info("Corrected "+link+" to "+corrected);

        } else if (opm.matches()) {
            corrected = _prefix+opm.group(1)+".html";
            log.info("Corrected "+link+" to "+corrected);

        } else if (FILE_PATTERN.matcher(link).matches()) {
            corrected = _prefix+link;
            log.info("Corrected "+link+" to "+corrected);

        } else {
            links.add(link);
            log.info("Didn't correct "+link);

        }
        return corrected;
    }

    /**
     * TODO: Add a description of this method.
     */
    public static void writeLinks() {
        try {
            final File f = new File("links.txt");
            final PrintWriter pw =
                new PrintWriter(f, "UTF-8");
            Collections.sort(links);
            for (final String link : links) {
                pw.println(link);
            }
            pw.close();
        } catch (final FileNotFoundException e) {
            log.error("Error writing links.", e);
        } catch (final UnsupportedEncodingException e) {
            log.error("Error writing links.", e);
        }
    }
}
