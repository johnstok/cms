/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ccc.services.api.ActionSummary;
import ccc.services.api.FileSummary;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateSummary;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;


/**
 * A {@link ModelData} implementation for binding a {@link FolderSummary}.
 *
 * @author Civic Computing Ltd.
 */
public final class DataBinding {

    /** VALUE : String. */
    public static final String VALUE = "value";
    /** KEY : String. */
    public static final String KEY = "key";


    private DataBinding() { super(); }


    /**
     * Convert a collection of log entry summaries to model data.
     *
     * @param arg0 The summaries
     * @return The model data.
     */
    public static List<LogEntrySummaryModelData> bindLogEntrySummary(
                                       final Collection<LogEntrySummary> arg0) {
        final List<LogEntrySummaryModelData> boundData =
            new ArrayList<LogEntrySummaryModelData>();
        for (final LogEntrySummary les : arg0) {
            boundData.add(new LogEntrySummaryModelData(les));
        }
        return boundData;
    }

    public static List<ResourceSummaryModelData> bindResourceSummary(
                                       final Collection<ResourceSummary> arg0) {
        final List<ResourceSummaryModelData> boundData =
            new ArrayList<ResourceSummaryModelData>();
        for (final ResourceSummary fs : arg0) {
            final ResourceSummaryModelData md = bindResourceSummary(fs);
            boundData.add(md);
        }
        return boundData;
    }

    public static ResourceSummaryModelData bindResourceSummary(
                                                     final ResourceSummary rs) {
        final ResourceSummaryModelData md = new ResourceSummaryModelData(rs);
        return md;
    }


    public static List<UserSummaryModelData> bindUserSummary(
                                         final Collection<UserSummary> result) {
        final List<UserSummaryModelData> boundData =
            new ArrayList<UserSummaryModelData>();
        for (final UserSummary us : result) {
            boundData.add(new UserSummaryModelData(us));
        }
        return boundData;
    }


    public static List<TemplateSummaryModelData> bindTemplateDelta(
                                         final Collection<TemplateSummary> list) {
        final List<TemplateSummaryModelData> boundData =
            new ArrayList<TemplateSummaryModelData>();
        for (final TemplateSummary ts : list) {
            boundData.add(new TemplateSummaryModelData(ts));
        }
        return boundData;
    }


    public static List<FileSummaryModelData> bindFileSummary(
                                           final Collection<FileSummary> arg0) {
        final List<FileSummaryModelData> boundData =
            new ArrayList<FileSummaryModelData>();
        for (final FileSummary fs : arg0) {
            boundData.add(new FileSummaryModelData(fs));
        }
        return boundData;
    }


    public static List<ModelData> bindMetadata(
        final Collection<Entry<String, String>> data) {

        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final Map.Entry<String, String> datum : data) {
            final ModelData md = new BaseModelData();
            md.set(KEY, datum.getKey());
            md.set(VALUE, datum.getValue());
            boundData.add(md);
        }
        return boundData;
    }


    public static List<ActionSummaryModelData> bindActionSummary(
                                      final Collection<ActionSummary> result) {
        final List<ActionSummaryModelData> boundData =
            new ArrayList<ActionSummaryModelData>();
        for (final ActionSummary as : result) {
            boundData.add(new ActionSummaryModelData(as));
        }
        return boundData;
    }
}
