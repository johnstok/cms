UPDATE settings SET value='1' WHERE name='DATABASE_VERSION';
create table comments (id varchar(36) not null, vn bigint not null, timestamp datetime not null, body longtext not null, author varchar(1024), url varchar(1024), email varchar(512) not null, status varchar(255) not null, resource_id varchar(36) not null, primary key (id)) ENGINE=InnoDB;
alter table comments add index FK_COMMENT_RESOURCE_ID (resource_id), add constraint FK_COMMENT_RESOURCE_ID foreign key (resource_id) references resources (id);
