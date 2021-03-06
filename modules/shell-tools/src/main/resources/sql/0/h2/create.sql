CREATE SEQUENCE log_entry_index START WITH 1 INCREMENT BY 1 CACHE 0;

create table action_fail_params (action_id varchar(36) not null, param_value varchar(1024) not null, param_key varchar(255) not null, primary key (action_id, param_key));

create table action_params (action_id varchar(36) not null, param_value varchar(1024) not null, param_key varchar(255) not null, primary key (action_id, param_key));

create table actions (id varchar(36) not null, vn bigint not null, execute_after timestamp not null, command varchar(255) not null, status varchar(255) not null, actor_id varchar(36) not null, subject_id varchar(36) not null, failure_code varchar(255), failure_id varchar(36), primary key (id));

create table aliases (id varchar(36) not null, target_id varchar(36) not null, primary key (id));

create table file_revision_properties (file_revision_id varchar(36) not null, prop_value varchar(1024) not null, prop_key varchar(255) not null, primary key (file_revision_id, prop_key));

create table file_revisions (id varchar(36) not null, vn bigint not null, size_in_bytes integer not null, data_id varchar(36) not null, mime_type_primary varchar(255) not null, mime_type_sub varchar(255) not null, major_change bit not null, actor_comment varchar(1024), timestamp timestamp not null, actor_id varchar(36) not null, file_id varchar(36) not null, revision_no integer not null, primary key (id));

create table file_wc_properties (file_wc_id varchar(36) not null, prop_value varchar(1024) not null, prop_key varchar(255) not null, primary key (file_wc_id, prop_key));

create table file_wcopies (id varchar(36) not null, vn bigint not null, size_in_bytes integer not null, data_id varchar(36) not null, mime_type_primary varchar(255) not null, mime_type_sub varchar(255) not null, file_id varchar(36) not null, primary key (id), unique (data_id));

create table files (id varchar(36) not null, current_revision integer not null, primary key (id));

create table folders (id varchar(36) not null, sort_order varchar(255) not null, index_page varchar(36), primary key (id));

create table logentries (id varchar(36) not null, vn bigint not null, actor_id varchar(36) not null, command varchar(255) not null, system bit not null, happened_on timestamp not null, subject_id varchar(36) not null, detail clob not null, index_position bigint not null DEFAULT nextval('LOG_ENTRY_INDEX'), recorded_on timestamp not null DEFAULT CURRENT_TIMESTAMP(), primary key (id));

create table page_revision_paragraphs (page_revision_id varchar(36) not null, name varchar(255) not null, type varchar(255) not null, value_text clob, value_boolean bit, value_date timestamp, primary key (page_revision_id, name));

create table page_revisions (id varchar(36) not null, vn bigint not null, major_change bit not null, actor_comment varchar(1024), timestamp timestamp not null, actor_id varchar(36) not null, page_id varchar(36) not null, revision_no integer not null, primary key (id));

create table page_wcopies (id varchar(36) not null, vn bigint not null, page_id varchar(36) not null, primary key (id));

create table page_wcopy_paragraphs (page_wcopy_id varchar(36) not null, name varchar(255) not null, type varchar(255) not null, value_text clob, value_boolean bit, value_date timestamp, primary key (page_wcopy_id, name));

create table pages (id varchar(36) not null, current_revision integer not null, primary key (id));

create table resource_metadata (resource_id varchar(36) not null, datum_value varchar(1024) not null, datum_key varchar(255) not null, primary key (resource_id, datum_key));

create table resource_roles (resource_id varchar(36) not null, role varchar(255) not null, primary key (resource_id, role));

create table resource_tags (resource_id varchar(36) not null, tag_value varchar(255) not null, primary key (resource_id, tag_value));

create table resources (id varchar(36) not null, vn bigint not null, title varchar(255) not null, in_main_menu bit not null, deleted bit not null, date_created timestamp not null, date_changed timestamp not null, description varchar(1024), template_id varchar(36), locked_by_id varchar(36), published_by_id varchar(36), created_by_id varchar(36), changed_by_id varchar(36), name varchar(255) not null, parent_id varchar(36), cache_duration bigint, parent_index integer, primary key (id));

create table searches (id varchar(36) not null, primary key (id));

create table settings (id varchar(36) not null, vn bigint not null, value varchar(1024) not null, name varchar(255) not null unique, primary key (id));

create table template_revisions (id varchar(36) not null, vn bigint not null, body clob not null, definition clob not null, mime_type_primary varchar(255) not null, mime_type_sub varchar(255) not null, major_change bit not null, actor_comment varchar(1024), timestamp timestamp not null, actor_id varchar(36) not null, template_id varchar(36) not null, revision_no integer not null, primary key (id));

create table templates (id varchar(36) not null, current_revision integer not null, primary key (id));

