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
package ccc.plugins;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import ccc.plugins.multipart.MultipartFormData;
import ccc.plugins.scripting.TextProcessor;
import ccc.plugins.search.Index;
import ccc.plugins.search.Indexer;
import ccc.plugins.security.Sessions;


/**
 * Factory for plugins.
 *
 * @author Civic Computing Ltd.
 */
public class PluginFactory {


    public TextProcessor createTemplating() {
        return
            create(
                TextProcessor.class,
                "ccc.plugins.scripting.velocity.VelocityProcessor");
    }


    public TextProcessor createScripting() {
        return
            create(
                TextProcessor.class,
                "ccc.plugins.scripting.rhino.ScriptRunner");
    }


    public Index createIndex(final Object... theArguments) {
        return
            construct(
                Index.class,
                "ccc.plugins.search.lucene.SimpleLuceneFS",
                theArguments);
    }


    public Indexer createIndexer(final Object... theArguments) {
        return
            construct(
                Indexer.class,
                "ccc.plugins.search.lucene.SimpleLuceneFS",
                theArguments);
    }


    public Sessions createSessions() {
        return
            create(
                Sessions.class,
                "ccc.plugins.security.jboss.JbossSession");
    }


    public MultipartFormData createFormData(final String charEncoding,
                                            final int contentLength,
                                            final String contentType,
                                            final InputStream inputStream) {

        final Class<?>[] types = new Class<?>[] {
            String.class,
            int.class,
            String.class,
            InputStream.class
        };

        final Object[] values = new Object[] {
            charEncoding,
            Integer.valueOf(contentLength),
            contentType,
            inputStream
        };

        return
            construct(
                MultipartFormData.class,
                "ccc.plugins.multipart.apache.MultipartForm",
                types,
                values);
    }


    private <T> T create(final Class<T> expectedClass, final String className) {
        try {
            final Class<?> theType = Class.forName(className);

            final Object o = theType.newInstance();

            return expectedClass.cast(o);

        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T construct(final Class<T> expectedClass,
                                  final String className,
                                  final Object[] theArguments) {

        final List<Class<?>> types = new ArrayList<Class<?>>();
        for (final Object o : theArguments) {
            types.add(o.getClass());
        }

        return
            construct(
                expectedClass,
                className,
                types.toArray(new Class[] {}),
                theArguments);
    }


    private static <T> T construct(final Class<T> expectedClass,
                                   final String className,
                                   final Class<?>[] types,
                                   final Object[] theArguments) {

        try {
            final Class<?> theType = Class.forName(className);

            final Object o =
                theType
                    .getConstructor(types)
                    .newInstance(theArguments);

            return expectedClass.cast(o);

        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
