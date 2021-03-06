create table action_fail_params (action_id varchar(36) not null, param_value varchar(1024) not null, param_key varchar(255) not null, primary key (action_id, param_key)) ENGINE=InnoDB;

create table action_params (action_id varchar(36) not null, param_value varchar(1024) not null, param_key varchar(255) not null, primary key (action_id, param_key)) ENGINE=InnoDB;

create table actions (id varchar(36) not null, vn bigint not null, execute_after datetime not null, command varchar(255) not null, status varchar(255) not null, actor_id varchar(36) not null, subject_id varchar(36) not null, failure_code varchar(255), failure_id varchar(36), primary key (id)) ENGINE=InnoDB;

create table aliases (id varchar(36) not null, target_id varchar(36) not null, primary key (id)) ENGINE=InnoDB;

create table file_revision_properties (file_revision_id varchar(36) not null, prop_value varchar(1024) not null, prop_key varchar(255) not null, primary key (file_revision_id, prop_key)) ENGINE=InnoDB;

create table file_revisions (id varchar(36) not null, vn bigint not null, size_in_bytes integer not null, data_id varchar(36) not null, mime_type_primary varchar(255) not null, mime_type_sub varchar(255) not null, major_change bit not null, actor_comment varchar(1024), timestamp datetime not null, actor_id varchar(36) not null, file_id varchar(36) not null, revision_no integer not null, primary key (id)) ENGINE=InnoDB;

create table file_wc_properties (file_wc_id varchar(36) not null, prop_value varchar(1024) not null, prop_key varchar(255) not null, primary key (file_wc_id, prop_key)) ENGINE=InnoDB;

create table file_wcopies (id varchar(36) not null, vn bigint not null, size_in_bytes integer not null, data_id varchar(36) not null, mime_type_primary varchar(255) not null, mime_type_sub varchar(255) not null, file_id varchar(36) not null, primary key (id), unique (data_id)) ENGINE=InnoDB;

create table files (id varchar(36) not null, current_revision integer not null, primary key (id)) ENGINE=InnoDB;

create table folders (id varchar(36) not null, sort_order varchar(255) not null, index_page varchar(36), primary key (id)) ENGINE=InnoDB;

create table logentries (id varchar(36) not null, vn bigint not null, actor_id varchar(36) not null, command varchar(255) not null, system bit not null, happened_on datetime not null, subject_id varchar(36) not null, detail longtext not null, index_position BIGINT(20) NOT NULL AUTO_INCREMENT UNIQUE, recorded_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, primary key (id)) ENGINE=InnoDB;

create table page_revision_paragraphs (page_revision_id varchar(36) not null, name varchar(255) not null, type varchar(255) not null, value_text longtext, value_boolean bit, value_date datetime, primary key (page_revision_id, name)) ENGINE=InnoDB;

create table page_revisions (id varchar(36) not null, vn bigint not null, major_change bit not null, actor_comment varchar(1024), timestamp datetime not null, actor_id varchar(36) not null, page_id varchar(36) not null, revision_no integer not null, primary key (id)) ENGINE=InnoDB;

create table page_wcopies (id varchar(36) not null, vn bigint not null, page_id varchar(36) not null, primary key (id)) ENGINE=InnoDB;

create table page_wcopy_paragraphs (page_wcopy_id varchar(36) not null, name varchar(255) not null, type varchar(255) not null, value_text longtext, value_boolean bit, value_date datetime, primary key (page_wcopy_id, name)) ENGINE=InnoDB;

create table pages (id varchar(36) not null, current_revision integer not null, primary key (id)) ENGINE=InnoDB;

create table resource_metadata (resource_id varchar(36) not null, datum_value varchar(1024) not null, datum_key varchar(255) not null, primary key (resource_id, datum_key)) ENGINE=InnoDB;

create table resource_roles (resource_id varchar(36) not null, role varchar(255) not null, primary key (resource_id, role)) ENGINE=InnoDB;

create table resource_tags (resource_id varchar(36) not null, tag_value varchar(255) not null, primary key (resource_id, tag_value)) ENGINE=InnoDB;

