create table actions (id varchar(255) not null, execute_after datetime not null, type varchar(255) not null, status varchar(255) not null, actor_id varchar(255) not null, subject_id varchar(255) not null, parameters longtext not null, failure longtext, primary key (id)) ENGINE=InnoDB;
create table aliases (id varchar(255) not null, target_id varchar(255) not null, primary key (id)) ENGINE=InnoDB;
create table files (id varchar(255) not null, description varchar(1024) not null, size_in_bytes integer not null, data_id varchar(255) not null, mime_type_primary varchar(255) not null, mime_type_sub varchar(255) not null, primary key (id), unique (data_id)) ENGINE=InnoDB;
create table folders (id varchar(255) not null, sort_order varchar(255) not null, primary key (id)) ENGINE=InnoDB;
create table logentries (id varchar(255) not null, actor_id varchar(255) not null, action varchar(255) not null, happened_on datetime not null, subject_type varchar(255) not null, subject_id varchar(255) not null, message varchar(1024) not null, detail longtext not null, index_position bigint not null, recorded_on datetime not null, major_change bit not null, primary key (id)) ENGINE=InnoDB;
create table pages (id varchar(255) not null, working_copy longtext, primary key (id)) ENGINE=InnoDB;
create table paragraphs (page_id varchar(255) not null, name varchar(255) not null, type varchar(255) not null, value_text longtext, value_boolean bit, value_date datetime, primary key (page_id, name, type)) ENGINE=InnoDB;
create table passwords (id varchar(255) not null, version bigint not null, hash tinyblob not null, user_id varchar(255) not null unique, primary key (id)) ENGINE=InnoDB;
create table resource_metadata (resource_id varchar(255) not null, datum_value varchar(1000) not null, datum_key varchar(100) not null, primary key (resource_id, datum_key)) ENGINE=InnoDB;
create table resource_roles (resource_id varchar(255) not null, role varchar(255) not null, primary key (resource_id, role)) ENGINE=InnoDB;
create table resource_tags (resource_id varchar(255) not null, tag_value varchar(255) not null, tag_index integer not null, primary key (resource_id, tag_index)) ENGINE=InnoDB;
create table resources (id varchar(255) not null, version bigint not null, title varchar(256) not null, in_main_menu bit not null, date_created datetime not null, date_changed datetime not null, template_id varchar(255), locked_by_id varchar(255), published_by_id varchar(255), name varchar(256) not null, parent_id varchar(255), parent_index integer, primary key (id)) ENGINE=InnoDB;
create table searches (id varchar(255) not null, primary key (id)) ENGINE=InnoDB;
create table settings (id varchar(255) not null, version bigint not null, value varchar(255) not null, name varchar(255) not null unique, primary key (id)) ENGINE=InnoDB;
create table templates (id varchar(255) not null, title varchar(256) not null, description varchar(1024) not null, body longtext not null, definition longtext not null, primary key (id)) ENGINE=InnoDB;
create table user_roles (user_id varchar(255) not null, role varchar(255) not null, primary key (user_id, role)) ENGINE=InnoDB;
create table users (id varchar(255) not null, version bigint not null, username varchar(255) not null unique, email varchar(255) not null, primary key (id)) ENGINE=InnoDB;
alter table actions add index FKBAC048FD432B573B (subject_id), add constraint FKBAC048FD432B573B foreign key (subject_id) references resources (id);
alter table actions add index FKBAC048FD584BF10F (actor_id), add constraint FKBAC048FD584BF10F foreign key (actor_id) references users (id);
alter table aliases add index FKC97D4EFE655C6748 (id), add constraint FKC97D4EFE655C6748 foreign key (id) references resources (id);
alter table aliases add index FKC97D4EFE34BFA4B6 (target_id), add constraint FKC97D4EFE34BFA4B6 foreign key (target_id) references resources (id);
alter table files add index FK5CEBA77655C6748 (id), add constraint FK5CEBA77655C6748 foreign key (id) references resources (id);
alter table folders add index FKD74671C5655C6748 (id), add constraint FKD74671C5655C6748 foreign key (id) references resources (id);
alter table logentries add index FKC5636CCC584BF10F (actor_id), add constraint FKC5636CCC584BF10F foreign key (actor_id) references users (id);
alter table pages add index FK657EFC4655C6748 (id), add constraint FK657EFC4655C6748 foreign key (id) references resources (id);
alter table paragraphs add index FK11C964C58AC5B0F9 (page_id), add constraint FK11C964C58AC5B0F9 foreign key (page_id) references pages (id);
alter table passwords add index FKC8AD9938B1E85779 (user_id), add constraint FKC8AD9938B1E85779 foreign key (user_id) references users (id);
alter table resource_metadata add index FK85E79840AEF1E519 (resource_id), add constraint FK85E79840AEF1E519 foreign key (resource_id) references resources (id);
alter table resource_roles add index FK11F6F34CAEF1E519 (resource_id), add constraint FK11F6F34CAEF1E519 foreign key (resource_id) references resources (id);
alter table resource_tags add index FK3A6381AAAEF1E519 (resource_id), add constraint FK3A6381AAAEF1E519 foreign key (resource_id) references resources (id);
alter table resources add index FK89CCBE252A3B35DC (published_by_id), add constraint FK89CCBE252A3B35DC foreign key (published_by_id) references users (id);
alter table resources add index FK89CCBE259465FD99 (template_id), add constraint FK89CCBE259465FD99 foreign key (template_id) references templates (id);
alter table resources add index FK89CCBE2526915F1D (parent_id), add constraint FK89CCBE2526915F1D foreign key (parent_id) references folders (id);
alter table resources add index FK89CCBE25474981F8 (locked_by_id), add constraint FK89CCBE25474981F8 foreign key (locked_by_id) references users (id);
alter table searches add index FK34F7A856655C6748 (id), add constraint FK34F7A856655C6748 foreign key (id) references resources (id);
alter table templates add index FK761EC339655C6748 (id), add constraint FK761EC339655C6748 foreign key (id) references resources (id);
alter table user_roles add index FK73429949B1E85779 (user_id), add constraint FK73429949B1E85779 foreign key (user_id) references users (id);
CREATE TABLE data( id VARCHAR(255) NOT NULL, version INTEGER NOT NULL, bytes BLOB NOT NULL );
ALTER TABLE logentries CHANGE COLUMN index_position index_position BIGINT(20) NOT NULL AUTO_INCREMENT UNIQUE;
ALTER TABLE logentries CHANGE COLUMN recorded_on recorded_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
INSERT INTO SETTINGS (id, version, value, name) VALUES ('145e827a-0f11-41bf-af0b-ad9a4a982c03', 0, '0', 'DATABASE_VERSION');