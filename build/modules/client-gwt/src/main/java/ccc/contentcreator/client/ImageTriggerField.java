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

import ccc.contentcreator.binding.ImageSummaryModelData;

import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * An extended trigger field storing selected image as
 * {@link ImageSummaryModelData}.
 *
 * @author Civic Computing Ltd.
 */
public class ImageTriggerField extends TriggerField<String> {

    private ImageSummaryModelData _md;

    /**
     * Mutator.
     *
     * @param md FileSummaryModelData to set.
     */
    public void setFSModel(final ImageSummaryModelData md) {
        _md = md;
    }

    /**
     * Accessor.
     *
     * @return FileSummaryModelData of the field.
     */
    public ImageSummaryModelData getFSModel() {
        return _md;
    }

}
