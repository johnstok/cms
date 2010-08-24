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


/**
 * A factory for serializers.
 *
 * @author Civic Computing Ltd.
 */
public final class SerializerFactory {

    // TODO: How can we constrain the wildcards to a single type?
    private static final Map<Class<?>, Serializer<?>> SUPPORTED_CLASSES;
    private static final Map<String, Class<?>>        SUPPORTED_NAMES;

    static {
        SUPPORTED_CLASSES = new HashMap<Class<?>, Serializer<?>>();
        SUPPORTED_NAMES   = new HashMap<String, Class<?>>();

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
            ResourceCriteria.class,
            new ResourceCriteriaSerializer<ResourceCriteria>() {
                @Override protected ResourceCriteria createObject() {
                    return new ResourceCriteria();
                }});

    }

    private SerializerFactory() { super(); }


    /**
     * Create a serializer for a specified class.
     *
     * @param <T> The type of serializer to create.
     * @param clazz Class representing the type to serialize.
     *
     * @return The corresponding serializer or NULL if no serializer is
     *  available.
     */
    @SuppressWarnings("unchecked") // TODO: Find a cleaner solution.
    public static <T> Serializer<T> create(final Class<T> clazz) {
        return (Serializer<T>) SUPPORTED_CLASSES.get(clazz);
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
    @SuppressWarnings("unchecked") // TODO: Find a cleaner solution.
    public static <T> Serializer<T> create(final String clazz) {
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
        SUPPORTED_NAMES.put(clazz.getName(), clazz);
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


    /**
     * Check if a class is supported by the serializer.
     *
     * @param name The name af the class to serialize.
     *
     * @return The corresponding class, or NULL if the name isn't supported.
     */
    public static Class<?> findClass(final String name) {
        return SUPPORTED_NAMES.get(name);
    }
}
