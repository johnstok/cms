package ccc.client.gwt.binding;

import java.util.Collection;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * JSON object for internal use.
 *
 * @author Civic Computing Ltd.
 */
final class JsonObject
    extends
        JavaScriptObject  {
    // TODO: Solve boxing of Javascript numbers and booleans.

    protected JsonObject() { /* NO OP */ }


    static native JsonObject create(final String jsonString) /*-{
        return eval('(' + jsonString + ')');
    }-*/;


    static native JsArray<JsonObject> array(final String s) /*-{
        return eval('(' + s + ')');
    }-*/;


    native <X> X get(final String property) /*-{
        return this[property];
    }-*/;


    Map<String, Object> getProperties() {
        throw new UnsupportedOperationException("Not supported.");
    }


    Collection<String> getPropertyNames() {
        throw new UnsupportedOperationException("Not supported.");
    }


    native <X> X remove(final String property) /*-{
        delete this[property];
    }-*/;


    native <X> X set(final String property, final X v) /*-{
        this[property] = v;
    }-*/;
}