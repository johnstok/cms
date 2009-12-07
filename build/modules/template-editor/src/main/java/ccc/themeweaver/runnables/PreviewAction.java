/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
import ccc.commons.VelocityProcessor;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PreviewAction
    extends
        AbstractAction {


    private BasicPanel        previewPane;
    private JEditorPane       editorPane;

    private final Folder      root             = new Folder("foo");

    private final Folder      home             = new Folder("Home");
    private final Folder      media            = new Folder("Media");
    private final Folder      policy           = new Folder("Policy");
    private final Folder      quitSmoking      = new Folder("QuitSmoking");
    private final Folder      ourWork          = new Folder("OurWork");
    private final Folder      information      = new Folder("Information");
    private final Folder      training         = new Folder("Training");

    private final Page        p1               = new Page(new ResourceName("p1"), "About us");
    private final Page        p2               = new Page(new ResourceName("p2"), "Support us");
    private final Page        p3               = new Page(new ResourceName("p3"), "Contact us");
    private final Page        p4               = new Page(new ResourceName("p4"), "Vacancies");

    /**
     * Constructor.
     *
     * @param editorPane
     * @param previewPane
     */
    public PreviewAction(final JEditorPane editorPane,
                         final XHTMLPanel previewPane) {

        super("Preview");
        this.editorPane = editorPane;
        this.previewPane = previewPane;

        root.add(home);
        root.add(media);
        root.add(policy);
        root.add(quitSmoking);
        root.add(ourWork);
        root.add(information);
        root.add(training);
        home.add(p1);
        home.add(p2);
        home.add(p3);
        home.add(p4);

        p1.addParagraph(Paragraph.fromText("CONTENT", Resources.readIntoString(getClass().getResource("content.txt"), Charset.forName("UTF-8"))));
        p1.addParagraph(Paragraph.fromText("HEADER", "About us"));
        p1.addParagraph(Paragraph.fromText("Description_Custom", "ASH Scotland, our history, campaigns, organisation and contacts; our role in UK tobacco control; annual report, organisational chart; complaints, complain; problems"));
        p1.addParagraph(Paragraph.fromText("Keywords_Custom", "ASH UK; ASH Scotland (UK); external complaints; complain; problems, AGM, annual general meeting, annual report, accounts, charity"));
        p1.addParagraph(Paragraph.fromText("STYLESHEET", "home"));
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent arg0) {

        final NamespaceHandler nsh = new XhtmlNamespaceHandler();
        final String previewText = new VelocityProcessor().render(p1, root, editorPane.getText());
        System.out.println(previewText);
        previewPane.setDocumentFromString(previewText, null, nsh);
    }
}