create table resources (id varchar(36) not null, vn bigint not null, title varchar(255) not null, in_main_menu bit not null, deleted bit not null, date_created datetime not null, date_changed datetime not null, description varchar(1024), template_id varchar(36), locked_by_id varchar(36), published_by_id varchar(36), created_by_id varchar(36), changed_by_id varchar(36), name varchar(255) not null, parent_id varchar(36), cache_duration bigint, parent_index integer, primary key (id)) ENGINE=InnoDB;

create table searches (id varchar(36) not null, primary key (id)) ENGINE=InnoDB;

create table settings (id varchar(36) not null, vn bigint not null, value varchar(1024) not null, name varchar(255) not null unique, primary key (id)) ENGINE=InnoDB;

create table template_revisions (id varchar(36) not null, vn bigint not null, body longtext not null, definition longtext not null, mime_type_primary varchar(255) not null, mime_type_sub varchar(255) not null, major_change bit not null, actor_comment varchar(1024), timestamp datetime not null, actor_id varchar(36) not null, template_id varchar(36) not null, revision_no integer not null, primary key (id)) ENGINE=InnoDB;

create table templates (id varchar(36) not null, current_revision integer not null, primary key (id)) ENGINE=InnoDB;

create table user_metadata (user_id varchar(36) not null, datum_value varchar(1024) not null, datum_key varchar(255) not null, primary key (user_id, datum_key)) ENGINE=InnoDB;

create table user_roles (user_id varchar(36) not null, role varchar(255) not null, primary key (user_id, role)) ENGINE=InnoDB;

create table users (id varchar(36) not null, vn bigint not null, username varchar(255) not null unique, hash tinyblob not null, email varchar(512) not null, name varchar(255) not null, primary key (id)) ENGINE=InnoDB;

alter table action_fail_params add index FK_ACTION_FPARAM_ACTION_ID (action_id), add constraint FK_ACTION_FPARAM_ACTION_ID foreign key (action_id) references actions (id);

alter table action_params add index FK_ACTION_PARAM_ACTION_ID (action_id), add constraint FK_ACTION_PARAM_ACTION_ID foreign key (action_id) references actions (id);

alter table actions add index FK_ACTION_RESOURCE_SUBJECT_ID (subject_id), add constraint FK_ACTION_RESOURCE_SUBJECT_ID foreign key (subject_id) references resources (id);

alter table actions add index FK_ACTION_USER_ACTOR_ID (actor_id), add constraint FK_ACTION_USER_ACTOR_ID foreign key (actor_id) references users (id);

alter table aliases add index FK_ALIAS_RESOURCE_ID (id), add constraint FK_ALIAS_RESOURCE_ID foreign key (id) references resources (id);

alter table aliases add index FK_ALIAS_RESOURCE_TARGET_ID (target_id), add constraint FK_ALIAS_RESOURCE_TARGET_ID foreign key (target_id) references resources (id);

alter table file_revision_properties add index FK_FILEREVPROPS_FILEREV_ID (file_revision_id), add constraint FK_FILEREVPROPS_FILEREV_ID foreign key (file_revision_id) references file_revisions (id);

alter table file_revisions add index FK_REVISION_FILE_ID (file_id), add constraint FK_REVISION_FILE_ID foreign key (file_id) references files (id);

alter table file_revisions add index FK_FILE_REV_USER_ACTOR_ID (actor_id), add constraint FK_FILE_REV_USER_ACTOR_ID foreign key (actor_id) references users (id);

alter table file_wc_properties add index FK_FILEWCPROPS_FILEWC_ID (file_wc_id), add constraint FK_FILEWCPROPS_FILEWC_ID foreign key (file_wc_id) references file_wcopies (id);

alter table file_wcopies add index FK_WCOPY_FILE_ID (file_id), add constraint FK_WCOPY_FILE_ID foreign key (file_id) references files (id);

alter table files add index FK_FILE_RESOURCE_ID (id), add constraint FK_FILE_RESOURCE_ID foreign key (id) references resources (id);

alter table folders add index FK_FOLDER_RESOURCE_ID (id), add constraint FK_FOLDER_RESOURCE_ID foreign key (id) references resources (id);

alter table folders add index FK_FOLDER_INDEX_PAGE_ID (index_page), add constraint FK_FOLDER_INDEX_PAGE_ID foreign key (index_page) references pages (id);

alter table logentries add index FK_LOGENTRY_USER_ACTOR_ID (actor_id), add constraint FK_LOGENTRY_USER_ACTOR_ID foreign key (actor_id) references users (id);

