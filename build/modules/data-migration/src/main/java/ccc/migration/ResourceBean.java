package ccc.migration;


/**
 * Resource bean for migration.
 *
 * @author Civic Computing Ltd
 */
public class ResourceBean {

    private final int _contentId;
    private final String _type;
    private final String _name;
    private final String _displayTemplate;

    /**
     * Constructor.
     *
     * @param contentId contentId of resource
     * @param type type of resource
     * @param name name of resource
     * @param displayTemplate displayTemplate of resource
     */
    public ResourceBean(final int contentId,
                        final String type,
                        final String name,
                        final String displayTemplate) {
        _contentId = contentId;
        _displayTemplate = displayTemplate;
        _name = name;
        _type = type;
    }


    /**
     * Accessor method for type.
     *
     * @return type of resource
     */
    public String type() {

        return _type;
    }

    /**
     * Accessor method for name.
     *
     * @return name of resource
     */
    public String name() {

        return _name;
    }

    /**
     * Accessor method for displayTemplate.
     *
     * @return displayTemplate of resource
     */
    public String displayTemplate() {

        return _displayTemplate;
    }

    /**
     * Accessor method for contentId.
     *
     * @return contentId of resource
     */
    public int contentId() {

        return _contentId;
    }
}
