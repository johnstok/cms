DROP SEQUENCE log_entry_index;

alter table action_fail_params drop constraint FK_ACTION_FPARAM_ACTION_ID;

alter table action_params drop constraint FK_ACTION_PARAM_ACTION_ID;

alter table actions drop constraint FK_ACTION_RESOURCE_SUBJECT_ID;

alter table actions drop constraint FK_ACTION_USER_ACTOR_ID;

alter table aliases drop constraint FK_ALIAS_RESOURCE_ID;

alter table aliases drop constraint FK_ALIAS_RESOURCE_TARGET_ID;

alter table file_revision_properties drop constraint FK_FILEREVPROPS_FILEREV_ID;

alter table file_revisions drop constraint FK_REVISION_FILE_ID;

alter table file_revisions drop constraint FK_FILE_REV_USER_ACTOR_ID;

alter table file_wc_properties drop constraint FK_FILEWCPROPS_FILEWC_ID;

alter table file_wcopies drop constraint FK_WCOPY_FILE_ID;

alter table files drop constraint FK_FILE_RESOURCE_ID;

alter table folders drop constraint FK_FOLDER_RESOURCE_ID;

alter table folders drop constraint FK_FOLDER_INDEX_PAGE_ID;

alter table logentries drop constraint FK_LOGENTRY_USER_ACTOR_ID;

alter table page_revision_paragraphs drop constraint FK_PARAGRAPH_REVISION_ID;

alter table page_revisions drop constraint FK_PAGE_REV_USER_ACTOR_ID;

alter table page_revisions drop constraint FK_REVISION_PAGE_ID;

alter table page_wcopies drop constraint FK_WCOPY_PAGE_ID;

alter table page_wcopy_paragraphs drop constraint FK_PARAGRAPH_WCOPY_ID;

alter table pages drop constraint FK_PAGE_RESOURCE_ID;

alter table resource_metadata drop constraint FK_RESMETADATA_RESOURCE_ID;

alter table resource_roles drop constraint FK_RESROLES_RESOURCE_ID;

alter table resource_tags drop constraint FK_RESTAGS_RESOURCE_ID;

alter table resources drop constraint FK_RESOURCE_USER_CHANGED_ID;

alter table resources drop constraint FK_RESOURCE_USER_PUBLISHED_ID;

alter table resources drop constraint FK_RESOURCE_USER_CREATED_ID;

alter table resources drop constraint FK_RESOURCE_TEMPLATE_ID;

alter table resources drop constraint FK_RESOURCE_FOLDER_PARENT_ID;

alter table resources drop constraint FK_RESOURCE_USER_LOCKED_ID;

alter table searches drop constraint FK_SEARCH_RESOURCE_ID;

alter table template_revisions drop constraint FK_TEMPLATE_REV_USER_ACTOR_ID;

alter table template_revisions drop constraint FK_REVISION_TEMPLATE_ID;

alter table templates drop constraint FK_TEMPLATE_RESOURCE_ID;

alter table user_metadata drop constraint FK_USERMETADATA_USER_ID;

alter table user_roles drop constraint FK_USERROLES_USER_ID;

drop table action_fail_params if exists;

drop table action_params if exists;

drop table actions if exists;

drop table aliases if exists;

drop table file_revision_properties if exists;

drop table file_revisions if exists;

drop table file_wc_properties if exists;

drop table file_wcopies if exists;

drop table files if exists;

drop table folders if exists;

drop table logentries if exists;

drop table page_revision_paragraphs if exists;

drop table page_revisions if exists;

drop table page_wcopies if exists;

drop table page_wcopy_paragraphs if exists;

drop table pages if exists;

drop table resource_metadata if exists;

drop table resource_roles if exists;

drop table resource_tags if exists;

drop table resources if exists;

drop table searches if exists;

drop table settings if exists;

drop table template_revisions if exists;

drop table templates if exists;

drop table user_metadata if exists;

drop table user_roles if exists;

drop table users if exists;
