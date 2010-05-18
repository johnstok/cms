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
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.Duration;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.SearchResult;
import ccc.plugins.s11n.Serializer;


/**
 * A factory for serializers.
 *
 * @author Civic Computing Ltd.
 */
public class SerializerFactory {

    // TODO: How can we constrain the wildcards to a single type?
    private static final Map<Class<?>, Serializer<?>> SUPPORTED_CLASSES;

    static {
        final Map<Class<?>, Serializer<?>> supported =
            new HashMap<Class<?>, Serializer<?>>();

        supported.put(ACL.class, new ACLSerializer());
        supported.put(Action.class, new ActionSerializer());
        supported.put(ActionSummary.class, new ActionSummarySerializer());
        supported.put(Alias.class, new AliasSerializer());
        supported.put(API.class, new APISerializer());
        supported.put(Comment.class, new CommentSerializer());
        supported.put(Duration.class, new DurationSerializer());
        supported.put(Failure.class, new FailureSerializer());
        supported.put(File.class, new FileSerializer());
        supported.put(Folder.class, new FolderSerializer());
        supported.put(Group.class, new GroupSerializer());
        supported.put(MimeType.class, new MimeTypeSerializer());
        supported.put(Page.class, new PageSerializer());
        supported.put(Paragraph.class, new ParagraphSerializer());
        supported.put(ResourceSummary.class, new ResourceSummarySerializer());
        supported.put(Revision.class, new RevisionSerializer());
        supported.put(SearchResult.class, new SearchResultSerializer());
        supported.put(Template.class, new TemplateSerializer());
        supported.put(Resource.class, new TempSerializer());
        supported.put(User.class, new UserSerializer());

        SUPPORTED_CLASSES = supported;
    }


    /**
     * Create a serializer for a specified class.
     *
     * @param <T> The type of serializer to create.
     * @param clazz Class representing the type to serialize.
     *
     * @return The corresponding serializer or NULL if no serializer is
     *  available.
     */
    public static <T> Serializer<T> create(final Class<T> clazz) {
        return (Serializer<T>) SUPPORTED_CLASSES.get(clazz);
    }


    /**
     * Add a new serializer.
     *
     * @param <T> The type to be serialized / deserialized.
     * @param clazz The class representing type T.
     * @param serializer The corresponding serializer.
     */
    public static <T> void addSerializer(final Class<T> clazz,
                                         final Serializer<T> serializer) {
        SUPPORTED_CLASSES.put(clazz, serializer);
    }


    /**
     * Query if a serializer is available for a specified class.
     *
     * @param clazz The class to check.
     *
     * @return True if a serializer is available; false otherwise.
     */
    public static boolean canCreate(final Class<?> clazz) {
        return SUPPORTED_CLASSES.keySet().contains(clazz);
    }
}
