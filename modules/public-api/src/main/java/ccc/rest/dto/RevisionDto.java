/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.dto;

import java.io.Serializable;
import java.util.Date;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.CommandType;
import ccc.types.Username;


/**
 * A summary of a log entry.
 *
 * @author Civic Computing Ltd.
 */
public final class RevisionDto implements Serializable, Jsonable {
    private CommandType _command;
    private Username _actorUsername;
    private Date _happenedOn;
    private long _index;
    private String _comment;
    private boolean _isMajor;

    @SuppressWarnings("unused") private RevisionDto() { super(); }

    /**
     * Constructor.
     *
     * @param command The action.
     * @param actorUsername The action's actor.
     * @param on The date of the action.
     * @param index The index of the action.
     * @param comment The comment of the action.
     * @param isMajorEdit Is the action a major edit.
     */
    public RevisionDto(final CommandType  command,
                           final Username  actorUsername,
                           final Date    on,
                           final long    index,
                           final String comment,
                           final boolean isMajorEdit) {
        _command = command;
        _actorUsername = actorUsername;
        _happenedOn = new Date(on.getTime());
        _index = index;
        _comment = comment;
        _isMajor = isMajorEdit;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation for this class.
     */
    public RevisionDto(final Json json) {
        this(
            CommandType.valueOf(json.getString(JsonKeys.COMMAND)),
            new Username(json.getString(JsonKeys.USERNAME)),
            json.getDate(JsonKeys.HAPPENED_ON),
            json.getLong(JsonKeys.INDEX).longValue(),
            json.getString(JsonKeys.COMMENT),
            json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue()
        );
    }

    /**
     * Accessor.
     *
     * @return Returns the action.
     */
    public CommandType getCommand() {
        return _command;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public Username getActorUsername() {
        return _actorUsername;
    }


    /**
     * Accessor.
     *
     * @return Returns the happenedOn.
     */
    public Date getHappenedOn() {
        return new Date(_happenedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the index.
     */
    public long getIndex() {
        return _index;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public String getComment() {
        return _comment;
    }


    /**
     * Accessor.
     *
     * @return Returns the isMajor.
     */
    public boolean isMajor() {
        return _isMajor;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.COMMAND, getCommand().name());
        json.set(JsonKeys.USERNAME, getActorUsername().toString());
        json.set(JsonKeys.HAPPENED_ON, getHappenedOn());
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.valueOf(isMajor()));
        json.set(JsonKeys.INDEX, Long.valueOf(getIndex()));
        json.set(JsonKeys.COMMENT, getComment());
    }
}