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
import ccc.plugins.s11n.json.Json;
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
                .read(parse(response.getText()));
    }


    protected Template parseTemplate(final Response response) {
        return
            serializers()
                .create(Template.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<ResourceSummary> parseResourceSummaries(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<ActionSummary> readActionSummaryCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<File> readFileSummaries(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<Template> readTemplates(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<Comment> readComments(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<User> readUserCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<Group> readGroups(final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected PagedCollection<Revision> readRevisionCollection(
                                                      final Response response) {
        return
            serializers()
                .create(PagedCollection.class)
                .read(parse(response.getText()));
    }


    protected API readAPI(final Response response) {
        return serializers().create(API.class).read(parse(response.getText()));
    }


    protected ResourceSummary readResourceSummary(final Response response) {
        return serializers()
                .create(ResourceSummary.class)
                .read(parse(response.getText()));
    }


    protected Duration readDuration(final Response response) {
        return serializers().create(Duration.class).read(parse(response.getText()));
    }


    protected Page readPage(final Response response) {
        return serializers().create(Page.class).read(parse(response.getText()));
    }


    protected ACL readACL(final Response response) {
        return serializers().create(ACL.class).read(parse(response.getText()));
    }


    protected User readUser(final Response response) {
        return serializers().create(User.class).read(parse(response.getText()));
    }


    protected Group readGroup(final Response response) {
        return serializers().create(Group.class).read(parse(response.getText()));
    }


    protected File readFile(final Response response) {
        return serializers().create(File.class).read(parse(response.getText()));
    }


    public ResourceSummary readResourceSummary(final String data) {
        return serializers().create(ResourceSummary.class).read(parse(data));
    }

    public Failure readFailure(final String response) {
        return serializers().create(Failure.class).read(parse(response));
    }


    protected String writeTemplate(final Template t) {
        final Json json = InternalServices.PARSER.newJson();
        serializers().create(Template.class).write(json, t);
        return json.toString();
    }


    protected String writeAlias(final Alias alias) {
        final Json json = newJson();
        serializers().create(Alias.class).write(json, alias);
        return json.toString();
    }


    protected String writePage(final Page page) {
        final Json json = newJson();
        serializers().create(Page.class).write(json, page);
        return json.toString();
    }


    protected String writeUser(final User user) {
        final Json json = newJson();
        serializers().create(User.class).write(json, user);
        return json.toString();
    }


    protected String writeACL(final ACL acl) {
        final Json json = newJson();
        serializers().create(ACL.class).write(json, acl);
        return json.toString();
    }


    protected String writeResource(final Resource resource) {
        final Json json = newJson();
        serializers().create(Resource.class).write(json, resource);
        return json.toString();
    }


    protected String writeFolder(final Folder folder) {
        final Json json = newJson();
        serializers().create(Folder.class).write(json, folder);
        return json.toString();
    }


    protected String writeComment(final Comment comment) {
        final Json json = newJson();
        serializers().create(Comment.class).write(json, comment);
        return json.toString();
    }

    protected String writeFile(final File file) {
        final Json json = newJson();
        serializers().create(File.class).write(json, file);
        return json.toString();
    }


    protected String writeAction(final ccc.api.core.Action action) {
        final Json json = newJson();
        serializers().create(ccc.api.core.Action.class).write(json, action);
        return json.toString();
    }


    protected String writeGroup(final Group group) {
        final Json json = newJson();
        serializers().create(Group.class).write(json, group);
        return json.toString();
    }


    private Serializers serializers() { return _serializers; }


    private Json parse(final String response) {
        return InternalServices.PARSER.parseJson(response);
    }


    private Json newJson() { return InternalServices.PARSER.newJson(); }
}
