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
package ccc.client.gwt.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ccc.api.core.ActionSummary;
import ccc.api.core.Comment;
import ccc.api.core.File;
import ccc.api.core.Group;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.client.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.BeanModelMarker;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.BeanModelMarker.BEAN;


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

    private static final Globals GLOBALS = new GlobalsImpl();


    private DataBinding() { super(); }


    /**
     * Convert a collection of log entry summaries to model data.
     *
     * @param revisions The revisions.
     * @return The model data.
     */
    public static List<BeanModel> bindLogEntrySummary(
                                       final Collection<Revision> revisions) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(Revision.class).createModel(revisions);
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
     * Create model data object for a user.
     *
     * @param user The user to bind.
     * @return The corresponding bean model.
     */
    public static BeanModel bindUserSummary(final User user) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(User.class).createModel(user);
    }


    /**
     * Create model data objects for a collection of user summaries.
     *
     * @param result The user summaries.
     * @return The corresponding model data objects.
     */
    public static List<BeanModel> bindUserSummary(
                                         final Collection<User> result) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(User.class).createModel(result);
    }


    /**
     * Create model data objects for a collection of template summaries.
     *
     * @param list The template summaries.
     *
     * @return The corresponding model data objects.
     */
    public static List<BeanModel> bindTemplateDelta(
                                     final Collection<Template> list) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(Template.class).createModel(list);
    }


    /**
     * Create a model data object for a single template.
     *
     * @param template The template to bind.
     *
     * @return The bean model wrapper for the template.
     */
    public static BeanModel bindTemplate(final Template template) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(Template.class).createModel(template);
    }


    /**
     * Create model data objects for a collection of file DTOs.
     *
     * @param arg0 The file DTOs.
     * @return The corresponding model data objects.
     */
    public static List<ImageSummaryModelData> bindFileSummary(
                                           final Collection<File> arg0) {
        final List<ImageSummaryModelData> boundData =
            new ArrayList<ImageSummaryModelData>();
        for (final File fs : arg0) {
            boundData.add(new ImageSummaryModelData(fs));
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
     * @param actions The action summaries.
     * @return The corresponding model data objects.
     */
    public static List<ActionSummaryModelData> bindActionSummary(
                                      final Collection<ActionSummary> actions) {
        final List<ActionSummaryModelData> boundData =
            new ArrayList<ActionSummaryModelData>();
        for (final ActionSummary as : actions) {
            boundData.add(new ActionSummaryModelData(as, GLOBALS));
        }
        return boundData;
    }


    /**
     * Bind a collection of comments.
     *
     * @param comments The comments to bind.
     *
     * @return The corresponding gxt models.
     */
    public static List<BeanModel> bindCommentSummary(
                                        final Collection<Comment> comments) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(Comment.class).createModel(comments);
    }


    /**
     * Bind a single comment.
     *
     * @param comment The comment to bind.
     *
     * @return The corresponding gxt model.
     */
    public static BeanModel bindCommentSummary(final Comment comment) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(Comment.class).createModel(comment);
    }


    /**
     * Bind a single group.
     *
     * @param group The group to bind.
     *
     * @return The corresponding gxt model.
     */
    public static BeanModel bindGroupSummary(final Group group) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(Group.class).createModel(group);
    }


    /**
     * Bind a collection of groups.
     *
     * @param groups The groups to bind.
     *
     * @return The corresponding gxt models.
     */
    public static List<BeanModel> bindGroupSummary(
                                            final Collection<Group> groups) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(Group.class).createModel(groups);
    }


    /**
     * GXT model for a template.
     */
    @BEAN(Template.class)
    public interface TemplateBeanModel extends BeanModelMarker {
        /** NAME : String. */
        String NAME = "name";
    }


    /**
     * GXT model for a user.
     */
    @BEAN(User.class)
    public interface UserBeanModel extends BeanModelMarker {
        /** EMAIL : String. */
        String EMAIL = "email";
        /** USERNAME : String. */
        String USERNAME = "username";
    }


    /**
     * GXT model for a group.
     */
    @BEAN(Group.class)
    public interface GroupBeanModel extends BeanModelMarker {
        /** NAME : String. */
        String NAME = "name";
        /** ID : String. */
        String ID   = "id";
    }


    /**
     * GXT model for a comment.
     */
    @BEAN(Comment.class)
    public interface CommentBeanModel extends BeanModelMarker {
        /** ID : String. */
        String ID           = "id";
        /** AUTHOR : String. */
        String AUTHOR       = "author";
        /** URL : String. */
        String URL          = "url";
        /** DATE_CREATED : String. */
        String DATE_CREATED = "timestamp";
        /** STATUS : String. */
        String STATUS       = "status";
    }


    /**
     * GXT model for a revision.
     */
    @BEAN(Revision.class)
    public interface RevisionBeanModel extends BeanModelMarker {
        /** USERNAME : String. */
        String USERNAME      = "actorUsername";
        /** HAPPENED_ON : String. */
        String HAPPENED_ON   = "happenedOn";
        /** COMMENT : String. */
        String COMMENT       = "comment";
        /** IS_MAJOR_EDIT : String. */
        String IS_MAJOR_EDIT = "major";
        /** INDEX : String. */
        String INDEX         = "index";
    }
}
