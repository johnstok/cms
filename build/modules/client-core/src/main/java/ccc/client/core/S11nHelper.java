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
package ccc.client.core;

import ccc.api.core.ACL;
import ccc.api.core.API;
import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.Alias;
import ccc.api.core.Comment;
import ccc.api.core.Failure;
import ccc.api.core.File;
import ccc.api.core.Folder;
import ccc.api.core.Group;
import ccc.api.core.Page;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.DBC;
import ccc.api.types.Duration;
import ccc.plugins.s11n.S11nException;
import ccc.plugins.s11n.Serializers;


/**
 * Helper class for serialization.
 *
 * @author Civic Computing Ltd.
 */
public class S11nHelper {

    private final Serializers _serializers;


    /**
     * Constructor.
     *
     * @param serializersAdapter
     */
    public S11nHelper() {
        this(InternalServices.SERIALIZERS);
    }


    /**
     * Constructor.
     *
     * @param serializers The serializer factory to use.
     */
    public S11nHelper(final Serializers serializers) {
        _serializers = DBC.require().notNull(serializers);
    }


    /**
     * Parse the response as a resource summary.
     *
     * @param response The response to parse.
     *
     * @return The resource summary.
     */
    public ResourceSummary parseResourceSummary(final Response response) {
        return
            serializers()
                .create(ResourceSummary.class)
                .read(response.getText());
    }


    /**
     * Read a template from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding template.
     */
    public Template parseTemplate(final Response response) {
        return
            serializers()
                .create(Template.class)
                .read(response.getText());
    }


    /**
     * Read a collection of resource summaries from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<ResourceSummary> parseResourceSummaries(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    /**
     * Read a collection of action summaries from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<ActionSummary> readActionSummaryCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    /**
     * Read a collection of files from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<File> readFileSummaries(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    /**
     * Read a collection of template from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<Template> readTemplates(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    /**
     * Read a collection of comments from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<Comment> readComments(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    /**
     * Read a collection of users from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<User> readUserCollection(
                                                      final Response response) {
        return
            serializers()
                .create(new PagedCollection(User.class).getClass())
                .read(response.getText());
    }


    /**
     * Read a collection of groups from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<Group> readGroups(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    /**
     * Read a collection of revisions from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding collection.
     */
    public PagedCollection<Revision> readRevisionCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    /**
     * Read an API from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding API.
     */
    public API readAPI(final Response response) {
        return serializers().create(API.class).read(response.getText());
    }


    /**
     * Read a resource from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding resource.
     */
    public Resource readResource(final Response response) {
        return serializers().create(Resource.class).read(response.getText());
    }


    /**
     * Read a folder from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding folder.
     */
    public Folder readFolder(final Response response) {
        return serializers().create(Folder.class).read(response.getText());
    }


    /**
     * Read a resource summary from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding resource summary.
     */
    public ResourceSummary readResourceSummary(final Response response) {
        return serializers()
                .create(ResourceSummary.class)
                .read(response.getText());
    }


    /**
     * Read a duration from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding duration.
     */
    public Duration readDuration(final Response response) {
        return serializers().create(Duration.class).read(response.getText());
    }


    /**
     * Read a page from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding page.
     */
    public Page readPage(final Response response) {
        return serializers().create(Page.class).read(response.getText());
    }


    /**
     * Read an ACL from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding ACL.
     */
    public ACL readACL(final Response response) {
        return serializers().create(ACL.class).read(response.getText());
    }


    /**
     * Read a boolean from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding boolean.
     */
    public Boolean readBoolean(final Response response) {
        return serializers().create(Boolean.class).read(response.getText());
    }


    /**
     * Read a user from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding user.
     */
    public User readUser(final Response response) {
        return serializers().create(User.class).read(response.getText());
    }


    /**
     * Read a group from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding group.
     */
    public Group readGroup(final Response response) {
        return serializers().create(Group.class).read(response.getText());
    }


    /**
     * Read a file from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding file.
     */
    public File readFile(final Response response) {
        return serializers().create(File.class).read(response.getText());
    }


