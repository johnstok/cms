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
    public Json newJson() { return new JsonImpl(); }


    /** {@inheritDoc} */
    @Override
    public Json parseJson(final Map<String, String> map) {
        return new JsonImpl(map);
    }
}
