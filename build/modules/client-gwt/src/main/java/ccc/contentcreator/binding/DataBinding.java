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

import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.dto.UserDto;

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

    private static final IGlobals GLOBALS = new IGlobalsImpl();


    private DataBinding() { super(); }


    /**
     * Convert a collection of log entry summaries to model data.
     *
     * @param arg0 The summaries
     * @return The model data.
     */
    public static List<LogEntrySummaryModelData> bindLogEntrySummary(
                                       final Collection<RevisionDto> arg0) {
        final List<LogEntrySummaryModelData> boundData =
            new ArrayList<LogEntrySummaryModelData>();
        for (final RevisionDto les : arg0) {
            boundData.add(new LogEntrySummaryModelData(les, GLOBALS));
        }
        return boundData;
    }


    /**
     * Create model data objects for a collection of resource summaries.
     *
     * @param arg0 The resource summaries.
     * @return The corresponding model data objects.
     */
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


    /**
     * Create a model data object from a resource summary.
     *
     * @param rs The resource summary.
     * @return The corresponding model data object.
     */
    public static ResourceSummaryModelData bindResourceSummary(
                                                     final ResourceSummary rs) {
        final ResourceSummaryModelData md = new ResourceSummaryModelData(rs);
        return md;
    }


    /**
     * Create model data objects for a collection of user summaries.
     *
     * @param result The user summaries.
     * @return The corresponding model data objects.
     */
    public static List<UserSummaryModelData> bindUserSummary(
                                         final Collection<UserDto> result) {
        final List<UserSummaryModelData> boundData =
            new ArrayList<UserSummaryModelData>();
        for (final UserDto us : result) {
            boundData.add(new UserSummaryModelData(us));
        }
        return boundData;
    }


    /**
     * Create model data objects for a collection of template summaries.
     *
     * @param list The template summaries.
     * @return The corresponding model data objects.
     */
    public static List<TemplateSummaryModelData> bindTemplateDelta(
                                     final Collection<TemplateSummary> list) {
        final List<TemplateSummaryModelData> boundData =
            new ArrayList<TemplateSummaryModelData>();
        for (final TemplateSummary ts : list) {
            boundData.add(new TemplateSummaryModelData(ts));
        }
        return boundData;
    }


    /**
     * Create model data objects for a collection of file DTOs.
     *
     * @param arg0 The file DTOs.
     * @param maxDimension The maximum dimension for thumb nails.
     * @return The corresponding model data objects.
     */
    public static List<ImageSummaryModelData> bindFileSummary(
                                           final Collection<FileDto> arg0,
                                           final int maxDimension) {
        final List<ImageSummaryModelData> boundData =
            new ArrayList<ImageSummaryModelData>();
        for (final FileDto fs : arg0) {
            boundData.add(new ImageSummaryModelData(fs, maxDimension));
        }
        return boundData;
    }


    /**
     * Create model data objects for a collection of metadata.
     *
     * @param data The metadata.
     * @return The corresponding model data objects.
     */
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


    /**
     * Create model data objects for a collection of action summaries.
     *
     * @param result The action summaries.
     * @return The corresponding model data objects.
     */
    public static List<ActionSummaryModelData> bindActionSummary(
                                      final Collection<ActionSummary> result) {
        final List<ActionSummaryModelData> boundData =
            new ArrayList<ActionSummaryModelData>();
        for (final ActionSummary as : result) {
            boundData.add(new ActionSummaryModelData(as, GLOBALS));
        }
        return boundData;
    }
}
