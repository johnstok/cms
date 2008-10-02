package ccc.migration;



/**
 * Migration specific runtime exception.
 *
 * @author Civic Computing Ltd
 */
public class MigrationException extends RuntimeException {

    /**
     * Constructor.
     */
    public MigrationException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     */
    public MigrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message
     */
    public MigrationException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause
     */
    public MigrationException(final Throwable cause) {
        super(cause);
    }

}
