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
package ccc.acceptance.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import ccc.api.types.DBC;


/**
 * Helper class to bind a resource bundle to an interface.
 *
 * @author Civic Computing Ltd.
 */
public final class BundleWrapper {

    private BundleWrapper() { super(); }

    /**
     * Bind a resource bundle to an interface.
     *
     * @param <T> The type to bind.
     * @param clazz Class representing the type to bind.
     * @param bundleName The name of the resource bundle.
     *
     * @return An instance of the specified interface, backed by the specified
     *  resource bundle.
     */
    public static <T> T wrap(final Class<T> clazz,
                             final String bundleName) {
        DBC.require().notEmpty(bundleName);
        DBC.require().notNull(clazz);

        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        final ResourceBundle myResources =
            ResourceBundle.getBundle(bundleName, Locale.getDefault(), cl);

        return clazz.cast(
            Proxy.newProxyInstance(
                cl,
                new Class[] {clazz},
                new InvocationHandler() {
                    /** {@inheritDoc} */
                    @Override
                    public Object invoke(final Object proxy,
                                         final Method method,
                                         final Object[] args) {
                        final String resource =
                            myResources.getString(method.getName());
                        return MessageFormat.format(resource, args);
                    }
                }));
    }
}
