/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.client.gwt.widgets;

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.client.gwt.views.gxt.ResourceSelectionDialog;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * A trigger field for selecting a CC resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTriggerField
    extends
        TriggerField<String> {

    private final ResourceSummary _targetRoot;
    private ResourceSummary _target;


    /**
     * Constructor.
     *
     * @param targetRoot The root resource containing resources.
     */
    public ResourceTriggerField(final ResourceSummary targetRoot) {
        super();
        _targetRoot = targetRoot;

        setEditable(false);

        addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be1) {
                    final ResourceSelectionDialog resourceSelect =
                        new ResourceSelectionDialog(_targetRoot, null);
                    resourceSelect.addListener(Events.Hide,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent be2) {
                            final ResourceSummary target =
                                resourceSelect.selectedResource();
                            if (!isRangeFolder(target)) {
                                _target = target;
                                setValue(_target.getName().toString());
                            }
                        }

                        });
                    resourceSelect.show();
                }});
    }


    private boolean isRangeFolder(final ResourceSummary target) {
        return null!=target && ResourceType.RANGE_FOLDER==target.getType();
    }


    /**
     * Accessor.
     *
     * @return The selected resource.
     */
    public ResourceSummary getTarget() { return _target; }


    /**
     * Mutator.
     *
     * @param target The target to set.
     */
    public void setTarget(final ResourceSummary target) {
        _target = target;
        if (null!=_target) {
            setValue(_target.getName().toString());
        } else {
            setValue(null);
        }
    }
}
