/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.script.ScriptException;

import ccc.commons.Resources;
import ccc.commons.ScriptRunner;
import ccc.domain.CccCheckedException;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.CommandType;


/**
 * Provides the basic contract for command execution.
 *
 * @param <T> The result type of the command.
 *
 * @author Civic Computing Ltd.
 */
public abstract class Command<T> {

    private final ResourceRepository _repository;
    private final LogEntryRepository _audit;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     */
    public Command(final ResourceRepository repository,
                   final LogEntryRepository audit) {
        _repository = repository;
        _audit = audit;
    }

    /**
     * Execute the command.
     *
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return The result of the command, of type T.
     *
     * @throws CccCheckedException If the command fails.
     */
    public final T execute(final User actor,
                           final Date happenedOn) throws CccCheckedException {
        beforeExecute(actor, happenedOn);
        final T result = doExecute(actor, happenedOn);
        afterExecute(actor, happenedOn, result);
        return result;
    }


    /**
     * Event handler that executes after the command has executed successfully.
     *
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     * @param result The result of command execution.
     */
    protected void afterExecute(final User actor,
                                final Date happenedOn,
                                final T result) {

        final String script = getAfterScript();
        if (null==script) { return; }

        final Map<String, Object> context = new HashMap<String, Object>();
        context.put("audit", _audit);
        context.put("resources", _repository);
        context.put("actor", actor);
        context.put("happenedOn", happenedOn);
        context.put("command", this);
        context.put("result", result);

        try {
            new ScriptRunner().eval(
                script, context, new PrintWriter(System.out));
        } catch (final ScriptException e) {
            // FIXME Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    /**
     * Event handler that executes before the command has executed.
     *
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     */
    protected void beforeExecute(final User actor,
                                 final Date happenedOn) {

        final String script = getBeforeScript();
        if (null==script) { return; }

        final Map<String, Object> context = new HashMap<String, Object>();
        context.put("audit", _audit);
        context.put("resources", _repository);
        context.put("actor", actor);
        context.put("happenedOn", happenedOn);
        context.put("command", this);

        try {
            new ScriptRunner().eval(
                script, context, new PrintWriter(System.out));
        } catch (final ScriptException e) {
            // FIXME Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    /**
     * Retrieve the script for the 'afterExecute' event.
     *
     * @return The script as a string.
     */
    private String getAfterScript() {
        final Properties events =
            Resources.readIntoProps("ccc/events.after.properties");
        return events.getProperty(getType().name());
    }


    /**
     * Retrieve the script for the 'beforeExecute' event.
     *
     * @return The script as a string.
     */
    private String getBeforeScript() {
        final Properties events =
            Resources.readIntoProps("ccc/events.before.properties");
        return events.getProperty(getType().name());
    }


    /**
     * Accessor.
     *
     * @return Returns the repository.
     */
    protected ResourceRepository getRepository() {
        return _repository;
    }


    /**
     * Accessor.
     *
     * @return Returns the audit log.
     */
    protected LogEntryRepository getAudit() {
        return _audit;
    }


    /**
     * Execute the command.
     *
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return The result of the command, of type T.
     *
     * @throws CccCheckedException If the command fails.
     */
    protected abstract T doExecute(final User actor,
                                   final Date happenedOn)
                                                    throws CccCheckedException;


    /**
     * Get the type of this command.
     *
     * @return The type as an enum.
     */
    protected abstract CommandType getType();
}
