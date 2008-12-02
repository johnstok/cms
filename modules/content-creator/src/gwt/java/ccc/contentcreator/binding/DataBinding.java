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

    public static List<ModelData> bindLogEntrySummary(final Collection<LogEntrySummary> arg0) {
        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final LogEntrySummary les : arg0) {
            final ModelData md = new BaseModelData();
            md.set("action", les._action);
            md.set("actor", les._actor);
            md.set("happenedOn", les._happenedOn);
            md.set("summary", les._summary);
            boundData.add(md);
        }
        return boundData;
    }

    public static List<ModelData> bindResourceSummary(final Collection<ResourceSummary> arg0) {
        final List<ModelData> boundData = new ArrayList<ModelData>();
        for (final ResourceSummary fs : arg0) {
            final ModelData md = new BaseModelData();

            md.set("id", fs._id);
            md.set("name", fs._name);
            md.set("published", fs._publishedBy);
            md.set("title", fs._title);
            md.set("locked", fs._lockedBy);
            md.set("type", fs._type);
            md.set("childCount", fs._childCount);
            md.set("folderCount", fs._folderCount);

            boundData.add(md);
        }
        return boundData;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param result
     * @return
     */
    public static List<ModelData> bindUserSummary(final Collection<UserSummary> result) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param _model
     * @param arg0
     */
    public static void merge(final ModelData _model, final ResourceSummary arg0) {
//        _model.set("title", dto.getTitle());
//        // TODO name update is not persisted.
//        _model.set("name", dto.getName());
//        _model.set("description",
//            dto.getDescription());
//        _model.set("body", dto.getBody());
//        _model.set("definition",
//            dto.getDefinition());
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param list
     * @return
     */
    public static List<ModelData> bindTemplateDelta(final Collection<TemplateDelta> list) {

        throw new UnsupportedOperationException("Method not implemented.");
    }
}
