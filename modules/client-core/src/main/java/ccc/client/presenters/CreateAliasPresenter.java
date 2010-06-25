/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.client.presenters;

import ccc.api.core.Alias;
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.client.actions.CreateAliasAction;
import ccc.client.actions.ResourceNameExistsAction;
import ccc.client.core.AbstractPresenter;
import ccc.client.core.Editable;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.events.Event;
import ccc.client.views.CreateAlias;


/**
 * MVP Presenter for creating alias.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasPresenter
    extends
        AbstractPresenter<CreateAlias, ResourceSummary>
    implements
        Editable {


    /**
     * Constructor.
     *
     * @param view View implementation.
     * @param model Model implementation.
     */
    public CreateAliasPresenter(final CreateAlias view,
                                final ResourceSummary model) {
        super(view, model);
        getView().setTargetName(model.getName());
        getView().show(this);
    }



    /** {@inheritDoc} */
    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void save() {
        final ValidationResult vr = getView().getValidationResult();
        if (vr.isValid()) {
            // FIXME: Conversion to type ResourceName can fail.
            new ResourceNameExistsAction(
                getView().getParent2(),
                new ResourceName(getView().getAliasName())) {
                @Override protected void execute(final boolean nameExists) {
                    if (nameExists) {
                        InternalServices.WINDOW.alert(
                            I18n.UI_MESSAGES.
                            nameExistsInFolder(getView().getAliasName()));
                    } else {
                        createAlias().run();
                    }

                }
            }.execute();
        } else {
            getView().alert(vr.getErrorText());
        }
    }

    private Runnable createAlias() {
        return new Runnable() {
            public void run() {
                final Alias a = new Alias();
                a.setParent(getView().getParent2().getId());
                a.setName(new ResourceName(getView().getAliasName()));
                a.setTargetId(getModel().getId());

                new CreateAliasAction(a).execute();
            }
        };
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final Event<CommandType> event) {
        switch (event.getType()) {
            case ALIAS_CREATE:
                dispose();
                break;

            default:
                break;
        }
    }
}
