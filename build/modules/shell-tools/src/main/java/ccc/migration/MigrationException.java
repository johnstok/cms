package ccc.migration;

import ccc.domain.CCCException;



/**
 * Migration specific runtime exception.
 *
 * @author Civic Computing Ltd
 */
public class MigrationException extends CCCException {

    /**
     * Constructor.
     */
    public MigrationException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message The message.
     * @param cause The cause.
     */
    public MigrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message The message.
     */
    public MigrationException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The cause.
     */
    public MigrationException(final Throwable cause) {
        super(cause);
    }

}
