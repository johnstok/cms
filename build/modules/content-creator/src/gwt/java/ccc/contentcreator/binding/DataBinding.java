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

    /** SUMMARY : String. */
    public static final String COMMENT = "comment";
    /** HAPPENED_ON : String. */
    public static final String HAPPENED_ON = "happenedOn";
    /** ACTOR : String. */
    public static final String ACTOR = "actor";
    /** ACTION : String. */
    public static final String ACTION = "action";
    /** PATH : String. */
    public static final String PATH = "path";
    /** MIME_TYPE : String. */
    public static final String MIME_TYPE = "mimeType";
    /** DEFINITION : String. */
    public static final String DEFINITION = "definition";
    /** BODY : String. */
    public static final String BODY = "body";
    /** DESCRIPTION : String. */
    public static final String DESCRIPTION = "description";
    /** USERNAME : String. */
    public static final String USERNAME = "username";
    /** EMAIL : String. */
    public static final String EMAIL = "email";
    /** SORT_ORDER : String. */
    public static final String SORT_ORDER = "sortOrder";
    /** CHILD_COUNT : String. */
    public static final String CHILD_COUNT = "childCount";
    /** TYPE : String. */
    public static final String TYPE = "type";
    /** LOCKED : String. */
    public static final String LOCKED = "locked";
    /** TITLE : String. */
    public static final String TITLE = "title";
    /** PUBLISHED : String. */
    public static final String PUBLISHED = "published";
    /** NAME : String. */
    public static final String NAME = "name";
    /** PARENT_ID : String. */
    public static final String PARENT_ID = "parentId";
    /** ID : String. */
    public static final String ID = "id";
    /** FOLDER_COUNT : String. */
    public static final String FOLDER_COUNT = "folderCount";
    /** MM_INCLUDE : String. */
    public static final String MM_INCLUDE = "mmInclude";
    /** WORKING_COPY : String. */
    public static final String WORKING_COPY = "workingCopy";
    /** IS_MAJOR_EDIT : String. */
    public static final String IS_MAJOR_EDIT = "isMajorEdit";

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
            md.set(ACTION, les._action);
            md.set(ACTOR, les._actor);
            md.set(HAPPENED_ON, new Date(les._happenedOn));
            md.set(COMMENT, les._comment);
            md.set(IS_MAJOR_EDIT, les._isMajorEdit);
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
        md.set(ID, us._id);
        md.set(EMAIL, us._email);
        md.set(USERNAME, us._username);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param md
     * @param fs
     */
    public static void merge(final ModelData md, final ResourceSummary fs) {
        md.set(ID, fs._id);
        md.set(PARENT_ID, fs._parentId);
        md.set(NAME, fs._name);
        md.set(PUBLISHED, fs._publishedBy);
        md.set(TITLE, fs._title);
        md.set(LOCKED, fs._lockedBy);
        md.set(TYPE, fs._type);
        md.set(CHILD_COUNT, fs._childCount);
        md.set(FOLDER_COUNT, fs._folderCount);
        md.set(MM_INCLUDE, fs._includeInMainMenu);
        md.set(SORT_ORDER, fs._sortOrder);
        md.set(WORKING_COPY, fs._hasWorkingCopy);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param td
     * @param md
     */
    public static void merge(final TemplateDelta td, final ModelData md) {
        md.set(ID, td._id);
        md.set(NAME, td._name);
        md.set(TITLE, td._title);
        md.set(DESCRIPTION, td._description);
        md.set(BODY, td._body);
        md.set(DEFINITION, td._definition);
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
            md.set(MIME_TYPE, fs._mimeType);
            md.set(PATH, fs._path);
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
