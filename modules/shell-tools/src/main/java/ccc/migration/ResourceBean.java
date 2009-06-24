package ccc.migration;

import ccc.commons.WordCharFixer;


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
    private final boolean _published;
    private final int _legacyVersion;
    private final boolean _secure;
    private final String _title;
    private final String _useInIndex;

    /**
     * Constructor.
     *
     * @param contentId contentId of resource
     * @param type type of resource
     * @param name name of resource
     * @param displayTemplate displayTemplate of resource
     * @param published published true for published resource
     * @param legacyVersion Legacy version id of the resource
     * @param isSecure Does the resource have security roles associated.
     * @param title The title for the resource
     * @param useInIndex The value of use_in_index.
     */
    public ResourceBean(final int contentId,
                        final String type,
                        final String name,
                        final String displayTemplate,
                        final boolean published,
                        final int legacyVersion,
                        final boolean isSecure,
                        final String title,
                        final String useInIndex) {
        _contentId = contentId;
        _displayTemplate = displayTemplate;
        _name = name;
        _type = type;
        _published = published;
        _legacyVersion = legacyVersion;
        _secure = isSecure;
        _title = title;
        _useInIndex = useInIndex;
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

    /**
     * Accessor method for boolean value of published status.
     *
     * @return True if resource has status PUBLISHED
     */
    public boolean isPublished() {
        return _published;
    }

    /**
     * Accessor method for legacyVersion.
     *
     * @return Legacy version id of the resource
     */
    public int legacyVersion() {
        return _legacyVersion;
    }

    /**
     * Accessor.
     *
     * @return true if there are security constraints for this resource, false
     * otherwise.
     */
    public boolean isSecure() {
        return _secure;
    }

    /**
     * Accessor.
     *
     * @return The title as a string, may be null.
     */
    public String title() {
        return _title;
    }

    /**
     * Accessor.
     *
     * @return The useInIndex as a string, may be null.
     */
    public String useInIndex() {
        return _useInIndex;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String cleanTitle() {
        final String cleanTitle =  (null==title()) ? name() : title();
        return new WordCharFixer().fix(cleanTitle);
    }
}
