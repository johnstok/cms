/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.contentcreator.client;

import ccc.contentcreator.api.DialogMode;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;


/**
 * TODO Add Description for this type.
 * TODO Class name is wrong - does update too.
 * TODO Properly refresh object cache so GUI reflects changes.
 *
 * @author Civic Computing Ltd
 */
public class CreateContentTemplateDialog extends EditDialog {

    private final ResourceServiceAsync _resourceService =
        Globals.resourceService();

    private final TextField<String> _templateTitle = new TextField<String>();
    private final TextField<String> _description = new TextField<String>();
    private final TextField<String> _body = new TextField<String>();

//    private final FeedbackPanel _feedbackPanel;

    private String _id;
    private DialogMode _mode;
    private ListStore<ResourceDTO> _store;
    private TemplateDTO _model;

    /**
     * Constructor.
     */
    public CreateContentTemplateDialog() {

        _mode = DialogMode.CREATE;

        _templateTitle.setFieldLabel(_constants.title());
        _templateTitle.setAllowBlank(false);
        _templateTitle.setId(_constants.title());
        _panel.add(_templateTitle, new FormData("100%"));

        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);
        _description.setId(_constants.description());
        _panel.add(_description, new FormData("100%"));

        _body.setFieldLabel(_constants.body());
        _body.setAllowBlank(false);
        _body.setId(_constants.body());
        _panel.add(_body, new FormData("100%"));

    }

    /**
     * Constructor.
     *
     * @param item TemplateDTO for the template.
     * @param store ListStore model for the dialog.
     */
    public CreateContentTemplateDialog(final TemplateDTO item,
                                       final ListStore<ResourceDTO> store) {

        this();

        _mode = DialogMode.UPDATE;

        _id = item.getId();
        _store = store;
        _model = item;
    }

    private TemplateDTO model() { // TODO: update to handle version correctly.
        return new TemplateDTO(
            _id,
            -1,
            _templateTitle.getValue(),
            _templateTitle.getValue(),
            _description.getValue(),
            _body.getValue(),
            "<fields/>");
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final TemplateDTO dto = model();
                if (dto.isValid()) {
                    switch (_mode) {
                        case CREATE:
                        _resourceService.createTemplate(
                        dto,
                        new DisposingCallback(CreateContentTemplateDialog.this));
                        break;
                    case UPDATE:
                        _resourceService.updateTemplate(
                            dto,
                            new ErrorReportingCallback<Void>(){
                                public void onSuccess(final Void arg0) {
                                    _model.set("title", dto.getTitle());
                                    _model.set("name", dto.getName());
                                    _model.set("description", dto.getDescription());
                                    _model.set("body", dto.getBody());
                                    _store.update(_model);
                                    hide();
                                }});
                        break;
                    default:
                        Globals.alert("Error.");
                    }
                } else {
                    Globals.alert(dto.validate().toString()); // TODO Reinstate feedback panel
//                    _feedbackPanel.displayErrors(dto.validate());
//                    _feedbackPanel.setVisible(true);
                }
            }
        };
    }
}
