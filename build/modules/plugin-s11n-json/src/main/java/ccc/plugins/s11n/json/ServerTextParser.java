package ccc.plugins.s11n.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Server implementation of the {@link TextParser} API.
 *
 * @author Civic Computing Ltd.
 */
public class ServerTextParser
    implements
        TextParser {


    /** {@inheritDoc} */
    @Override
    public Json parseJson(final String text) {
        return new JsonImpl(text);
    }


    /** {@inheritDoc} */
    @Override
    public Json newJson() { return new JsonImpl(); }


    /**
     * Parse a string map into a JSON object.
     *
     * @param map The map to parse.
     *
     * @return The corresponding JSON object.
     */
    public Json parseJson(final Map<String, String> map) {
        return new JsonImpl(map);
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> parseStringMap(final String text) {
        try {
            final JSONObject o = new JSONObject(text);
            final Map<String, String> stringMap = new HashMap<String, String>();
            for (final Iterator<String> i = o.keys(); i.hasNext();) {
                final String mapKey = i.next();
                final String mapValue = (String) o.get(mapKey);
                stringMap.put(mapKey, mapValue);
            }
            return stringMap;
        } catch (final JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
