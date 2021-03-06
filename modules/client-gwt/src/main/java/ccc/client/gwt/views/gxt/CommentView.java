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
import ccc.api.types.CommentStatus;
import ccc.client.core.Editable;
import ccc.client.core.Globals;
import ccc.client.core.Validatable;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.binding.EnumModelData;
import ccc.client.views.ICommentView;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;


/**
 * Dialog for comment editing.
 *
 * @author Civic Computing Ltd.
 */
// TODO: This class shouldn't extend ContentPanel; composition not inheritance.
public class CommentView
    extends
        AbstractEditDialog
    implements
        Validatable,
        ICommentView {

    private final TextField<String> _author = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final TextField<String> _url = new TextField<String>();
    private final TextArea _body = new TextArea();
    private final ComboBox<EnumModelData<CommentStatus>> _status =
        new ComboBox<EnumModelData<CommentStatus>>();

    private Editable _presenter;
    private static final int TEXT_AREA_HEIGHT = 300;

    /**
     * Constructor.
     *
     * @param title The title.
     * @param globals Globals.
     */
    public CommentView(final String title, final Globals globals) {
        super(title, globals);

        _author.setFieldLabel(constants().author());
        _author.setAllowBlank(false);
        addField(_author);

        _email.setFieldLabel(constants().email());
        _email.setAllowBlank(false);
        addField(_email);

        _url.setFieldLabel(constants().url());
        addField(_url);

        final ListStore<EnumModelData<CommentStatus>> statuses =
            new ListStore<EnumModelData<CommentStatus>>();
        for (final CommentStatus status : CommentStatus.values()) {
            statuses.add(new EnumModelData<CommentStatus>(status));
        }
        _status.setFieldLabel(constants().status());
        _status.setDisplayField("name");
        _status.setStore(statuses);
        _status.setAllowBlank(false);
        _status.setEditable(false);
        _status.setTriggerAction(TriggerAction.ALL);
        addField(_status);

        _body.setFieldLabel(constants().body());
        _body.setHeight(TEXT_AREA_HEIGHT);
        addField(_body);
    }

    /** {@inheritDoc} */
    public void setAuthor(final String author) {
        _author.setValue(author);
    }

    /** {@inheritDoc} */
    public void setBody2(final String commentBody) {
        _body.setValue(commentBody);
    }

    /** {@inheritDoc} */
    public void setStatus(final CommentStatus status) {
        _status.setValue(new EnumModelData<CommentStatus>(status));
    }

    /** {@inheritDoc} */
    public void setUrl2(final String url) {
        _url.setValue(url);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                _presenter.save();
            }
        };
    }

    /** {@inheritDoc} */
    public String getAuthor() {
        return _author.getValue();
    }

    /** {@inheritDoc} */
    public String getBody2() {
        return _body.getValue();
    }

    /** {@inheritDoc} */
    public CommentStatus getStatus() {
        return _status.getValue().getValue();
    }

    /** {@inheritDoc} */
    public String getUrl2() {
        return _url.getValue();
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();
        final String url = _url.getValue();
        if (url != null && !url.trim().isEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                _url.setValue("http://"+url);
            }
            result.addError(
                validator.notValidURL(_url.getValue()));
        }

        result.addError(
            validator.notEmpty(
                _author.getValue(), _author.getFieldLabel()));
        result.addError(
            validator.notEmpty(
                _email.getValue(), _email.getFieldLabel()));
        result.addError(
            validator.notEmpty(
                _body.getValue(), _body.getFieldLabel()));
        result.addError(
            validator.notValidEmail(
                _email.getValue(), _email.getFieldLabel()));

        return result;
    }


    /** {@inheritDoc} */
    public void setEmail(final String email) {
        _email.setValue(email);
    }


    /** {@inheritDoc} */
    public String getEmail() {
        return _email.getValue();
    }


    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        show();
    }


    /** {@inheritDoc} */
    @Override
    public void cancel() {
        _presenter.cancel();
    }
}
