
package ccc.contentcreator.client;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class Tree {

    final class ModelIconProviderImplementation implements ModelIconProvider<ModelData> {

        public AbstractImagePrototype getIcon(final ModelData model) {
            if (model.get("icon") != null) {
                return IconHelper.createPath((String) model.get("icon"));
            }
            return null;
        }
    }

    protected static final String NULL_ICON_PATH = null;
    /** TODO read BACKGROUND_COLOUR from a properties file? **/
    protected static final String BACKGROUND_COLOUR = "white";
    protected static final String BACKGROUND_ATTRIBUTE = "background";
    protected static final boolean IS_LEAF = true;
    protected static final boolean IS_NOT_LEAF = false;
    protected static final boolean EXPANDED = true;
    protected static final boolean NOT_EXPANDED = false;
    protected static final boolean ADD_ALL_CHILDREN = true;
    protected static final boolean DONT_ADD_CHILDREN = false;
    
    protected final TreeStore<ModelData> _store = new TreeStore<ModelData>();
    protected final TreePanel<ModelData> _tree = new TreePanel<ModelData>(_store);

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

    protected ModelData getNewItem(final String name, final String id) {
        return getNewItem(name, id, NULL_ICON_PATH);
    }

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
    
    public void showTable() {
    }
}
