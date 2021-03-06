create table action_fail_params (action_id varchar2(36 char) not null, param_value varchar2(1024 char) not null, param_key varchar2(255 char) not null, primary key (action_id, param_key));

create table action_params (action_id varchar2(36 char) not null, param_value varchar2(1024 char) not null, param_key varchar2(255 char) not null, primary key (action_id, param_key));

create table actions (id varchar2(36 char) not null, vn number(19,0) not null, execute_after timestamp not null, command varchar2(255 char) not null, status varchar2(255 char) not null, actor_id varchar2(36 char) not null, subject_id varchar2(36 char) not null, failure_code varchar2(255 char), failure_id varchar2(36 char), primary key (id));

create table aliases (id varchar2(36 char) not null, target_id varchar2(36 char) not null, primary key (id));

create table file_revision_properties (file_revision_id varchar2(36 char) not null, prop_value varchar2(1024 char) not null, prop_key varchar2(255 char) not null, primary key (file_revision_id, prop_key));

create table file_revisions (id varchar2(36 char) not null, vn number(19,0) not null, size_in_bytes number(10,0) not null, data_id varchar2(36 char) not null, mime_type_primary varchar2(255 char) not null, mime_type_sub varchar2(255 char) not null, major_change number(1,0) not null, actor_comment varchar2(1024 char), timestamp timestamp not null, actor_id varchar2(36 char) not null, file_id varchar2(36 char) not null, revision_no number(10,0) not null, primary key (id));

create table file_wc_properties (file_wc_id varchar2(36 char) not null, prop_value varchar2(1024 char) not null, prop_key varchar2(255 char) not null, primary key (file_wc_id, prop_key));

create table file_wcopies (id varchar2(36 char) not null, vn number(19,0) not null, size_in_bytes number(10,0) not null, data_id varchar2(36 char) not null, mime_type_primary varchar2(255 char) not null, mime_type_sub varchar2(255 char) not null, file_id varchar2(36 char) not null, primary key (id), unique (data_id));

create table files (id varchar2(36 char) not null, current_revision number(10,0) not null, primary key (id));

create table folders (id varchar2(36 char) not null, sort_order varchar2(255 char) not null, index_page varchar2(36 char), primary key (id));

create table logentries (id varchar2(36 char) not null, vn number(19,0) not null, actor_id varchar2(36 char) not null, command varchar2(255 char) not null, system number(1,0) not null, happened_on timestamp not null, subject_id varchar2(36 char) not null, detail clob not null, index_position number(19,0) not null, recorded_on timestamp not null, primary key (id));

create table page_revision_paragraphs (page_revision_id varchar2(36 char) not null, name varchar2(255 char) not null, type varchar2(255 char) not null, value_text clob, value_boolean number(1,0), value_date timestamp, primary key (page_revision_id, name));

create table page_revisions (id varchar2(36 char) not null, vn number(19,0) not null, major_change number(1,0) not null, actor_comment varchar2(1024 char), timestamp timestamp not null, actor_id varchar2(36 char) not null, page_id varchar2(36 char) not null, revision_no number(10,0) not null, primary key (id));

create table page_wcopies (id varchar2(36 char) not null, vn number(19,0) not null, page_id varchar2(36 char) not null, primary key (id));

create table page_wcopy_paragraphs (page_wcopy_id varchar2(36 char) not null, name varchar2(255 char) not null, type varchar2(255 char) not null, value_text clob, value_boolean number(1,0), value_date timestamp, primary key (page_wcopy_id, name));

create table pages (id varchar2(36 char) not null, current_revision number(10,0) not null, primary key (id));

create table resource_metadata (resource_id varchar2(36 char) not null, datum_value varchar2(1024 char) not null, datum_key varchar2(255 char) not null, primary key (resource_id, datum_key));

create table resource_roles (resource_id varchar2(36 char) not null, role varchar2(255 char) not null, primary key (resource_id, role));

create table resource_tags (resource_id varchar2(36 char) not null, tag_value varchar2(255 char) not null, primary key (resource_id, tag_value));

create table resources (id varchar2(36 char) not null, vn number(19,0) not null, title varchar2(255 char) not null, in_main_menu number(1,0) not null, deleted number(1,0) not null, date_created timestamp not null, date_changed timestamp not null, description varchar2(1024 char), template_id varchar2(36 char), locked_by_id varchar2(36 char), published_by_id varchar2(36 char), created_by_id varchar2(36 char), changed_by_id varchar2(36 char), name varchar2(255 char) not null, parent_id varchar2(36 char), cache_duration number(19,0), parent_index number(10,0), primary key (id));

create table searches (id varchar2(36 char) not null, primary key (id));

create table settings (id varchar2(36 char) not null, vn number(19,0) not null, value varchar2(1024 char) not null, name varchar2(255 char) not null unique, primary key (id));

create table template_revisions (id varchar2(36 char) not null, vn number(19,0) not null, body clob not null, definition clob not null, mime_type_primary varchar2(255 char) not null, mime_type_sub varchar2(255 char) not null, major_change number(1,0) not null, actor_comment varchar2(1024 char), timestamp timestamp not null, actor_id varchar2(36 char) not null, template_id varchar2(36 char) not null, revision_no number(10,0) not null, primary key (id));

create table templates (id varchar2(36 char) not null, current_revision number(10,0) not null, primary key (id));

create table user_metadata (user_id varchar2(36 char) not null, datum_value varchar2(1024 char) not null, datum_key varchar2(255 char) not null, primary key (user_id, datum_key));

create table user_roles (user_id varchar2(36 char) not null, role varchar2(255 char) not null, primary key (user_id, role));

create table users (id varchar2(36 char) not null, vn number(19,0) not null, username varchar2(255 char) not null unique, hash raw(255) not null, email varchar2(512 char) not null, name varchar2(255 char) not null, primary key (id));

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

CREATE SEQUENCE log_entry_index START WITH 1 INCREMENT BY 1;

CREATE TRIGGER logentry_autonumber BEFORE INSERT ON logentries FOR EACH ROW BEGIN SELECT log_entry_index.nextval INTO :new.index_position FROM dual; END;;

CREATE TRIGGER logentry_timestamp BEFORE INSERT ON logentries FOR EACH ROW BEGIN SELECT systimestamp INTO :new.recorded_on FROM dual; END;;

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c03', 0, '0', 'DATABASE_VERSION');

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c04', 0, '/tmp/CCC7/lucene', 'LUCENE_INDEX_PATH');

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c05', 0, '/tmp/CCC7/filestore', 'FILE_STORE_PATH');
