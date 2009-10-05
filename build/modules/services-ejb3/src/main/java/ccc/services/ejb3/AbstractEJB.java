/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJBContext;
import javax.ejb.TimerService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.domain.Action;
import ccc.domain.Alias;
import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.ActionRepository;
import ccc.persistence.ActionRepositoryImpl;
import ccc.persistence.DataRepository;
import ccc.persistence.DataRepositoryImpl;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.ResourceRepository;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepository;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.streams.ReadToStringAction;
import ccc.rest.RestException;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.dto.UserDto;
import ccc.types.CommandType;
import ccc.types.ResourceType;


/**
 * Abstract base class for EJBs.
 *
 * @author Civic Computing Ltd.
 */
abstract class AbstractEJB {

    private static Logger log = Logger.getLogger(AbstractEJB.class);

    @javax.annotation.Resource private EJBContext    _context;
    @PersistenceContext        private EntityManager   _em;

    private UserRepository     _users;
    private ResourceRepository _resources;
    private LogEntryRepository _audit;
    private DataRepository     _dm;
    private ActionRepository   _actions;


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _audit = new LogEntryRepositoryImpl(_em);
        _users = new UserRepositoryImpl(_em);
        _resources = new ResourceRepositoryImpl(_em);
        _dm = DataRepositoryImpl.onFileSystem(_em);
        _actions = new ActionRepositoryImpl(_em);
    }


    /**
     * Accessor.
     *
     * @return The timer service for this EJB.
     */
    protected final TimerService getTimerService() {
        return _context.getTimerService();
    }


    /**
     * Accessor.
     *
     * @return Returns the user repository.
     */
    protected final UserRepository getUsers() {
        return _users;
    }


    /**
     * Accessor.
     *
     * @return Returns the resource repository.
     */
    protected final ResourceRepository getResources() {
        return _resources;
    }


    /**
     * Accessor.
     *
     * @return Returns the log entry repository.
     */
    protected final LogEntryRepository getAuditLog() {
        return _audit;
    }


    /**
     * Accessor.
     *
     * @return Returns the file repository.
     */
    protected final DataRepository getFiles() {
        return _dm;
    }


    /**
     * Accessor.
     *
     * @return Returns the action repository.
     */
    protected final ActionRepository getActions() {
        return _actions;
    }


    /**
     * Fail a method throwing an application exception.
     *
     * @param e The checked exception to convert.
     *
     * @return The corresponding application exception.
     */
    protected RestException fail(final CccCheckedException e) {
        _context.setRollbackOnly();  // CRITICAL
        final RestException cfe = e.toRemoteException();
        log.info(
            "Handled local exception: "+cfe.getFailure().getExceptionId(), e);
        return cfe;
    }


    /**
     * Look up the user for the specified ID.
     *
     * @param userId The user's ID.
     *
     * @return The corresponding user.
     *
     * @throws EntityNotFoundException If a corresponding user doesn't exist.
     */
    protected User userForId(final UUID userId) throws EntityNotFoundException {
        final User u = _users.find(userId);
        return u;
    }


    /**
     * Accessor.
     * TODO: Throw 'invalid session exception instead.
     *
     * @throws EntityNotFoundException If no user corresponds to the current
     *  principal.
     *
     * @return The currently logged in user.
     */
    protected User currentUser() throws EntityNotFoundException {
        return _users.loggedInUser(_context.getCallerPrincipal());
    }


    /**
     * Accessor.
     *
     * @throws EntityNotFoundException If no user corresponds to the current
     *  principal.
     *
     * @return The currently logged in user's ID.
     */
    protected UUID currentUserId() throws EntityNotFoundException {
        return currentUser().id();
    }



    /* ====================================================================
     * Model translation.
     * ================================================================== */

    /**
     * Map a collection of Resource to a collection of ResourceSummary.
     *
     * @param resources The collection of resources to map.
     * @return The corresponding collection of ResourceSummary.
     */
    protected Collection<ResourceSummary> mapResources(
                               final Collection<? extends Resource> resources) {
        final Collection<ResourceSummary> mapped =
            new ArrayList<ResourceSummary>();
        for (final Resource r : resources) {
            mapped.add(mapResource(r));
        }
        return mapped;
    }


    /**
     * Create summaries for a list of users.
     *
     * @param users The users.
     * @return The corresponding summaries.
     */
    protected Collection<UserDto> mapUsers(final Collection<User> users) {
        final Collection<UserDto> mapped = new ArrayList<UserDto>();
        for (final User u : users) {
            mapped.add(mapUser(u));
        }
        return mapped;
    }


    /**
     * Create summaries for a collection of files.
     *
     * @param files The files.
     * @return The corresponding summaries.
     */
    protected Collection<FileDto> mapFiles(final Collection<File> files) {
        final Collection<FileDto> mapped = new ArrayList<FileDto>();
        for (final File f : files) {
            mapped.add(mapFile(f));
        }
        return mapped;
    }


    /**
     * Create summaries for a collection of log entries.
     *
     * @param revisions The revisions.
     * @return The corresponding summaries.
     */
    protected Collection<RevisionDto> mapLogEntries(
                         final Map<Integer, ? extends Revision<?>> revisions) {
        final Collection<RevisionDto> mapped =
            new ArrayList<RevisionDto>();
        for (final Map.Entry<Integer, ? extends Revision<?>> rev
            : revisions.entrySet()) {
            mapped.add(mapLogEntry(rev));
        }
        return mapped;
    }


    /**
     * Create summaries for a collection of templates.
     *
     * @param templates The templates.
     * @return The corresponding summaries.
     */
    protected Collection<TemplateSummary> mapTemplates(
                                               final List<Template> templates) {
        final Collection<TemplateSummary> mapped =
            new ArrayList<TemplateSummary>();
        for (final Template t : templates) { mapped.add(mapTemplate(t)); }
        return mapped;
    }


    /**
     * Create a summary for a log entry.
     *
     * @param rev The revision.
     * @return A corresponding summary.
     */
    protected RevisionDto mapLogEntry(
                         final Map.Entry<Integer, ? extends Revision<?>> rev) {
        return
            new RevisionDto(
                CommandType.PAGE_UPDATE,
                rev.getValue().getActor().username(),
                rev.getValue().getTimestamp(),
                rev.getKey().longValue(),
                rev.getValue().getComment(),
                rev.getValue().isMajorChange());
    }


    /**
     * Create a summary for a resource.
     *
     * @param r The CCC resource.
     * @return The corresponding summary.
     */
    public ResourceSummary mapResource(final Resource r) {
        int childCount = 0;
        int folderCount = 0;
        String sortOrder = null;
        UUID indexPage = null;
        boolean hasWorkingCopy = false;
        if (r.type() == ResourceType.FOLDER) {
            childCount = r.as(Folder.class).entries().size();
            folderCount = r.as(Folder.class).folders().size();
            sortOrder = r.as(Folder.class).sortOrder().name();
            indexPage = (null==r.as(Folder.class).indexPage())
                ? null : r.as(Folder.class).indexPage().id();
        } else if (r.type() == ResourceType.PAGE) {
            hasWorkingCopy = (r.as(Page.class).hasWorkingCopy());
        } else if (r.type() == ResourceType.FILE) {
            hasWorkingCopy = (r.as(File.class).hasWorkingCopy());
        }

        final ResourceSummary rs =
            new ResourceSummary(
                r.id(),
                (null==r.parent()) ? null : r.parent().id(),
                r.name().toString(),
                (r.isPublished())
                    ? r.publishedBy().username() : null,
                r.title(),
                (r.isLocked()) ? r.lockedBy().username() : null,
                r.type(),
                childCount,
                folderCount,
                r.includeInMainMenu(),
                sortOrder,
                hasWorkingCopy,
                r.dateCreated(),
                r.dateChanged(),
                (null==r.template()) ? null : r.template().id(),
                r.tagString(),
                r.absolutePath().toString(),
                indexPage,
                r.description()
            );
        return rs;
    }


    /**
     * Create a summary for a user.
     *
     * @param user The user.
     * @return A corresponding summary.
     */
    protected UserDto mapUser(final User user) {
        return
            new UserDto(
                user.email().getText(),
                user.id(),
                user.username(),
                user.roles(),
                user.metadata());
    }


    /**
     * Create a summary of a file.
     *
     * @param file The file to map.
     * @return The summary of the file.
     */
    protected FileDto mapFile(final File file) {
        final FileDto fs =
            new FileDto(
                file.mimeType().toString(),
                file.absolutePath().toString(),
                file.id(),
                file.name().toString(),
                file.title());
        return fs;
    }


    /**
     * Create a summary of a text file.
     *
     * @param file The file to map.
     * @return The summary of the file.
     */
    protected TextFileDelta mapTextFile(final File file) {

        final TextFileDelta fs =
            new TextFileDelta(
                file.id(),
                (!file.isText())
                    ? null : ReadToStringAction.read(_dm, file),
                file.mimeType(),
                file.currentRevision().isMajorChange(),
                file.currentRevision().getComment());
        return fs;
    }


    /**
     * Create a delta for a template.
     *
     * @param template The template.
     * @return A corresponding delta.
     */
    protected TemplateDelta deltaTemplate(final Template template) {
        if (null==template) {
            return null;
        }
        final TemplateDelta delta =
            new TemplateDelta(
                template.body(),
                template.definition(),
                template.mimeType()
        );
        return delta;
    }


    /**
     * Create a delta for a user.
     *
     * @param user The user.
     * @return A corresponding summary.
     */
    protected UserDto deltaUser(final User user) {
        final UserDto delta =
            new UserDto(
                user.email().getText(),
                user.username(),
                user.roles(),
                user.metadata());
        return delta;
    }


    /**
     * Create a delta for an alias.
     *
     * @param alias The alias.
     * @return A corresponding delta.
     */
    protected AliasDelta deltaAlias(final Alias alias) {
        return alias.createSnapshot();
    }


    /**
     * Create a delta for a file.
     *
     * @param file The file.
     * @return A corresponding delta.
     */
    protected FileDelta deltaFile(final File file) {
        return file.getOrCreateWorkingCopy();
    }


    /**
     * Create a delta for a page.
     *
     * @param page The CCC page.
     * @return The corresponding delta.
     */
    protected PageDelta deltaPage(final Page page) {
        return page.getOrCreateWorkingCopy();
    }


    /**
     * Create deltas for a collection of templates.
     *
     * @param templates The templates.
     * @return The corresponding deltas.
     */
    protected Collection<TemplateDelta> deltaTemplates(
                                               final List<Template> templates) {
        final Collection<TemplateDelta> mapped = new ArrayList<TemplateDelta>();
        for (final Template t : templates) {
            mapped.add(deltaTemplate(t));
        }
        return mapped;
    }


    /**
     * Create summaries for a list of actions.
     *
     * @param actions The actions.
     * @return The corresponding summaries.
     */
    protected Collection<ActionSummary> mapActions(
                                             final Collection<Action> actions) {
        final Collection<ActionSummary> summaries =
            new ArrayList<ActionSummary>();
        for (final Action a : actions) {
            summaries.add(mapAction(a));
        }
        return summaries;
    }


    /**
     * Create a summary for an action.
     *
     * @param a The action.
     * @return The corresponding summary.
     */
    protected ActionSummary mapAction(final Action a) {
        final ActionSummary summary =
            new ActionSummary(
                a.id(),
                a.type(),
                a.actor().username(),
                a.executeAfter(),
                a.subject().type(),
                a.subject().absolutePath().toString(),
                a.status(),
                (null==a.getCode()) ? null : a.getCode());
        return summary;
    }


    /**
     * Create a summary for a template.
     *
     * @param t The template.
     * @return The corresponding summary.
     */
    protected TemplateSummary mapTemplate(final Template t) {
        if (t == null) {
            return null;
        }
        return
            new TemplateSummary(
                t.id(),
                t.name().toString(),
                t.title(),
                t.description(),
                t.body(),
                t.definition());
    }
}
