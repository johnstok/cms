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


/**
 * API for a client side validator.
 *
 * @author Civic Computing Ltd.
 */
public interface Validator {

    /**
     * Perform validation.
     *
     * @param validate The validation object used by the validator.
     */
    void validate(Validate validate);

}
