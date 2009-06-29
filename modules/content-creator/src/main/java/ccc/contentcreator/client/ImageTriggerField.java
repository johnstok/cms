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

import ccc.contentcreator.binding.FileSummaryModelData;

import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * An extended trigger field storing selected image as
 * {@link FileSummaryModelData}.
 *
 * @author Civic Computing Ltd.
 */
public class ImageTriggerField extends TriggerField<String> {

    private FileSummaryModelData _md;

    /**
     * Mutator.
     *
     * @param md FileSummaryModelData to set.
     */
    public void setFSModel(final FileSummaryModelData md) {
        _md = md;
    }

    /**
     * Accessor.
     *
     * @return FileSummaryModelData of the field.
     */
    public FileSummaryModelData getFSModel() {
        return _md;
    }

}
