/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Revision      $Rev: 3151 $
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2010-09-28 10:25:58 +0100 (Tue, 28 Sep 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.client.actions.FindFileAction;
import ccc.client.core.Globals;
import ccc.client.core.I18n;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


/**
 * A trigger field for term selecting.
 *
 * @author Civic Computing Ltd.
 */
public class TaxonomyTriggerField
extends
TriggerField<List<String>> {

    private List<String> _terms;


    /**
     * Constructor.
     *
     * @param targetRoot The root resource containing resources.
     * @param vocabularyID The id of the vocabulary.
     */
    public TaxonomyTriggerField(final ResourceSummary targetRoot,
                                final String vocabularyID) {
        super();

        setEditable(false);

        addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be1) {
                    new FindFileAction(UUID.fromString(vocabularyID)) {
                        @Override
                        public void onSuccess(final File item) {
                            final TaxonomySelector ts =
                                new TaxonomySelector(item.getContent());
                            ts.show();
                            ts.checkSelected(_terms);
                        }
                    }.execute();
                }
            });
    }


    /**
     * Accessor.
     *
     * @return The selected terms.
     */
    public List<String>  getTerms() { return _terms; }


    /**
     * Mutator.
     *
     * @param terms The terms to set.
     */
    public void setTerms(final List<String> terms) {
        _terms = terms;
        if (null!=_terms) {
            setValue(_terms);
        } else {
            setValue(null);
        }
    }


    /**
     * Selector window for the taxonomy term selection.
     *
     * @author Civic Computing Ltd.
     */
    public class TaxonomySelector extends Window {
        private final ContentPanel _cp = new ContentPanel();
        private final TreeStore<ModelData> _store = new TreeStore<ModelData>();
        private final TreePanel<ModelData> _tree;

        private static final int DIALOG_HEIGHT = 400;
        private static final int DIALOG_WIDTH = 400;
        /**
         * Constructor.
         *
         * @param vocabularyXML The XML for the selector.
         */
        public TaxonomySelector(final String vocabularyXML) {
            setModal(true);
            setBodyStyle("backgroundColor: white;");
            setHeading(I18n.uiConstants.selectTerms());
            setWidth(DIALOG_WIDTH);
            setHeight(DIALOG_HEIGHT);
            setMinWidth(Globals.MIN_WIDTH);
            setLayout(new FitLayout());

            parseXMLtoModel(vocabularyXML);
            _tree = new TreePanel<ModelData>(_store);
            _tree.setCheckable(true);
            _tree.setAutoLoad(true);
            _tree.setDisplayProperty("name");
            _store.setKeyProvider(new ModelKeyProvider<ModelData>() {
                public String getKey(final ModelData model) {
                    return model.get("id");
                }
            });
            _cp.setScrollMode(Scroll.AUTOY);
            _cp.add(_tree);

            final Button save = new Button(
                I18n.uiConstants.save(),
                new SelectionListener<ButtonEvent>() {
                    @Override public void componentSelected(
                                                         final ButtonEvent ce) {
                        hide();
                    }
                }
            );

            add(_cp);
            addButton(save);

            addListener(Events.Hide,
                new Listener<ComponentEvent>() {
                @Override
                public void handleEvent(final ComponentEvent be) {
                    final List<String> terms = new ArrayList<String>();

                    for (final ModelData item : _tree.getCheckedSelection()) {
                        terms.add((String) item.get("id"));
                      }
                    setTerms(terms);
                }
            });
        }


        /**
         * Toggles selected tree items.
         *
         * @param list The list of selected terms.
         */
        public void checkSelected(final List<String> list) {
            if (null == list) {
                return;
            }
            for (final ModelData item : _store.getAllItems()) {
                for (final String term : list) {
                    if (((String) item.get("id")).equals(term)) {
                        _tree.setChecked(item, true);
                    }
                }
            }
        }


        private void parseXMLtoModel(final String vocabularyXML) {

            final List<ModelData> models = new ArrayList<ModelData>();

            final Document def = XMLParser.parse(vocabularyXML);
            Node n = def.getFirstChild().getFirstChild();

            while (n != null) {
                final TreeModel tm =  handle(n);
                if (tm != null) {
                    models.add(tm);
                }
                n = n.getNextSibling();
            }
            _store.add(models, true);
        }


        private TreeModel handle(final Node n) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                final Element f = (Element) n;
                final String id          = f.getAttribute("id");
                final String title       = f.getAttribute("title");

                final TreeModel model = new BaseTreeModel();
                model.set("id", id);
                model.set("name", title);
                if (f.hasChildNodes()) {
                    final NodeList fields = f.getChildNodes();
                    for (int i=0; i<fields.getLength(); i++) {
                        final Node childTerm = fields.item(i);

                        final TreeModel tm =  handle(childTerm);
                        if (tm != null) {
                            model.add(tm);
                        }
                     }
                }
                return model;
            }
            return null;
        }
    }
}
