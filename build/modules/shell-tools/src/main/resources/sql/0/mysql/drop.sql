alter table action_fail_params drop foreign key FK_ACTION_FPARAM_ACTION_ID;

alter table action_params drop foreign key FK_ACTION_PARAM_ACTION_ID;

alter table actions drop foreign key FK_ACTION_RESOURCE_SUBJECT_ID;

alter table actions drop foreign key FK_ACTION_USER_ACTOR_ID;

alter table aliases drop foreign key FK_ALIAS_RESOURCE_ID;

alter table aliases drop foreign key FK_ALIAS_RESOURCE_TARGET_ID;

alter table file_revision_properties drop foreign key FK_FILEREVPROPS_FILEREV_ID;

alter table file_revisions drop foreign key FK_REVISION_FILE_ID;

alter table file_revisions drop foreign key FK_FILE_REV_USER_ACTOR_ID;

alter table file_wc_properties drop foreign key FK_FILEWCPROPS_FILEWC_ID;

alter table file_wcopies drop foreign key FK_WCOPY_FILE_ID;

alter table files drop foreign key FK_FILE_RESOURCE_ID;

alter table folders drop foreign key FK_FOLDER_RESOURCE_ID;

alter table folders drop foreign key FK_FOLDER_INDEX_PAGE_ID;

alter table logentries drop foreign key FK_LOGENTRY_USER_ACTOR_ID;

alter table page_revision_paragraphs drop foreign key FK_PARAGRAPH_REVISION_ID;

alter table page_revisions drop foreign key FK_PAGE_REV_USER_ACTOR_ID;

alter table page_revisions drop foreign key FK_REVISION_PAGE_ID;

alter table page_wcopies drop foreign key FK_WCOPY_PAGE_ID;

alter table page_wcopy_paragraphs drop foreign key FK_PARAGRAPH_WCOPY_ID;

alter table pages drop foreign key FK_PAGE_RESOURCE_ID;

alter table resource_metadata drop foreign key FK_RESMETADATA_RESOURCE_ID;

alter table resource_roles drop foreign key FK_RESROLES_RESOURCE_ID;

alter table resource_tags drop foreign key FK_RESTAGS_RESOURCE_ID;

alter table resources drop foreign key FK_RESOURCE_USER_CHANGED_ID;

alter table resources drop foreign key FK_RESOURCE_USER_PUBLISHED_ID;

alter table resources drop foreign key FK_RESOURCE_USER_CREATED_ID;

alter table resources drop foreign key FK_RESOURCE_TEMPLATE_ID;

alter table resources drop foreign key FK_RESOURCE_FOLDER_PARENT_ID;

alter table resources drop foreign key FK_RESOURCE_USER_LOCKED_ID;

alter table searches drop foreign key FK_SEARCH_RESOURCE_ID;

alter table template_revisions drop foreign key FK_TEMPLATE_REV_USER_ACTOR_ID;

alter table template_revisions drop foreign key FK_REVISION_TEMPLATE_ID;

alter table templates drop foreign key FK_TEMPLATE_RESOURCE_ID;

alter table user_metadata drop foreign key FK_USERMETADATA_USER_ID;

alter table user_roles drop foreign key FK_USERROLES_USER_ID;

drop table if exists action_fail_params;

drop table if exists action_params;

drop table if exists actions;

drop table if exists aliases;

drop table if exists file_revision_properties;

drop table if exists file_revisions;

drop table if exists file_wc_properties;

drop table if exists file_wcopies;

drop table if exists files;

drop table if exists folders;

drop table if exists logentries;

drop table if exists page_revision_paragraphs;

drop table if exists page_revisions;

drop table if exists page_wcopies;

drop table if exists page_wcopy_paragraphs;

drop table if exists pages;

drop table if exists resource_metadata;

drop table if exists resource_roles;

drop table if exists resource_tags;

drop table if exists resources;

drop table if exists searches;

drop table if exists settings;

drop table if exists template_revisions;

drop table if exists templates;

drop table if exists user_metadata;

drop table if exists user_roles;

drop table if exists users;
