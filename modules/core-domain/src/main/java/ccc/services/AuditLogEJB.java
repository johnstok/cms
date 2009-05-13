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
package ccc.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import ccc.commons.DBC;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.User;


/**
 * Implementation of {@link AuditLog}.
 *
 * @author Civic Computing Ltd.
 */
public class AuditLogEJB
    implements
        AuditLog {
    private static final Logger LOG = Logger.getLogger(AuditLogEJB.class);

    private Dao _em;

    /**
     * Constructor.
     * @param em The entity manager used to perform queries.
     */
    public AuditLogEJB(final Dao em) {
        DBC.require().notNull(em);
        _em = em;
    }

    /** {@inheritDoc} */
    @Override
    public void recordLock(final Resource resource,
                           final User actor,
                           final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forLock(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUnlock(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUnlock(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordCreate(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forCreate(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordChangeTemplate(final Resource resource,
                                     final User actor,
                                     final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forTemplateChange(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdate(final Resource resource,
                             final User actor,
                             final Date happenedOn,
                             final String comment,
                             final boolean isMajorEdit) {
        DBC.require().notNull(resource);
        final LogEntry le = LogEntry.forUpdate(
            resource,
            actor,
            happenedOn,
            comment,
            isMajorEdit);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdate(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le = LogEntry.forUpdate(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordMove(final Resource resource,
                           final User actor,
                           final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forMove(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordRename(final Resource resource,
                             final User actor,
                             final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forRename(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordPublish(final Resource resource,
                              final User actor,
                              final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forPublish(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUnpublish(final Resource resource,
                                final User actor,
                                final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUnpublish(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public LogEntry findEntryForIndex(final long index) {
        return _em.find(
            QueryNames.LOG_ENTRY_BY_ID, LogEntry.class, Long.valueOf(index));
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdateTags(final Resource resource,
                                 final User actor,
                                 final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUpdateTags(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdateMetadata(final Resource resource,
                                 final User actor,
                                 final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUpdateMetadata(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordIncludeInMainMenu(final Resource resource,
                                        final User actor,
                                        final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forIncludeInMainMenu(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordRemoveFromMainMenu(final Resource resource,
                                        final User actor,
                                        final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forRemoveFromMainMenu(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordChangeRoles(final Resource resource,
                                  final User actor,
                                  final Date happenedOn) {

        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forChangeRoles(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordReorder(final Folder folder,
                              final User actor,
                              final Date happenedOn) {
        DBC.require().notNull(folder);
        final LogEntry le =
            LogEntry.forReorder(folder, actor, happenedOn);
        _em.create(le);
        log(folder, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdateSortOrder(final Folder folder,
                                      final User actor,
                                      final Date happenedOn) {
        DBC.require().notNull(folder);
        final LogEntry le =
            LogEntry.forUpdateSortOrder(folder, actor, happenedOn);
        _em.create(le);
        log(folder, actor, happenedOn, le);
    }

    /** {@inheritDoc} */
    @Override
    public void recordUpdateCache(final Resource resource,
                                  final User actor,
                                  final Date happenedOn) {
        DBC.require().notNull(resource);
        final LogEntry le =
            LogEntry.forUpdateCache(resource, actor, happenedOn);
        _em.create(le);
        log(resource, actor, happenedOn, le);
    }


    private void log(final Resource resource,
                     final User actor,
                     final Date happenedOn,
                     final LogEntry le) {

        final DateFormat df = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss z");
        LOG.info(
            "Action: " + le.action()
            + " for " + resource.id()
            + ", " + actor.username()
            + " on " + df.format(happenedOn));
    }
}
