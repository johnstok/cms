/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import java.util.Arrays;
import java.util.HashSet;

import ccc.commons.WordCharFixer;
import ccc.rest.dto.PageDelta;
import ccc.types.DBC;
import ccc.types.Paragraph;
import ccc.types.ParagraphType;
import ccc.types.ResourceName;
import ccc.types.ResourceType;


/**
 * A page resource.
 *
 * @author Civic Computing Ltd.
 */
public class Page
    extends
        WorkingCopySupport<PageRevision, PageDelta, PageWorkingCopy> {

    /** MAXIMUM_PARAGRAPHS : int. */
    public static final int MAXIMUM_PARAGRAPHS = 32;


    /** Constructor: for persistence only. */
    protected Page() { super(); }


    /**
     * Constructor.
     *
     * @param title The title for the resource.
     * @param metadata The metadata for the revision.
     */
    Page(final String title, final RevisionMetadata metadata) {
        super(title);
        update(new PageDelta(new HashSet<Paragraph>()), metadata);
    }


    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     * @param template The page's template.
     * @param metadata The metadata for the revision.
     * @param paragraphs The page's paragraphs.
     */
    public Page(final ResourceName name,
                final String title,
                final Template template,
                final RevisionMetadata metadata,
                final Paragraph... paragraphs) {
        super(name, title);
        setTemplate(template);
        update(
            new PageDelta(
                new HashSet<Paragraph>(Arrays.asList(paragraphs))),
                metadata);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType getType() {
        return ResourceType.PAGE;
    }


    private void validateParagraphs(final HashSet<Paragraph> paras) {
        final Template template = computeTemplate(null);
        if (null != template) {
            final PageHelper pageHelper = new PageHelper();
            pageHelper.validateFieldsForPage(paras, template.getDefinition());
        }
    }


    private HashSet<Paragraph> cleanParagraphs(final PageDelta delta) {
        final HashSet<Paragraph> paras = new HashSet<Paragraph>();
        for (final Paragraph para : delta.getParagraphs()) {
            if (ParagraphType.TEXT == para.type()) {
                final WordCharFixer fixer = new WordCharFixer();
                final Paragraph p =
                    Paragraph.fromText(para.name(), fixer.fix(para.text()));
                paras.add(p);
            } else {
                paras.add(para);
            }
        }
        return paras;
    }





    /* ====================================================================
     * Working copy implementation.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public PageDelta createSnapshot() {
        return new PageDelta(
            new HashSet<Paragraph>(currentRevision().getParagraphs()));
    }


    /** {@inheritDoc} */
    @Override
    protected PageWorkingCopy createWorkingCopy(final PageDelta delta) {
        return new PageWorkingCopy(delta);
    }


    /** {@inheritDoc} */
    @Override
    protected void update(final PageDelta delta,
                          final RevisionMetadata metadata) {
        DBC.require().maxValue(
            delta.getParagraphs().size(),
            MAXIMUM_PARAGRAPHS);

        final HashSet<Paragraph> paras = cleanParagraphs(delta);

        validateParagraphs(paras);

        addRevision(
            new PageRevision(
                metadata.getTimestamp(),
                metadata.getActor(),
                metadata.isMajorChange(),
                metadata.getComment(),
                paras));
    }




    /* ====================================================================
     * Snapshot support.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public final PageDelta forWorkingCopy() {
        final PageDelta dto =
            new PageDelta(
                new HashSet<Paragraph>(getWorkingCopy().getParagraphs()));
        setDtoProps(dto);
        dto.setRevision(-1);
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public final PageDelta forCurrentRevision() {
        final PageDelta dto =
            new PageDelta(
                new HashSet<Paragraph>(currentRevision().getParagraphs()));
        setDtoProps(dto);
        dto.setRevision(currentRevisionNo());
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public final PageDelta forSpecificRevision(final int revNo) {
        final PageDelta dto =
            new PageDelta(
                new HashSet<Paragraph>(revision(revNo).getParagraphs()));
        setDtoProps(dto);
        dto.setRevision(revNo);
        return dto;
    }
}
