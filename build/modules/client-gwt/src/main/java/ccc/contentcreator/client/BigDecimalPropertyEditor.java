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
package ccc.contentcreator.client;

import java.math.BigDecimal;

import com.extjs.gxt.ui.client.widget.form.NumberPropertyEditor;


/**
 * Property editor for {@link BigDecimal}.
 *
 * @author Civic Computing Ltd.
 */
public class BigDecimalPropertyEditor extends NumberPropertyEditor {
    /**
     * Constructor.
     *
     * @param type The number class.
     */
    public BigDecimalPropertyEditor(final Class<?> type) {
        super(type);
    }

    /** {@inheritDoc} */
    @Override
    public Number convertStringValue(final String value) {
        if (type == BigDecimal.class) {
            return new BigDecimal(value);
        }
        return super.convertStringValue(value);
    }
}
