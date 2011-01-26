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
package ccc.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Helper methods for Java reflection.
 *
 * @author Civic Computing Ltd.
 */
public final class Reflection {


    private Reflection() { super(); }


    public static <T> T construct(final Class<T> expectedClass,
                                  final String className) {
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

        final Class<?>[] types = classesForObjects(theArguments);

        return
            construct(
                expectedClass,
                className,
                types,
                theArguments);
    }


    public static <T> T construct(final Class<T> expectedClass,
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
            if (e.getCause() instanceof RuntimeException) {
                final RuntimeException re = (RuntimeException) e.getCause();
                throw re;
            }
            throw new RuntimeException(e.getCause());
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Invoke a method on the specified object.
     *
     * @param className The class of the object.
     * @param methodName The name of the method.
     * @param obj The object on which the method will be invoked
     * @param params The parameters for the invocation.
     *
     * @return The return value of the method, as an object.
     */
    public static Object invoke(final String className,
                                final String methodName,
                                final Object obj,
                                final Object[] params) {
        return
            invoke(
                className, methodName, obj, params, classesForObjects(params));
    }


    /**
     * Invoke a method on the specified object.
     *
     * @param className The class of the object.
     * @param methodName The name of the method.
     * @param obj The object on which the method will be invoked
     * @param params The parameters for the invocation.
     * @param classes The classes of the method's parameters.
     *
     * @return The return value of the method, as an object.
     */
    public static Object invoke(final String className,
                              final String methodName,
                              final Object obj,
                              final Object[] params,
                              final Class<?>[] classes) {
        final String errorMessage = "Unable to invoke method.";

        try {
            final Class<?> c = Class.forName(className);
            final Method selectedMethod =
                c.getDeclaredMethod(methodName, classes);
            selectedMethod.setAccessible(true);
            return selectedMethod.invoke(obj, params);

        } catch (final SecurityException e) {
            throw new RuntimeException(errorMessage, e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(errorMessage, e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(errorMessage, e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(errorMessage, e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(errorMessage, e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(errorMessage, e);
        }
    }


    /**
     * Test if the specified object is an instance of the specified class.
     *
     * @param className The class.
     * @param object The object to test.
     *
     * @return True if the object is an instance of the class; false otherwise.
     */
    public static boolean isClass(final String className, final Object object) {
        try {
            final Class<?> clazz = Class.forName(className);
            return clazz.isAssignableFrom(object.getClass());
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }


    private static Class<?>[] classesForObjects(final Object[] theArguments) {
        final List<Class<?>> types = new ArrayList<Class<?>>();
        for (final Object o : theArguments) {
            types.add(o.getClass());
        }
        return types.toArray(new Class[] {});
    }
}
