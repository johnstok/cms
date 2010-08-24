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
package ccc.plugins.s11n.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccc.api.core.PageCriteria;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;
import ccc.api.types.Range;
import ccc.api.types.SortOrder;


/**
 * Serializer for {@link Resource}s.
 *
 * @author Civic Computing Ltd.
 */
class PageCriteriaSerializer
    extends
        ResourceCriteriaSerializer<PageCriteria> {


    /** {@inheritDoc} */
    @Override
    public PageCriteria read(final Json json) {
        if (null==json) { return null; }

        final PageCriteria p = super.read(json);

        final Map<String, Range<?>> ranges = new HashMap<String, Range<?>>();
        final Collection<Json> jsonRanges = json.getCollection("para-ranges");
        for (final Json jsonRange : jsonRanges) {
            final String name = jsonRange.getString("name");
            final String className = jsonRange.getString("class");
            if (Date.class.getName().equals(className)) {
                ranges.put(
                    name,
                    new Range<Date>(
                        Date.class,
                        jsonRange.getDate("start"),
                        jsonRange.getDate("end")));
            } else if (String.class.getName().equals(className)) {
                ranges.put(
                    name,
                    new Range<String>(
                        String.class,
                        jsonRange.getString("start"),
                        jsonRange.getString("end")));
//            } else if (BigDecimal.class.getName().equals(className)) {
//                ranges.put(
//                    name,
//                    new Range<BigDecimal>(
//                        BigDecimal.class,
//                        jsonRange.getBigDecimal("start"),
//                        jsonRange.getBigDecimal("end")));
            } else {
                throw new RuntimeException(
                    "Unsupported range type: "+className);
            }
        }
        p.setParaRanges(ranges);

        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        for (final Json jsonPara : json.getCollection(JsonKeys.PARAGRAPHS)) {
            paragraphs.add(new ParagraphSerializer().read(jsonPara));
        }
        p.setParaMatches(paragraphs);

        p.setParaSortField(json.getString("para-sort-field"));

        final String pSortOrder = json.getString("para-sort-order");
        p.setParaSortOrder(
            (null==pSortOrder) ? null : SortOrder.valueOf(pSortOrder));

        final String pSortType = json.getString("para-sort-type");
        p.setParaSortType(
            (null==pSortType) ? null : ParagraphType.valueOf(pSortType));

        return p;
    }


    /** {@inheritDoc} */
    @Override protected PageCriteria createObject() {
        return new PageCriteria();
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final PageCriteria instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        final List<Json> ranges = new ArrayList<Json>();

        for (final Map.Entry<String, Range<?>> i
                                        : instance.getParaRanges().entrySet()) {
            final Json range = json.create();
            if (Date.class.equals(i.getValue().getType())) {
                final Range<Date> j = (Range<Date>) i.getValue();
                range.set("start", j.getStart());
                range.set("end", j.getEnd());
            } else if (String.class.equals(i.getValue().getType())) {
                final Range<String> j = (Range<String>) i.getValue();
                range.set("start", j.getStart());
                range.set("end", j.getEnd());
//            } else if (BigDecimal.class.equals(i.getValue().getType())) {
//                final Range<BigDecimal> j = (Range<BigDecimal>) i.getValue();
//                range.set("start", j.getStart());
//                range.set("end", j.getEnd());
            } else {
                throw new RuntimeException(
                    "Unsupported range type: "+i.getValue().getType());
            }
            range.set("class", i.getValue().getType().getName());
            range.set("name", i.getKey());
            ranges.add(range);
        }
        json.setJsons("para-ranges", ranges);

        json.setJsons(
            JsonKeys.PARAGRAPHS,
            new ParagraphSerializer().write(json, instance.getParaMatches()));

        json.set("para-sort-field", instance.getParaSortField());

        final SortOrder o = instance.getParaSortOrder();
        json.set("para-sort-order", (null==o) ? null : o.name());

        final ParagraphType t = instance.getParaSortType();
        json.set("para-sort-type", (null==t) ? null : t.name());

        return json;
    }
}
