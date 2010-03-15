
package ccc.contentcreator.widgets;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Base class for user interface trees.
 *
 * @author Civic Computing Ltd.
 */
public abstract class Tree {

    /**
     * Icon provider.
     *
     * @author Civic Computing Ltd.
     */
    final class ModelIconProviderImplementation
        implements ModelIconProvider<ModelData> {

        /** {@inheritDoc} */
        public AbstractImagePrototype getIcon(final ModelData model) {
            if (model.get("icon") != null) {
                return IconHelper.createPath((String) model.get("icon"));
            }
            return null;
        }
    }

    /** NULL_ICON_PATH : String. */
    protected static final String NULL_ICON_PATH = null;
    /** BACKGROUND_COLOUR : String. */
    protected static final String BACKGROUND_COLOUR = "white";
    /** BACKGROUND_ATTRIBUTE : String. */
    protected static final String BACKGROUND_ATTRIBUTE = "background";
    /** IS_LEAF : boolean. */
    protected static final boolean IS_LEAF = true;
    /** IS_NOT_LEAF : boolean. */
    protected static final boolean IS_NOT_LEAF = false;
    /** EXPANDED : boolean. */
    protected static final boolean EXPANDED = true;
    /** NOT_EXPANDED : boolean. */
    protected static final boolean NOT_EXPANDED = false;
    /** ADD_ALL_CHILDREN : boolean. */
    protected static final boolean ADD_ALL_CHILDREN = true;
    /** DONT_ADD_CHILDREN : boolean. */
    protected static final boolean DONT_ADD_CHILDREN = false;

    /** _store : TreeStore<ModelData>. */
    protected final TreeStore<ModelData> _store = new TreeStore<ModelData>();
    /** _tree : TreePanel<ModelData>. */
    protected final TreePanel<ModelData> _tree =
        new TreePanel<ModelData>(_store);

    /**
     * Constructor.
     *
     */
    public Tree() {
        super();
    }

    /**
     * Accessor.
     *
     * @return The tree.
     */
    public TreePanel<ModelData> getTree() {
        return _tree;
    }

    /**
     * Creates BaseModelData object for a tree with null icon path.
     *
     * @param name Name of the model.
     * @param id Id of the model.
     * @return The ModelData object with set values.
     */
    protected ModelData getNewItem(final String name, final String id) {
        return getNewItem(name, id, NULL_ICON_PATH);
    }

    /**
     * Creates BaseModelData object for a tree.
     *
     * @param name Name of the model.
     * @param id Id of the model.
     * @param iconPath Path for the icon.
     * @return The ModelData object with set values.
     */
    protected ModelData getNewItem(final String name,
                                   final String id,
                                   final String iconPath) {

        final ModelData baseModelData = new BaseModelData();

        baseModelData.set("name", name);
        baseModelData.set("id", id);

        if (iconPath != null) {
            baseModelData.set("icon", iconPath);
        }

        return baseModelData;
    }

    /**
     * Add tree to parent panel.
     *
     */
    public abstract void showTable();
}
