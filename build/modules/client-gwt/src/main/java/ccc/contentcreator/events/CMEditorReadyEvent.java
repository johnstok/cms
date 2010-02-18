/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.events;

import ccc.contentcreator.client.CodeMirrorEditor;
import ccc.contentcreator.core.Event;


/**
 * An event indicating an CodeMirror editor is initialised.
 *
 * @author Civic Computing Ltd.
 */
public class CMEditorReadyEvent implements Event {

    private CodeMirrorEditor _cme;

    /**
     * Constructor.
     *
     * @param cme The CodeMirrorEditor.
     */
    public CMEditorReadyEvent(final CodeMirrorEditor cme) {
        _cme = cme;
    }

    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Type.CM_EDITOR_READY;
    }

    /**
     * Accessor.
     *
     * @return Returns the editor.
     */
    public CodeMirrorEditor getCodeMirrorEditor() {
        return _cme;
    }
}
