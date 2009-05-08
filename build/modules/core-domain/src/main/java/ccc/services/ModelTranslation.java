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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.actions.Action;
import ccc.domain.Alias;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceType;
import ccc.domain.Snapshot;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.api.ActionSummary;
import ccc.services.api.AliasDelta;
import ccc.services.api.Decimal;
import ccc.services.api.FileDelta;
import ccc.services.api.FileSummary;
import ccc.services.api.ID;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;


/**
 * Helper class for translating between the core domain and DTOs.
 *
 * @author Civic Computing Ltd.
 */
public class ModelTranslation {

    /**
     * Map a collection of Folder to a collection of FolderSummary.
     *
     * @param folders The collection of folders to map.
     * @return The corresponding collection of FolderSummary.
     */
    protected Collection<ResourceSummary> mapFolders(
                                             final Collection<Folder> folders) {
        final Collection<ResourceSummary> mapped =
            new ArrayList<ResourceSummary>();
        for (final Folder f : folders) {
            mapped.add(map(f));
        }
        return mapped;
    }

    /**
     * Map a collection of Resource to a collection of ResourceSummary.
     *
     * @param resources The collection of resources to map.
     * @return The corresponding collection of ResourceSummary.
     */
    protected Collection<ResourceSummary> mapResources(
                                         final Collection<Resource> resources) {
        final Collection<ResourceSummary> mapped =
            new ArrayList<ResourceSummary>();
        for (final Resource r : resources) {
            mapped.add(map(r));
        }
        return mapped;
    }


    /**
     * Create summaries for a list of users.
     *
     * @param users The users.
     * @return The corresponding summaries.
     */
    protected Collection<UserSummary> mapUsers(final Collection<User> users) {
        final Collection<UserSummary> mapped = new ArrayList<UserSummary>();
        for (final User u : users) {
            mapped.add(map(u));
        }
        return mapped;
    }


    /**
     * Create summaries for a collection of files.
     *
     * @param files The files.
     * @return The corresponding summaries.
     */
    protected Collection<FileSummary> mapFiles(final Collection<File> files) {
        final Collection<FileSummary> mapped = new ArrayList<FileSummary>();
        for (final File f : files) {
            mapped.add(map(f));
        }
        return mapped;
    }


    /**
     * Create summaries for a collection of log entries.
     *
     * @param logEntries The log entries.
     * @return The corresponding summaries.
     */
    protected Collection<LogEntrySummary> mapLogEntries(
                                              final List<LogEntry> logEntries) {
        final Collection<LogEntrySummary> mapped =
            new ArrayList<LogEntrySummary>();
        for (final LogEntry le : logEntries) {
            mapped.add(map(le));
        }
        return mapped;
    }


