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
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.Duration;
import ccc.plugins.s11n.Serializers;
import ccc.plugins.s11n.json.SerializerFactory;


/**
 * Helper class for serialization.
 *
 * @author Civic Computing Ltd.
 */
public class S11nHelper {

    private final Serializers _serializers =
        new SerializerFactory(InternalServices.PARSER);


    /**
     * Parse the response as a resource summary.
     *
     * @param response The response to parse.
     *
     * @return The resource summary.
     */
    protected ResourceSummary parseResourceSummary(final Response response) {
        return
            serializers()
                .create(ResourceSummary.class)
                .read(response.getText());
    }


    protected Template parseTemplate(final Response response) {
        return
            serializers()
                .create(Template.class)
                .read(response.getText());
    }


    protected PagedCollection<ResourceSummary> parseResourceSummaries(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected PagedCollection<ActionSummary> readActionSummaryCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected PagedCollection<File> readFileSummaries(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected PagedCollection<Template> readTemplates(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected PagedCollection<Comment> readComments(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected PagedCollection<User> readUserCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected PagedCollection<Group> readGroups(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected PagedCollection<Revision> readRevisionCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(response.getText());
    }


    protected API readAPI(final Response response) {
        return serializers().create(API.class).read(response.getText());
    }


    protected ResourceSummary readResourceSummary(final Response response) {
        return serializers()
                .create(ResourceSummary.class)
                .read(response.getText());
    }


    protected Duration readDuration(final Response response) {
        return serializers().create(Duration.class).read(response.getText());
    }


    protected Page readPage(final Response response) {
        return serializers().create(Page.class).read(response.getText());
    }


    protected ACL readACL(final Response response) {
        return serializers().create(ACL.class).read(response.getText());
    }


    protected User readUser(final Response response) {
        return serializers().create(User.class).read(response.getText());
    }


    protected Group readGroup(final Response response) {
        return serializers().create(Group.class).read(response.getText());
    }


    protected File readFile(final Response response) {
        return serializers().create(File.class).read(response.getText());
    }


    public ResourceSummary readResourceSummary(final String data) {
        return serializers().create(ResourceSummary.class).read(data);
    }

    public Failure readFailure(final String response) {
        return serializers().create(Failure.class).read(response);
    }


    protected String writeTemplate(final Template t) {
        return serializers().create(Template.class).write(t);
    }


    protected String writeAlias(final Alias alias) {
        return serializers().create(Alias.class).write(alias);
    }


    protected String writePage(final Page page) {
        return serializers().create(Page.class).write(page);
    }


    protected String writeUser(final User user) {
        return serializers().create(User.class).write(user);
    }


    protected String writeACL(final ACL acl) {
        return serializers().create(ACL.class).write(acl);
    }


    protected String writeResource(final Resource resource) {
        return serializers().create(Resource.class).write(resource);
    }


    protected String writeFolder(final Folder folder) {
        return serializers().create(Folder.class).write(folder);
    }


    protected String writeComment(final Comment comment) {
        return serializers().create(Comment.class).write(comment);
    }

    protected String writeFile(final File file) {
        return serializers().create(File.class).write(file);
    }


    protected String writeAction(final ccc.api.core.Action action) {
        return serializers().create(ccc.api.core.Action.class).write(action);
    }


    protected String writeGroup(final Group group) {
        return serializers().create(Group.class).write(group);
    }


    private Serializers serializers() { return _serializers; }
}
