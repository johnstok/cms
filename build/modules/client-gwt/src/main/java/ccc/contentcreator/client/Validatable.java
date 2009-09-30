/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;


/**
 * Simple object validation API.
 *
 * @author Civic Computing Ltd.
 */
public interface Validatable {

    /**
     * Confirm whether the object is valid.
     *
     * @return Validation result object.
     */
    ValidationResult getValidationResult();
}
