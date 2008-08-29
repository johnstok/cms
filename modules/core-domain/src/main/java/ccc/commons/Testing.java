/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.commons;

import java.lang.reflect.InvocationHandler;
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
}
