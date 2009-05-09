/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates a class whose instances have 'value object' semantics.
 * <p>
 * In other words they are fungible. Obvious examples are 'Money', 'Username',
 * etc. See <a href="http://www.martinfowler.com/bliki/ValueObject.html>Martin
 * Fowler's article</a> for more details.
 *
 * @author Civic Computing Ltd.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ValueObject {
    // No values.
}
