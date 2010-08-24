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
package ccc.plugins.s11n.json;

import java.util.HashMap;
import java.util.Map;

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
import ccc.api.core.PageCriteria;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.Duration;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.SearchResult;
import ccc.plugins.s11n.Serializer;
import ccc.plugins.s11n.Serializers;
import ccc.plugins.s11n.TextParser;


/**
 * A factory for serializers.
 *
 * @author Civic Computing Ltd.
 */
public final class SerializerFactory implements Serializers {

    private final TextParser _textParser;

    // TODO: How can we constrain the wildcards to a single type?
    private final Map<Class<?>, Serializer<?>> supportedClasses;
    private final Map<String, Class<?>>        supportedNames;


    /**
     * Constructor.
     *
     * @param textParser The text parser to use.
     */
    public SerializerFactory(final TextParser textParser) {
        super();

        _textParser = textParser;
        supportedClasses = new HashMap<Class<?>, Serializer<?>>();
        supportedNames   = new HashMap<String, Class<?>>();

        addSerializer(ACL.class, new ACLSerializer());
        addSerializer(Action.class, new ActionSerializer());
        addSerializer(ActionSummary.class, new ActionSummarySerializer());
        addSerializer(Alias.class, new AliasSerializer());
        addSerializer(API.class, new APISerializer());
        addSerializer(Comment.class, new CommentSerializer());
        addSerializer(Duration.class, new DurationSerializer());
        addSerializer(Failure.class, new FailureSerializer());
        addSerializer(File.class, new FileSerializer());
        addSerializer(Folder.class, new FolderSerializer());
        addSerializer(Group.class, new GroupSerializer());
        addSerializer(MimeType.class, new MimeTypeSerializer());
        addSerializer(Page.class, new PageSerializer());
        addSerializer(Paragraph.class, new ParagraphSerializer());
        addSerializer(ResourceSummary.class, new ResourceSummarySerializer());
        addSerializer(Revision.class, new RevisionSerializer());
        addSerializer(SearchResult.class, new SearchResultSerializer());
        addSerializer(Template.class, new TemplateSerializer());
        addSerializer(Resource.class, new TempSerializer());
        addSerializer(User.class, new UserSerializer());
        addSerializer(PageCriteria.class, new PageCriteriaSerializer());
        addSerializer(
            PagedCollection.class, new PagedCollectionSerializer(this));
        addSerializer(
            ResourceCriteria.class,
            new ResourceCriteriaSerializer<ResourceCriteria>() {
                @Override protected ResourceCriteria createObject() {
                    return new ResourceCriteria();
                }});
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked") // TODO: Find a cleaner solution.
    public <T> Serializer<T> create(final Class<T> clazz) {
        return (Serializer<T>) supportedClasses.get(clazz);
    }


    /**
     * Add a new serializer.
     *
     * @param <T> The type to be serialized / deserialized.
     * @param clazz The class representing type T.
     * @param serializer The corresponding serializer.
     */
    public <T> void addSerializer(final Class<T> clazz,
                                         final Serializer<T> serializer) {
        supportedClasses.put(clazz, serializer);
        supportedNames.put(clazz.getName(), clazz);
    }


    /** {@inheritDoc} */
    @Override
    public boolean canCreate(final Class<?> clazz) {
        return supportedClasses.keySet().contains(clazz);
    }


    /** {@inheritDoc} */
    @Override
    public Class<?> findClass(final String name) {
        return supportedNames.get(name);
    }


    /** {@inheritDoc} */
    @Override
    public TextParser textParser() {
        return _textParser;
    }
}
