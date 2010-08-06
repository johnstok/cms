/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.core;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;
import ccc.api.types.Range;
import ccc.api.types.SortOrder;


/**
 * Criteria class to search for pages.
 *
 * @author Civic Computing Ltd.
 */
public class PageCriteria
    extends
        ResourceCriteria {

    private Set<Paragraph> _paraMatches = new HashSet<Paragraph>();
    private Map<String, Range<?>> _paraRanges = new HashMap<String, Range<?>>();

    private String _sortField;
    private SortOrder _sortOrder;
    private ParagraphType _sortType;


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final String value) {
        _paraMatches.add(Paragraph.fromText(name, value));
    }


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final boolean value) {
        _paraMatches.add(Paragraph.fromBoolean(name, Boolean.valueOf(value)));
    }


//    /**
//     * Match the specified paragraph.
//     *
//     * @param name The paragraph name.
//     * @param value The paragraph value.
//     */
//    public void matchParagraph(final String name, final long value) {
//        _paraMatches.add(Paragraph.fromNumber(name, value));
//    }
//
//
//    /**
//     * Match the specified paragraph.
//     *
//     * @param name The paragraph name.
//     * @param value The paragraph value.
//     */
//    public void matchParagraph(final String name, final double value) {
//        _paraMatches.add(Paragraph.fromNumber(name, value));
//    }
//
//
//    /**
//     * Match the specified paragraph.
//     *
//     * @param name The paragraph name.
//     * @param value The paragraph value.
//     */
//    public void matchParagraph(final String name, final BigDecimal value) {
//        _paraMatches.add(Paragraph.fromNumber(name, value));
//    }


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final Date value) {
        _paraMatches.add(Paragraph.fromDate(name, value));
    }


    /**
     * Filter pages within a range.
     *
     * @param name The name of the paragraph to filter on.
     * @param from The starting filter value.
     * @param to   The ending filter value.
     */
    public void rangeParagraph(final String name,
                               final Date from,
                               final Date to) {
        _paraRanges.put(name, new Range<Date>(Date.class, from, to));
    }


    /**
     * Filter pages within a range.
     *
     * @param name The name of the paragraph to filter on.
     * @param from The starting filter value.
     * @param to   The ending filter value.
     */
    public void rangeParagraph(final String name,
                               final String from,
                               final String to) {
        _paraRanges.put(name, new Range<String>(String.class, from, to));
    }


//    public void rangeParagraph(final String name,
//                               final Long from,
//                               final Long to) {
//        final BigDecimal fDecimal = (null==from) ? null : new BigDecimal(from);
//        final BigDecimal tDecimal = (null==to)   ? null : new BigDecimal(to);
//        rangeParagraph(name, fDecimal, tDecimal);
//    }
//
//
//    public void rangeParagraph(final String name,
//                               final Double from,
//                               final Double to) {
//        final BigDecimal fDecimal = (null==from) ? null : new BigDecimal(from);
//        final BigDecimal tDecimal = (null==to)   ? null : new BigDecimal(to);
//        rangeParagraph(name, fDecimal, tDecimal);
//    }
//
//
//    public void rangeParagraph(final String name,
//                               final Float from,
//                               final Float to) {
//        final BigDecimal fDecimal = (null==from) ? null : new BigDecimal(from);
//        final BigDecimal tDecimal = (null==to)   ? null : new BigDecimal(to);
//        rangeParagraph(name, fDecimal, tDecimal);
//    }
//
//
//    public void rangeParagraph(final String name,
//                               final Integer from,
//                               final Integer to) {
//        final BigDecimal fDecimal = (null==from) ? null : new BigDecimal(from);
//        final BigDecimal tDecimal = (null==to)   ? null : new BigDecimal(to);
//        rangeParagraph(name, fDecimal, tDecimal);
//    }
//
//
//    public void rangeParagraph(final String name,
//                               final BigDecimal from,
//                               final BigDecimal to) {
//        _paraRanges.put(
//            name, new Range<BigDecimal>(BigDecimal.class, from, to));
//    }


    /**
     * Accessor.
     *
     * @return The paragraphs to match.
     */
    public Set<Paragraph> getParaMatches() {
        return new HashSet<Paragraph>(_paraMatches);
    }


    /**
     * Mutator.
     *
     * @param paragraphs The paragraphs to match.
     */
    public void setParaMatches(final Set<Paragraph> paragraphs) {
        _paraMatches.clear();
        _paraMatches.addAll(paragraphs);
    }


    /**
     * Accessor.
     *
     * @return The paragraph ranges.
     */
    public Map<String, Range<?>> getParaRanges() {
        return new HashMap<String, Range<?>>(_paraRanges);
    }


    /**
     * Mutator.
     *
     * @param paraRanges The paragraph ranges.
     */
    public void setParaRanges(final Map<String, Range<?>> paraRanges) {
        _paraRanges.clear();
        _paraRanges.putAll(paraRanges);
    }


    /**
     * Accessor.
     *
     * @return Returns the sortField.
     */
    public String getParaSortField() {
        return _sortField;
    }


    /**
     * Mutator.
     *
     * @param sortField The sortField to set.
     */
    public void setParaSortField(final String sortField) {
        _sortField = sortField;
    }


    /**
     * Accessor.
     *
     * @return Returns the sortOrder.
     */
    public SortOrder getParaSortOrder() {
        return _sortOrder;
    }


    /**
     * Mutator.
     *
     * @param sortOrder The sortOrder to set.
     */
    public void setParaSortOrder(final SortOrder sortOrder) {
        _sortOrder = sortOrder;
    }


    /**
     * Accessor.
     *
     * @return Returns the sort type.
     */
    public ParagraphType getParaSortType() {
        return _sortType;
    }


    /**
     * Mutator.
     *
     * @param sortType The sort type to set.
     */
    public void setParaSortType(final ParagraphType sortType) {
        _sortType = sortType;
    }


    /**
     * Query.
     *
     * @return Returns true if results should be sorted; false otherwise.
     */
    public boolean isSortedByPara() {
        return
            null!=_sortField
            && _sortField.trim().length()>0
            && null!=_sortOrder
            && _sortType!=null;
    }


    /**
     * Sort the results.
     *
     * @param field The paragraph to sort on.
     * @param type  The type of the paragraph sorted on.
     * @param order The order to sort.
     */
    public final void sort(final String field,
                           final ParagraphType type,
                           final SortOrder order) {
        setParaSortField(field);
        setParaSortOrder(order);
        setParaSortType(type);
    }
}