    /**
     * Read a file from a response.
     *
     * @param data The response to read.
     *
     * @return The corresponding file.
     */
    public File readFile(final String data) {
        return serializers().create(File.class).read(data);
    }


    /**
     * Read a template from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding template.
     */
    public Template readTemplate(final Response response) {
        return serializers().create(Template.class).read(response.getText());
    }


    /**
     * Read a resource summary from a string.
     *
     * @param data The data to read.
     *
     * @return The corresponding resource summary.
     */
    public ResourceSummary readResourceSummary(final String data) {
        return serializers().create(ResourceSummary.class).read(data);
    }


    /**
     * Read a failure from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding failure.
     */
    public Failure readFailure(final String response) {
        return serializers().create(Failure.class).read(response);
    }


    /**
     * Read an exception from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding exception.
     */
    public Throwable readException(final Response response) {
        try {
            return
                new RemoteException(
                    serializers()
                        .create(Failure.class)
                        .read(response.getText()));
        } catch (final S11nException e) {
            return
                new RuntimeException(
                    response.getStatusCode() + " "
                    + response.getStatusText() + "\n\n"
                    + response.getText());
        }
    }


    /**
     * Write a template to a string.
     *
     * @param t The template to write.
     *
     * @return The string representation.
     */
    public String writeTemplate(final Template t) {
        return serializers().create(Template.class).write(t);
    }


    /**
     * Write an alias to a string.
     *
     * @param alias The alias to write.
     *
     * @return The string representation.
     */
    public String writeAlias(final Alias alias) {
        return serializers().create(Alias.class).write(alias);
    }


    /**
     * Write a page to a string.
     *
     * @param page The page to write.
     *
     * @return The string representation.
     */
    public String writePage(final Page page) {
        return serializers().create(Page.class).write(page);
    }


    /**
     * Write a user to a string.
     *
     * @param user The user to write.
     *
     * @return The string representation.
     */
    public String writeUser(final User user) {
        return serializers().create(User.class).write(user);
    }


    /**
     * Write an ACL to a string.
     *
     * @param acl The ACL to write.
     *
     * @return The string representation.
     */
    public String writeACL(final ACL acl) {
        return serializers().create(ACL.class).write(acl);
    }


    /**
     * Write a resource to a string.
     *
     * @param resource The resource to write.
     *
     * @return The string representation.
     */
    public String writeResource(final Resource resource) {
        return serializers().create(Resource.class).write(resource);
    }


    /**
     * Write a folder to a string.
     *
     * @param folder The folder to write.
     *
     * @return The string representation.
     */
    public String writeFolder(final Folder folder) {
        return serializers().create(Folder.class).write(folder);
    }


    /**
     * Write a comment to a string.
     *
     * @param comment The comment to write.
     *
     * @return The string representation.
     */
    public String writeComment(final Comment comment) {
        return serializers().create(Comment.class).write(comment);
    }

    /**
     * Write a file to a string.
     *
     * @param file The file to write.
     *
     * @return The string representation.
     */
    public String writeFile(final File file) {
        return serializers().create(File.class).write(file);
    }


    /**
     * Write an action to a string.
     *
     * @param action The action to write.
     *
     * @return The string representation.
     */
    public String writeAction(final ccc.api.core.Action action) {
        return serializers().create(ccc.api.core.Action.class).write(action);
    }


    /**
     * Write a group to a string.
     *
     * @param group The group to write.
     *
     * @return The string representation.
     */
    public String writeGroup(final Group group) {
        return serializers().create(Group.class).write(group);
    }


    /**
     * Write a resource criteria to a string.
     *
     * @param criteria The resource criteria to write.
     *
     * @return The string representation.
     */
    public String writeResourceCriteria(final ResourceCriteria criteria) {
        return serializers().create(ResourceCriteria.class).write(criteria);
    }


    /**
     * Read an action from a response.
     *
     * @param response The response to read.
     *
     * @return The corresponding action.
     */
    public Action readAction(final Response response) {
        return serializers().create(Action.class).read(response.getText());
    }


    private Serializers serializers() { return _serializers; }
}
