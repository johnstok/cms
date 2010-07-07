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
     * Convert a collection of resource summaries to model data.
     *
     * @param resources The resources.
     *
     * @return The model data.
     */
    public static List<BeanModel> bindResourceSummary(
                               final Collection<ResourceSummary> resources) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(ResourceSummary.class).createModel(resources);
    }


    /**
     * Convert a resource summary to model data.
     *
     * @param rs The resource.
     *
     * @return The model data.
     */
    public static BeanModel bindResourceSummary(final ResourceSummary rs) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(ResourceSummary.class).createModel(rs);
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
     * Create model data objects for a collection of files.
     *
     * @param files The files to bind.
     *
     * @return The corresponding model data objects.
     */
    public static List<BeanModel> bindFileSummary(
                                           final Collection<File> files) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(File.class).createModel(files);
    }


    /**
     * Create a model data object for a file.
     *
     * @param file The file to bind.
     *
     * @return The corresponding model data object.
     */
    public static BeanModel bindFileSummary(final File file) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(File.class).createModel(file);
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
     * Bind a collection of action summaries.
     *
     * @param actions The action summaries to bind.
     *
     * @return The corresponding gxt models.
     */
    public static List<BeanModel> bindActionSummary(
                                      final Collection<ActionSummary> actions) {
        final BeanModelLookup ml = BeanModelLookup.get();
        return ml.getFactory(ActionSummary.class).createModel(actions);
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


    /**
     * GXT model for a file.
     */
    @BEAN(File.class)
    public interface FileBeanModel extends BeanModelMarker {
        /** NAME : String. */
        String NAME = "name";
        /** PATH : String. */
        String PATH = "path";
        /** TITLE : String. */
        String TITLE = "title";
        /** SHORT_NAME : String. */
        String SHORT_NAME = "title";
        /** WIDTH : String. */
        String WIDTH = "width";
        /** HEIGHT : String. */
        String HEIGHT = "height";
    }


    /**
     * GXT model for an action summary.
     */
    @BEAN(ActionSummary.class)
    public interface ActionSummaryBeanModel extends BeanModelMarker {
    }


    /**
     * GXT model for a resource summary.
     */
    @BEAN(ResourceSummary.class)
    public interface ResourceSummaryBeanModel extends BeanModelMarker {
    }
}