    /**
     * Create a summary for a log entry.
     *
     * @param le The log entry.
     * @return A corresponding summary.
     */
    protected LogEntrySummary map(final LogEntry le) {
        return
            new LogEntrySummary(
                le.subjectId().toString(),
                le.action(),
                le.actor().username(),
                le.happenedOn(),
                le.comment(),
                le.isMajorEdit(),
                le.index());
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param r
     * @return
     */
    public ResourceSummary map(final Resource r) {
        int childCount = 0;
        int folderCount = 0;
        String sortOrder = null;
        boolean hasWorkingCopy = false;
        if (r.type() == ResourceType.FOLDER) {
            childCount = r.as(Folder.class).entries().size();
            folderCount = r.as(Folder.class).folders().size();
            sortOrder = r.as(Folder.class).sortOrder().name();
        } else if (r.type() == ResourceType.PAGE) {
            hasWorkingCopy = (r.as(Page.class).workingCopy() != null);
        } else if (r.type() == ResourceType.FILE) {
            hasWorkingCopy = (r.as(File.class).workingCopy() != null);
        }

        final ResourceSummary rs =
            new ResourceSummary(
                toID(r.id()),
                (null==r.parent()) ? null : toID(r.parent().id()),
                r.name().toString(),
                (r.isPublished()) ? r.publishedBy().username() : null,
                r.title(),
                (r.isLocked()) ? r.lockedBy().username() : null,
                r.type().name(),
                childCount,
                folderCount,
                r.includeInMainMenu(),
                sortOrder,
                hasWorkingCopy,
                r.dateCreated(),
                r.dateChanged()
            );
        return rs;
    }


    /**
     * Create a summary for a user.
     *
     * @param user The user.
     * @return A corresponding summary.
     */
    protected UserSummary map(final User user) {
        return
            new UserSummary(
                user.email().getText(),
                toID(user.id()),
                user.username(),
                user.roles());
    }


    /**
     * Create a summary of a file.
     *
     * @param file The file to map.
     * @return The summary of the file.
     */
    protected FileSummary map(final File file) {
        final FileSummary fs =
            new FileSummary(
                file.mimeType().toString(),
                file.absolutePath().toString(),
                toID(file.id()),
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
    protected TemplateDelta delta(final Template template) {
        if (null==template) {
            return null;
        }
        final TemplateDelta delta = new TemplateDelta(
            toID(template.id()),
            template.name().toString(),
            template.title(),
            template.description(),
            template.body(),
            template.definition()
        );
        return delta;
    }


    /**
     * Create a delta for a user.
     *
     * @param user The user.
     * @return A corresponding summary.
     */
    protected UserDelta delta(final User user) {
        final UserDelta delta =
            new UserDelta(
                toID(user.id()),
                null,
                user.email().getText(),
                user.username(),
                user.roles());
        return delta;
    }


    /**
     * Create a delta for an alias.
     *
     * @param alias The alias.
     * @return A corresponding delta.
     */
    protected AliasDelta delta(final Alias alias) {
        final AliasDelta delta =
            new AliasDelta(
                toID(alias.id()),
                alias.name().toString(),
                alias.target().name().toString(),
                toID(alias.target().id()));
        return delta;
    }


    /**
     * Create a delta for a file.
     *
     * @param file The file.
     * @return A corresponding delta.
     */
    protected FileDelta delta(final File file) {
        final FileDelta delta =
            new FileDelta(
                toID(file.id()),
                file.name().toString(),
                file.title(),
                file.description()
            );
        return delta;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param find
     * @return
     */
    protected PageDelta delta(final Page page) {
        final Template t = page.template();
        final Template ct = page.computeTemplate(null);
        final List<ParagraphDelta> paragraphs = new ArrayList<ParagraphDelta>();
        for (final Paragraph p : page.paragraphs()) {
            final ParagraphDelta pDelta =
                new ParagraphDelta(
                    p.name(),
                    ParagraphDelta.Type.valueOf(p.type().name()),
                    null, // FIXME: What is the raw value?!
                    p.text(),
                    p.date(),
                    new Decimal(p.number().toString()));
            paragraphs.add(pDelta);
        }

        final PageDelta delta =
            new PageDelta(
                toID(page.id()),
                page.name().toString(),
                page.title(),
                (null==t) ? null : t.id().toString(),
                page.tagString(),
                page.isPublished(),
                paragraphs,
                (null==ct) ? null : delta(ct)
            );
        return delta;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param find
     * @return
     */
    protected ResourceDelta delta(final Resource resource) {
        final Template t = resource.template();

        final ResourceDelta delta =
            new ResourceDelta(
                toID(resource.id()),
                resource.name().toString(),
                resource.title(),
                (null==t) ? null : t.id().toString(),
                resource.tagString(),
                resource.isPublished()
            );
        return delta;
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
            mapped.add(delta(t));
        }
        return mapped;
    }


    /**
     * Merge page and its working copy to a page delta.
     *
     * @param page The page to process.
     * @return Merged page delta.
     */
    protected PageDelta workingCopyDelta(final Page page) {
        final PageDelta delta = delta(page);

        if (page.workingCopy() != null) {
            final Snapshot ss = page.workingCopy();
            delta.setTitle(ss.getString("title"));
            final List<ParagraphDelta> paragraphs =
                new ArrayList<ParagraphDelta>();
            for(final Snapshot s : ss.getSnapshots("paragraphs")) {
                final Paragraph p = Paragraph.fromSnapshot(s);
                final ParagraphDelta pDelta =
                    new ParagraphDelta(
                        p.name(),
                        ParagraphDelta.Type.valueOf(p.type().name()),
                        null,
                        p.text(),
                        p.date(),
                        new Decimal(p.number().toString()));
                paragraphs.add(pDelta);
            }
            delta.setParagraphs(paragraphs);
        }
        return delta;
    }


    /**
     * Create summaries for a list of actions.
     *
     * @param actions The actions.
     * @return The corresponding summaries.
     */
    protected Collection<ActionSummary> map(final Collection<Action> actions) {
        final Collection<ActionSummary> summaries =
            new ArrayList<ActionSummary>();
        for (final Action a : actions) {
            summaries.add(map(a));
        }
        return summaries;
    }


    /**
     * Create a summary for an action.
     *
     * @param a The action.
     * @return The corresponding summary.
     */
    protected ActionSummary map(final Action a) {
        final ActionSummary summary =
            new ActionSummary(
                toID(a.id()),
                a.type(),
                a.actor().username(),
                a.executeAfter(),
                a.subject().type().toString(),
                a.subject().absolutePath().toString(),
                a.status().toString()
                );
        return summary;
    }


    private ID toID(final UUID uuid) {
        return new ID(uuid.toString());
    }
}
