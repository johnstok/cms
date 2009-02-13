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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ccc.services.api.FileSummary;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;


/**
 * A {@link ModelData} implementation for binding a {@link FolderSummary}.
 *
 * @author Civic Computing Ltd.
 */
public class DataBinding {

    /**
     * TODO: Add a description of this method.
     *
     * @param arg0
     * @return
     */
    public static List<ModelData> bindLogEntrySummary(
                                    final Collection<LogEntrySummary> arg0) {
        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final LogEntrySummary les : arg0) {
            final ModelData md = new BaseModelData();
            md.set("action", les._action);
            md.set("actor", les._actor);
            md.set("happenedOn", new Date(les._happenedOn));
            md.set("summary", les._summary);
            boundData.add(md);
        }
        return boundData;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param arg0
     * @return
     */
    public static List<ModelData> bindResourceSummary(
                                    final Collection<ResourceSummary> arg0) {
        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final ResourceSummary fs : arg0) {
            final ModelData md = bindResourceSummary(fs);
            boundData.add(md);
        }
        return boundData;
    }

    public static ModelData bindResourceSummary(final ResourceSummary fs) {
        final ModelData md = new BaseModelData();
        merge(md, fs);
        return md;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param result
     * @return
     */
    public static List<ModelData> bindUserSummary(
                                        final Collection<UserSummary> result) {
        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final UserSummary us : result) {
            final ModelData md = new BaseModelData();
            merge(md, us);
            boundData.add(md);
        }
        return boundData;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param list
     * @return
     */
    public static List<ModelData> bindTemplateDelta(
        final Collection<TemplateDelta> list) {
        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final TemplateDelta td : list) {
            final ModelData md = new BaseModelData();
            merge(td, md);
            boundData.add(md);
        }
        return boundData;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param md
     * @param us
     */
    private static void merge(final ModelData md, final UserSummary us) {
        md.set("id", us._id);
        md.set("email", us._email);
        md.set("username", us._username);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param md
     * @param fs
     */
    public static void merge(final ModelData md, final ResourceSummary fs) {
        md.set("id", fs._id);
        md.set("parentId", fs._parentId);
        md.set("name", fs._name);
        md.set("published", fs._publishedBy);
        md.set("title", fs._title);
        md.set("locked", fs._lockedBy);
        md.set("type", fs._type);
        md.set("childCount", fs._childCount);
        md.set("folderCount", fs._folderCount);
        md.set("mmInclude", fs._includeInMainMenu);
        md.set("sortOrder", fs._sortOrder);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param td
     * @param md
     */
    public static void merge(final TemplateDelta td, final ModelData md) {
        md.set("id", td._id);
        md.set("name", td._name);
        md.set("title", td._title);
        md.set("description", td._description);
        md.set("body", td._body);
        md.set("definition", td._definition);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param arg0
     * @return
     */
    public static List<ModelData> bindFileSummary(final Collection<FileSummary> arg0) {

        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final FileSummary fs : arg0) {
            final ModelData md = new BaseModelData();
            merge(md, fs);
            md.set("mimeType", fs._mimeType);
            md.set("path", fs._path);
            boundData.add(md);
        }
        return boundData;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param data
     * @return
     */
    public static List<ModelData> bindMetadata(
        final Collection<Entry<String, String>> data) {

        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final Map.Entry<String, String> datum : data) {
            final ModelData md = new BaseModelData();
            md.set("key", datum.getKey());
            md.set("value", datum.getValue());
            boundData.add(md);
        }
        return boundData;
    }
}
