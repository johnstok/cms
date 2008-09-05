/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.themeweaver.runnables;

import java.awt.event.ActionEvent;
import java.nio.charset.Charset;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;

import org.xhtmlrenderer.extend.NamespaceHandler;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.BasicPanel;

import ccc.commons.Resources;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;
import ccc.themeweaver.VelocityProcessor;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PreviewAction extends AbstractAction {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -95808082525224723L;

    private BasicPanel previewPane;
    private JEditorPane editorPane;

    /**
     * Constructor.
     *
     * @param editorPane
     * @param previewPane
     */
    public PreviewAction(final JEditorPane editorPane, final XHTMLPanel previewPane) {
        super("Preview");
        this.editorPane = editorPane;
        this.previewPane = previewPane;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent arg0) {
        final Page p = new Page(new ResourceName("foo"), "About us");
        p.addParagraph(
            "CONTENT",
            new Paragraph(
                Resources.readIntoString(
                    getClass().getResource("content.txt"),
                    Charset.forName("UTF-8")
                )
            )
        );
        p.addParagraph(
            "HEADER",
            new Paragraph("About us"
            )
        );
        p.addParagraph(
            "Description_Custom",
            new Paragraph("ASH Scotland, our history, campaigns, organisation and contacts; our role in UK tobacco control; annual report, organisational chart; complaints, complain; problems"
            )
        );
        p.addParagraph(
            "Keywords_Custom",
            new Paragraph("ASH UK; ASH Scotland (UK); external complaints; complain; problems, AGM, annual general meeting, annual report, accounts, charity"
            )
        );
        final NamespaceHandler nsh = new XhtmlNamespaceHandler();
        final String previewText =
            new VelocityProcessor().render(p, editorPane.getText());
        System.out.println(previewText);
        previewPane.setDocumentFromString(previewText, null, nsh);
    }
}
