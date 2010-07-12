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

import ccc.api.core.Template;
import ccc.client.core.I18n;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.widgets.CodeMirrorEditor;
import ccc.client.gwt.widgets.CodeMirrorEditor.EditorListener;
import ccc.client.gwt.widgets.CodeMirrorEditor.Type;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;


/**
 * Dialog for a read-only view of a template.
 *
 * @author Civic Computing Ltd.
 */
public class PreviewTemplateDialog
    extends AbstractBaseDialog
    implements EditorListener {

    private final Template _template;
    private final CodeMirrorEditor _bodyEditor;
    private final CodeMirrorEditor _definitionEditor;

    /** DEFAULT_WIDTH : int. */
    protected static final int DEFAULT_WIDTH = 640;
    /** DEFAULT_HEIGHT : int. */
    protected static final int DEFAULT_HEIGHT = 366;
    /** TEXT_AREA_HEIGHT : int. */
    protected static final int TEXT_AREA_HEIGHT = 300;
    /**
     * Constructor.
     *
     * @param template Template to display.
     */
    public PreviewTemplateDialog(final Template template) {

        super(I18n.UI_CONSTANTS.template()+": "+template.getTitle(),
            new GlobalsImpl());
        _template = template;
        setHeight(DEFAULT_HEIGHT);
        TabPanel folder = new TabPanel();
        folder.setWidth(DEFAULT_WIDTH);
        folder.setHeight(DEFAULT_HEIGHT);

        TabItem definition = new TabItem(I18n.UI_CONSTANTS.definitionXML());
        definition.addStyleName("pad-text");

        _definitionEditor = new CodeMirrorEditor(
            "definition",
            this,
            CodeMirrorEditor.Type.DEFINITION,
            true);
        definition.add(_definitionEditor);
        folder.add(definition);

        TabItem body = new TabItem(I18n.UI_CONSTANTS.body());
        body.addStyleName("pad-text");

        _bodyEditor = new CodeMirrorEditor(
            "body",
            this,
            CodeMirrorEditor.Type.BODY,
            true);
        body.add(_bodyEditor);
        folder.add(body);
        add(folder);

        addResizeListener();
    }

    /** {@inheritDoc} */
    @Override
    public void onInitialized(final Type type, final CodeMirrorEditor editor) {
        // FIXME: Dodgy.
        if (CodeMirrorEditor.Type.BODY == type) {
            editor.setEditorCode(_template.getBody());
        } else if (CodeMirrorEditor.Type.DEFINITION == type) {
            editor.setEditorCode(_template.getDefinition());
        }
    }

    private void addResizeListener() {

        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int h =
                    be.getHeight()-(DEFAULT_HEIGHT - TEXT_AREA_HEIGHT);
                if (h > (DEFAULT_HEIGHT - TEXT_AREA_HEIGHT)) {
                    _definitionEditor.setEditorHeight(h+"px");
                    _bodyEditor.setEditorHeight(h+"px");
                }
            }
        });
    }

}
