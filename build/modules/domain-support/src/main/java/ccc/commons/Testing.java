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

package ccc.commons;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Helper methods for testing.
 *
 * @author Civic Computing Ltd.
 */
public final class Testing {

    private Testing() { /* NO-OP */ }

    /**
     * Create a test dummy.
     *
     * @param <T> The type of the interface.
     * @param theInterface The interface the dummy should implement.
     * @return An instance of type T.
     */
    public static <T> T dummy(final Class<T> theInterface) {

        return theInterface.cast(
            Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {theInterface},
                new InvocationHandler() {
                    /** {@inheritDoc} */
                    @Override
                    public Object invoke(final Object proxy,
                                         final Method method,
                                         final Object[] args) throws Throwable {
                        throw new UnsupportedOperationException(
                            "Method '"
                            + method.getName()
                            + "()' invoked on dummy for type '"
                            + theInterface.getSimpleName()
                            + "'.");
                    }
                }));
    }

    /**
     * Create a test dummy.
     *
     * @param <T> The type of the interface.
     * @param theInterface The interface the dummy should implement.
     * @return An instance of type T.
     */
    public static <T> T stub(final Class<T> theInterface) {

        return theInterface.cast(
            Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {theInterface},
                new InvocationHandler() {
                    /** {@inheritDoc} */
                    @Override
                    public Object invoke(final Object proxy,
                                         final Method method,
                                         final Object[] args) {
                        return null;
                    }
                }));
    }

    /**
     * Create a dummy string by repeating the specified character, 'length'
     * times.
     *
     * @param c The character to repeat.
     * @param length The length of the string.
     * @return The dummy string 'c*length'.
     */
    public static String dummyString(final char c, final int length) {
        final StringBuilder dummyString = new StringBuilder();
        for (int i=0; i<length; i++) {
            dummyString.append(c);
        }
        return dummyString.toString();
    }

    /**
     * Constructs an instance of a class.
     * <p>Calls the no-args constructor, making it accessible if necessary.
     *
     * @param clazz The class to construct.
     */
    public static void construct(final Class<?> clazz) {
        try {
            final Constructor<?> c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            c.newInstance();
        } catch (final SecurityException e) {
            throw new RuntimeException(e);
        } catch (final IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
