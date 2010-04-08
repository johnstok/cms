/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
import java.util.Properties;

import ccc.commons.Resources;
import ccc.domain.User;
import ccc.persistence.CommentRepository;
import ccc.persistence.DataRepository;
import ccc.persistence.GroupRepository;
import ccc.persistence.IRepositoryFactory;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.UserRepository;
import ccc.plugins.PluginFactory;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.Script;
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
    private final UserRepository _userRepo;
    private final DataRepository _data;
    private final GroupRepository _groups;
    private final CommentRepository _comments;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     * @param audit The audit logger, for logging business actions.
     * @param userRepo The repository for users.
     * @param data The data repository for storing binary data.
     */
    public Command(final ResourceRepository repository,
                   final LogEntryRepository audit,
                   final UserRepository userRepo,
                   final DataRepository data) {
        // TODO: Test for NULL here?
        _repository = repository;
        _audit = audit;
        _userRepo = userRepo;
        _data = data;
        _groups = null;
        _comments = null;
    }

    /**
     * Constructor.
     *
     * @param repoFactory The repository factory for this command.
     */
    public Command(final IRepositoryFactory repoFactory) {
        _repository = repoFactory.createResourceRepository();
        _audit      = repoFactory.createLogEntryRepo();
        _userRepo   = repoFactory.createUserRepo();
        _data       = repoFactory.createDataRepository();
        _groups     = repoFactory.createGroupRepo();
        _comments   = repoFactory.createCommentRepo();
    }

    /**
     * Execute the command.
     *
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return The result of the command, of type T.
     */
    public final T execute(final User actor, final Date happenedOn) {
        validate();
        authorize(actor);
        beforeExecute(actor, happenedOn);
//        LOG.info(
//            "Trying command "+getType()+" for actor "+actor.username()+".");
        final T result = doExecute(actor, happenedOn);
//        LOG.info("Completed command.");
        afterExecute(actor, happenedOn, result);
        return result;
    }


    /**
     * Validate the command's inputs.
     */
    protected void validate() { /* NO OP */ }


    /**
     * Confirm that the actor may execute this command.
     * <p>The default implementation allows any actor to execute the command.
     *
     * @param actor The actor performing the command.
     */
    @SuppressWarnings("unused")
    protected void authorize(final User actor) { /* NO OP */ }

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

        final Context context = new Context();
        context.add("audit", _audit);
        context.add("resources", _repository);
        context.add("actor", actor);
        context.add("happenedOn", happenedOn);
        context.add("command", this);
        context.add("result", result);

        new PluginFactory().createScripting().render(
            new Script(script, "after_"+getType()),
            new PrintWriter(System.out),
            context);
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

        final Context context = new Context();
        context.add("audit", _audit);
        context.add("resources", _repository);
        context.add("actor", actor);
        context.add("happenedOn", happenedOn);
        context.add("command", this);

        new PluginFactory().createScripting().render(
            new Script(script, "before_"+getType()),
            new PrintWriter(System.out),
            context);
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
     * @return Returns the resource repository.
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
     * Accessor.
     *
     * @return Returns the user repository.
     */
    public UserRepository getUsers() {
        return _userRepo;
    }


    /**
     * Accessor.
     *
     * @return Returns the data repository.
     */
    public DataRepository getData() {
        return _data;
    }


    /**
     * Accessor.
     *
     * @return Returns the groups repository.
     */
    protected GroupRepository getGroups() {
        return _groups;
    }


    /**
     * Accessor.
     *
     * @return Return the comments repository.
     */
    protected CommentRepository getComments() {
        return _comments;
    }


    /**
     * Execute the command.
     *
     * @param actor The user who performed the command.
     * @param happenedOn When the command was performed.
     *
     * @return The result of the command, of type T.
     */
    protected abstract T doExecute(final User actor, final Date happenedOn);


    /**
     * Get the type of this command.
     *
     * @return The type as an enum.
     */
    protected abstract CommandType getType();
}
