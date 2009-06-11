package ccc.migration;


/**
 * Paragraph bean for migration.
 *
 * @author Civic Computing Ltd
 */
public class ParagraphBean {
    private final String _key;
    private final String _text;

    /**
     * Constructor.
     *
     * @param key Key of the paragraph
     * @param text Text of the paragraph
     */
    public ParagraphBean(final String key, final String text) {
        _key = key;
        _text = text;
    }


    /**
     * Accessor for key.
     *
     * @return Key of the paragraph
     */
    public String key() {
        return _key;
    }


    /**
     * Accessor for text.
     *
     * @return Text of the paragraph
     */
    public String text() {
        return _text;
    }

}
