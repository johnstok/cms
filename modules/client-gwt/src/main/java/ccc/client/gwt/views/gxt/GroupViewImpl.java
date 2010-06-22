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
package ccc.client.gwt.views.gxt;

import static ccc.client.core.InternalServices.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.api.types.Permission;
import ccc.client.core.Editable;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.presenters.GroupPresenter.GroupView;
import ccc.client.gwt.widgets.ContentCreator;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * MVP view onto a group.
 *
 * @author Civic Computing Ltd.
 */
public class GroupViewImpl
    extends
        AbstractEditDialog
    implements
        GroupView {

    private Editable _presenter;

    private final TextField<String> _name = new TextField<String>();
    private final CheckBoxGroup _permGroup = new CheckBoxGroup();
    private final Map<String, CheckBox> _perms =
        new HashMap<String, CheckBox>();


    /**
     * Constructor.
     *
     * @param globals The globals implementation.
     */
    public GroupViewImpl(final Globals globals) {
        super(I18n.UI_CONSTANTS.createGroup(), globals);

        _name.setFieldLabel(I18n.UI_CONSTANTS.name());
        _name.setAllowBlank(false);
        addField(_name);

        for (final String allowedPerm : Permission.ALL) {
            final CheckBox cb = new CheckBox();
            cb.setBoxLabel(allowedPerm);
            _perms.put(allowedPerm, cb);
            _permGroup.add(cb);
        }
        _permGroup.setOrientation(Orientation.VERTICAL);
        _permGroup.setFieldLabel(I18n.UI_CONSTANTS.permissions());

        addField(_permGroup);
    }


    /** {@inheritDoc} */
    @Override
    public String getName() {
        return _name.getValue();
    }


    /** {@inheritDoc} */
    @Override
    public void setName(final String name) {
        _name.setValue(name);
    }


    /** {@inheritDoc} */
    @Override
    public Set<String> getPermissions() {
        final Set<String> permissions = new HashSet<String>();
        for (final CheckBox cb : _permGroup.getValues()) {
            permissions.add(cb.getBoxLabel()); // TODO: Can we improve this?
        }
        return permissions;
    }


    /** {@inheritDoc} */
    @Override
    public void setPermissions(final Set<String> permissions) {
        for (final String perm : permissions) {
            final CheckBox cb = _perms.get(perm);
            if (null!=cb) { cb.setValue(Boolean.TRUE); }
        }
    }


    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {
                getPresenter().save();
            }
        };
    }


    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();

        result.addError(
            VALIDATOR.notEmpty(
                _name.getValue(), _name.getFieldLabel()));

        return result;
    }


    /** {@inheritDoc} */
    @Override
    public void alert(final String message) {
        ContentCreator.WINDOW.alert(message);
    }
}