alter table page_revision_paragraphs add index FK_PARAGRAPH_REVISION_ID (page_revision_id), add constraint FK_PARAGRAPH_REVISION_ID foreign key (page_revision_id) references page_revisions (id);

alter table page_revisions add index FK_PAGE_REV_USER_ACTOR_ID (actor_id), add constraint FK_PAGE_REV_USER_ACTOR_ID foreign key (actor_id) references users (id);

alter table page_revisions add index FK_REVISION_PAGE_ID (page_id), add constraint FK_REVISION_PAGE_ID foreign key (page_id) references pages (id);

alter table page_wcopies add index FK_WCOPY_PAGE_ID (page_id), add constraint FK_WCOPY_PAGE_ID foreign key (page_id) references pages (id);

alter table page_wcopy_paragraphs add index FK_PARAGRAPH_WCOPY_ID (page_wcopy_id), add constraint FK_PARAGRAPH_WCOPY_ID foreign key (page_wcopy_id) references page_wcopies (id);

alter table pages add index FK_PAGE_RESOURCE_ID (id), add constraint FK_PAGE_RESOURCE_ID foreign key (id) references resources (id);

alter table resource_metadata add index FK_RESMETADATA_RESOURCE_ID (resource_id), add constraint FK_RESMETADATA_RESOURCE_ID foreign key (resource_id) references resources (id);

alter table resource_roles add index FK_RESROLES_RESOURCE_ID (resource_id), add constraint FK_RESROLES_RESOURCE_ID foreign key (resource_id) references resources (id);

alter table resource_tags add index FK_RESTAGS_RESOURCE_ID (resource_id), add constraint FK_RESTAGS_RESOURCE_ID foreign key (resource_id) references resources (id);

alter table resources add index FK_RESOURCE_USER_CHANGED_ID (changed_by_id), add constraint FK_RESOURCE_USER_CHANGED_ID foreign key (changed_by_id) references users (id);

alter table resources add index FK_RESOURCE_USER_PUBLISHED_ID (published_by_id), add constraint FK_RESOURCE_USER_PUBLISHED_ID foreign key (published_by_id) references users (id);

alter table resources add index FK_RESOURCE_USER_CREATED_ID (created_by_id), add constraint FK_RESOURCE_USER_CREATED_ID foreign key (created_by_id) references users (id);

alter table resources add index FK_RESOURCE_TEMPLATE_ID (template_id), add constraint FK_RESOURCE_TEMPLATE_ID foreign key (template_id) references templates (id);

alter table resources add index FK_RESOURCE_FOLDER_PARENT_ID (parent_id), add constraint FK_RESOURCE_FOLDER_PARENT_ID foreign key (parent_id) references folders (id);

alter table resources add index FK_RESOURCE_USER_LOCKED_ID (locked_by_id), add constraint FK_RESOURCE_USER_LOCKED_ID foreign key (locked_by_id) references users (id);

alter table searches add index FK_SEARCH_RESOURCE_ID (id), add constraint FK_SEARCH_RESOURCE_ID foreign key (id) references resources (id);

alter table template_revisions add index FK_TEMPLATE_REV_USER_ACTOR_ID (actor_id), add constraint FK_TEMPLATE_REV_USER_ACTOR_ID foreign key (actor_id) references users (id);

alter table template_revisions add index FK_REVISION_TEMPLATE_ID (template_id), add constraint FK_REVISION_TEMPLATE_ID foreign key (template_id) references templates (id);

alter table templates add index FK_TEMPLATE_RESOURCE_ID (id), add constraint FK_TEMPLATE_RESOURCE_ID foreign key (id) references resources (id);

alter table user_metadata add index FK_USERMETADATA_USER_ID (user_id), add constraint FK_USERMETADATA_USER_ID foreign key (user_id) references users (id);

alter table user_roles add index FK_USERROLES_USER_ID (user_id), add constraint FK_USERROLES_USER_ID foreign key (user_id) references users (id);

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c03', 0, '0', 'DATABASE_VERSION');

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c04', 0, '/tmp/CCC7/lucene', 'LUCENE_INDEX_PATH');

INSERT INTO settings (id, vn, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c05', 0, '/tmp/CCC7/filestore', 'FILE_STORE_PATH');
