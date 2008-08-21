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

package ccc.view.contentcreator.dialogs;

import ccc.view.contentcreator.client.Constants;
import ccc.view.contentcreator.client.GwtApp;
import ccc.view.contentcreator.client.ResourceServiceAsync;
import ccc.view.contentcreator.dto.TemplateDTO;
import ccc.view.contentcreator.widgets.ButtonBar;
import ccc.view.contentcreator.widgets.FeedbackPanel;
import ccc.view.contentcreator.widgets.PanelControl;
import ccc.view.contentcreator.widgets.TwoColumnForm;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateContentTemplateDialog {

    private final AppDialog     _delegate;
    private final GwtApp        _app;
    private final Constants     _constants;
    private final PanelControl  _gui;
    private final ResourceServiceAsync _resourceService;

    private final StringControl _templateTitle;
    private final StringControl _description;
    private final StringControl _body;

    /**
     * Constructor.
     *
     * @param app
     */
    public CreateContentTemplateDialog(final GwtApp app) {

        _app = app;
        _constants = _app.constants();
        _delegate = _app.dialog(_constants.createDisplayTemplate());
        _gui = _app.verticalPanel();
        _resourceService = _app.lookupService();

        _templateTitle = _app.textBox();
        _description = _app.textBox();
        _body = _app.textArea();

        drawGUI();
        _delegate.gui(_gui);
    }

    private void drawGUI() {

        final FeedbackPanel fPanel = new FeedbackPanel(_app);
        fPanel.setVisible(false);
        _gui.add(fPanel);

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
                    new HidingClickListener(_delegate))
                .add(
                    _constants.save(),
                    new ClickListener() {
                        public void onClick(final Widget sender) {
                            final TemplateDTO dto = model();
                            if (dto.isValid()) {
                                _resourceService.createTemplate(
                                    dto,
                                    new DisposingCallback(_app, _delegate));
                            } else {
                                final FeedbackPanel foo =
                                    (FeedbackPanel) _gui.child(0);
                                foo.displayErrors(dto.validate());
                                foo.setVisible(true);
                            }
                        }})
            );
    }

    private TemplateDTO model() {
        return new TemplateDTO(
            _templateTitle.model(),
            _description.model(),
            _body.model());
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    public void center() {
        _delegate.center();
    }
}
