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
package ccc.services.ejb3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ccc.domain.Action;
import ccc.domain.Alias;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.Revision;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.dto.UserDto;
import ccc.types.CommandType;
import ccc.types.ResourceType;


/**
 * Helper class for translating between the core domain and DTOs.
 * TODO: These methods could now be folded into the various CCC resource
 *  classes?
 *
 * @author Civic Computing Ltd.
 */
public class ModelTranslation {

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
                             final Map<Integer, ? extends Revision> revisions) {
        final Collection<RevisionDto> mapped =
            new ArrayList<RevisionDto>();
        for (final Map.Entry<Integer, ? extends Revision> rev : revisions.entrySet()) {
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
                             final Map.Entry<Integer, ? extends Revision> rev) {
        return
            new RevisionDto(
                CommandType.PAGE_UPDATE,
                rev.getValue().getActor().username(),
                rev.getValue().getTimestamp(),
                rev.getKey(),
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
                user.roles());
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
