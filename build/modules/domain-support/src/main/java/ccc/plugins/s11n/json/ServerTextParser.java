package ccc.plugins.s11n.json;

import java.util.Map;

import ccc.plugins.s11n.TextParser;

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
    public boolean parseBoolean(final String text) {
        return Boolean.valueOf(text).booleanValue();
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> parseMapString(final String text) {
        final Json json = parseJson(text);
        return json.getStringMap("properties");
    }


    /** {@inheritDoc} */
    @Override
    public Json newJson() { return new JsonImpl(); }


    /** {@inheritDoc} */
    @Override
    public Json parseJson(final Map<String, String> map) {
        return new JsonImpl(map);
    }
}
