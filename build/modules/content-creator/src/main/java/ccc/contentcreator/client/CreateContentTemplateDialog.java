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

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.DialogMode;
import ccc.contentcreator.api.PanelControl;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.StringControl;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.callbacks.DisposingClickListener;
import ccc.contentcreator.dto.TemplateDTO;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateContentTemplateDialog {

    private final ApplicationDialog     _delegate;
    private final Application        _app;
    private final UIConstants     _constants;
    private final PanelControl  _gui;
    private final ResourceServiceAsync _resourceService;

    private final StringControl _templateTitle;
    private final StringControl _description;
    private final StringControl _body;
    private final FeedbackPanel _feedbackPanel;
    private String _id;
    private DialogMode _mode;

    /**
     * Constructor.
     *
     * @param app
     */
    public CreateContentTemplateDialog(final Application app) {

        _mode = DialogMode.CREATE;
        _app = app;
        _constants = _app.constants();
        _delegate = _app.dialog(_constants.createDisplayTemplate());
        _gui = _app.verticalPanel();
        _resourceService = _app.lookupService();

        _templateTitle = _app.textBox();
        _description = _app.textBox();
        _body = _app.textArea();
        _feedbackPanel = new FeedbackPanel(_app);

        drawGUI();
        _delegate.gui(_gui);
    }

    /**
     * Constructor.
     *
     * @param app
     * @param item
     */
    public CreateContentTemplateDialog(final Application app, final TemplateDTO item) {
        this(app);
        _mode = DialogMode.UPDATE;
        _templateTitle.model(item.getTitle());
        _description.model(item.getDescription());
        _body.model(item.getBody());
        _id = item.getId();
    }

    private void drawGUI() {

        _feedbackPanel.setVisible(false);
        _gui.add(_feedbackPanel);

        _gui.add(
            new TwoColumnForm(_app, 3)
                .add(_constants.title(), _templateTitle)
                .add(_constants.description(), _description)
                .add(_constants.body(), _body)
            );

        _gui.add(
            new ButtonBar(_app)
                .add(
                    _constants.cancel(),
                    new DisposingClickListener(_delegate))
                .add(
                    _constants.save(),
                    new ClickListener() {
                        public void onClick(final Widget sender) {
                            final TemplateDTO dto = model();
                            if (dto.isValid()) {
                                switch (_mode) {
                                    case CREATE:
                                    _resourceService.createTemplate(
                                    dto,
                                    new DisposingCallback(_app, _delegate));
                                    break;
                                case UPDATE:
                                    _resourceService.updateTemplate(
                                        dto,
                                        new DisposingCallback(_app, _delegate));
                                    break;
                                default:
                                    _app.alert("Error.");
                                }
                            } else {
                                _feedbackPanel.displayErrors(dto.validate());
                                _feedbackPanel.setVisible(true);
                            }
                        }})
            );
    }

    private TemplateDTO model() {
        return new TemplateDTO(
            _templateTitle.model(),
            _description.model(),
            _body.model(),
            _id);
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    public void center() {
        _delegate.center();
    }
}