create table user_metadata (user_id varchar(36) not null, datum_value varchar(1024) not null, datum_key varchar(255) not null, primary key (user_id, datum_key));

create table user_roles (user_id varchar(36) not null, role varchar(255) not null, primary key (user_id, role));

create table users (id varchar(36) not null, vn bigint not null, username varchar(255) not null unique, hash binary(255) not null, email varchar(512) not null, name varchar(255) not null, primary key (id));

alter table action_fail_params add constraint FK_ACTION_FPARAM_ACTION_ID foreign key (action_id) references actions;

alter table action_params add constraint FK_ACTION_PARAM_ACTION_ID foreign key (action_id) references actions;

alter table actions add constraint FK_ACTION_RESOURCE_SUBJECT_ID foreign key (subject_id) references resources;

alter table actions add constraint FK_ACTION_USER_ACTOR_ID foreign key (actor_id) references users;

alter table aliases add constraint FK_ALIAS_RESOURCE_ID foreign key (id) references resources;

alter table aliases add constraint FK_ALIAS_RESOURCE_TARGET_ID foreign key (target_id) references resources;

alter table file_revision_properties add constraint FK_FILEREVPROPS_FILEREV_ID foreign key (file_revision_id) references file_revisions;

alter table file_revisions add constraint FK_REVISION_FILE_ID foreign key (file_id) references files;

alter table file_revisions add constraint FK_FILE_REV_USER_ACTOR_ID foreign key (actor_id) references users;

alter table file_wc_properties add constraint FK_FILEWCPROPS_FILEWC_ID foreign key (file_wc_id) references file_wcopies;

alter table file_wcopies add constraint FK_WCOPY_FILE_ID foreign key (file_id) references files;

alter table files add constraint FK_FILE_RESOURCE_ID foreign key (id) references resources;

alter table folders add constraint FK_FOLDER_RESOURCE_ID foreign key (id) references resources;

alter table folders add constraint FK_FOLDER_INDEX_PAGE_ID foreign key (index_page) references pages;

alter table logentries add constraint FK_LOGENTRY_USER_ACTOR_ID foreign key (actor_id) references users;

alter table page_revision_paragraphs add constraint FK_PARAGRAPH_REVISION_ID foreign key (page_revision_id) references page_revisions;

alter table page_revisions add constraint FK_PAGE_REV_USER_ACTOR_ID foreign key (actor_id) references users;

alter table page_revisions add constraint FK_REVISION_PAGE_ID foreign key (page_id) references pages;

alter table page_wcopies add constraint FK_WCOPY_PAGE_ID foreign key (page_id) references pages;

alter table page_wcopy_paragraphs add constraint FK_PARAGRAPH_WCOPY_ID foreign key (page_wcopy_id) references page_wcopies;

alter table pages add constraint FK_PAGE_RESOURCE_ID foreign key (id) references resources;

alter table resource_metadata add constraint FK_RESMETADATA_RESOURCE_ID foreign key (resource_id) references resources;

alter table resource_roles add constraint FK_RESROLES_RESOURCE_ID foreign key (resource_id) references resources;

alter table resource_tags add constraint FK_RESTAGS_RESOURCE_ID foreign key (resource_id) references resources;

alter table resources add constraint FK_RESOURCE_USER_CHANGED_ID foreign key (changed_by_id) references users;

alter table resources add constraint FK_RESOURCE_USER_PUBLISHED_ID foreign key (published_by_id) references users;

alter table resources add constraint FK_RESOURCE_USER_CREATED_ID foreign key (created_by_id) references users;

alter table resources add constraint FK_RESOURCE_TEMPLATE_ID foreign key (template_id) references templates;

alter table resources add constraint FK_RESOURCE_FOLDER_PARENT_ID foreign key (parent_id) references folders;

alter table resources add constraint FK_RESOURCE_USER_LOCKED_ID foreign key (locked_by_id) references users;

alter table searches add constraint FK_SEARCH_RESOURCE_ID foreign key (id) references resources;

alter table template_revisions add constraint FK_TEMPLATE_REV_USER_ACTOR_ID foreign key (actor_id) references users;

alter table template_revisions add constraint FK_REVISION_TEMPLATE_ID foreign key (template_id) references templates;

alter table templates add constraint FK_TEMPLATE_RESOURCE_ID foreign key (id) references resources;

alter table user_metadata add constraint FK_USERMETADATA_USER_ID foreign key (user_id) references users;

alter table user_roles add constraint FK_USERROLES_USER_ID foreign key (user_id) references users;

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c03', 0, '0', 'DATABASE_VERSION');

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c04', 0, '/tmp/CCC7/lucene', 'LUCENE_INDEX_PATH');

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c05', 0, '/tmp/CCC7/filestore', 'FILE_STORE_PATH');
