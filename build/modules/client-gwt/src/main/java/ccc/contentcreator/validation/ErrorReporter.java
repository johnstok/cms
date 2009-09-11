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
package ccc.contentcreator.validation;

import java.util.List;


/**
 * API for reporting validation errors.
 *
 * @author Civic Computing Ltd.
 */
public interface ErrorReporter {

    /**
     * Report the specified errors.
     *
     * @param errors The errors, as a list of strings.
     */
    void report(List<String> errors);
}
