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
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;


/**
 * TODO: Add Description for this type.
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

    void extractURLs(final Map<String, StringBuffer> map) {

        for (final Map.Entry<String, StringBuffer> para : map.entrySet()) {
            try {
                final HtmlCleaner cleaner  = new HtmlCleaner();
                cleaner.getProperties().setOmitHtmlEnvelope(true);
                final TagNode html = cleaner.clean(para.getValue().toString());
                final Object[] l = html.evaluateXPath("//a");

                for (final Object element : l) {
                    final TagNode anchor = (TagNode) element;
                    if (anchor.hasAttribute("href")) {
                        final String link = anchor.getAttributeByName("href");

                        // Ignore links that don't need correction
                        if (link.startsWith("mailto:")) { // email address
                            continue;
                        } else if (link.startsWith("http://")) { // absolute URLs
                            continue;
                        } else if (link.startsWith("/")) { // absolute URLs
                            continue;
                        } else if (link.startsWith("#")) { // link on the same page
                            continue;
                        }

                        correct(anchor, link);
                    }
                }

                para.setValue(
                    new StringBuffer(
                        new PrettyXmlSerializer(cleaner.getProperties())
                            .getXmlAsString(html)
                    )
                );

            } catch (final Exception e) {
                log.error("HTML parsing failed:\r\n"+para.getValue(), e);
            }
        }
    }

    void correct(final TagNode anchor, final String link) {
        final Matcher pm = PAGE_PATTERN.matcher(link);
        final Matcher opm = OLDPAGE_PATTERN.matcher(link);

        if (pm.matches()) {
            final String corrected = "/ash/"+pm.group(1)+"html";
            anchor.addAttribute("href", corrected);
            log.info("Corrected "+link+" to "+corrected);
        } else if (opm.matches()) {
            final String corrected = "/ash/"+opm.group(1)+".html";
            anchor.addAttribute("href", corrected);
            log.info("Corrected "+link+" to "+corrected);
        } else if (FILE_PATTERN.matcher(link).matches()) {
            final String corrected = "/ash/"+link;
            anchor.addAttribute("href", corrected);
            log.info("Corrected "+link+" to "+corrected);
        } else {
            links.add(link);
        }
    }

    /**
     * TODO: Add a description of this method.
     *
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
