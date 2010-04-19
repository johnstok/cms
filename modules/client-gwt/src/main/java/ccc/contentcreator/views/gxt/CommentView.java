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
package ccc.contentcreator.views.gxt;

import ccc.contentcreator.binding.EnumModelData;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.core.Validatable;
import ccc.contentcreator.core.ValidationResult;
import ccc.contentcreator.core.Validations2;
import ccc.contentcreator.presenters.UpdateCommentPresenter;
import ccc.types.CommentStatus;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CommentView
    extends
        AbstractEditDialog
    implements
        Validatable {

    private final TextField<String> _author = new TextField<String>();
    private final TextField<String> _email = new TextField<String>();
    private final TextField<String> _url = new TextField<String>();
    private final TextArea _body = new TextArea();
    private final ComboBox<EnumModelData<CommentStatus>> _status =
        new ComboBox<EnumModelData<CommentStatus>>();

    private UpdateCommentPresenter _presenter;


    /**
     * Constructor.
     *
     * @param title
     * @param globals
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
        _body.setHeight(300);
        addField(_body);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param author
     */
    public void setAuthor(final String author) {
        _author.setValue(author);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param body
     */
    public void setBody2(final String body) {
        _body.setValue(body);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param status
     */
    public void setStatus(final CommentStatus status) {
        _status.setValue(new EnumModelData<CommentStatus>(status));
    }

    /**
     * TODO: Add a description for this method.
     * TODO: This class shouldn't extend ContentPanel; composition not
     *  inheritance.
     *
     * @param url
     */
    public void setUrl2(final String url) {
        _url.setValue(url);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                _presenter.update();
            }
        };
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String getAuthor() {
        return _author.getValue();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String getBody2() {
        return _body.getValue();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public CommentStatus getStatus() {
        return _status.getValue().getValue();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String getUrl2() {
        return _url.getValue();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param updateCommentPresenter
     */
    public void setPresenter(final UpdateCommentPresenter presenter) {
        _presenter = presenter;
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
            if (!Validations2.notValidURL(_url.getValue())) {
                result.addError(constants().websiteAddressNotValid());
            }
        }
        if (!Validations2.notEmpty(_author.getValue())) {
            result.addError(constants().commentNotValid());
        }
        if (!Validations2.notEmpty(_email.getValue())
            || !Validations2.notValidEmail(_email.getValue())) {
            result.addError(_email.getFieldLabel()
                +" "+constants().isNotValid());
        }
        return result;
    }

    /**
     * Mutator.
     *
     * @param email Email to set.
     */
    public void setEmail(final String email) {
        _email.setValue(email);
    }

    /**
     * Accessor.
     *
     * @return Email field value.
     */
    public String getEmail() {
        return _email.getValue();
    }

}
