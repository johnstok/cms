UPDATE settings SET value='1' WHERE name='DATABASE_VERSION';
create table comments (id varchar(36) not null, vn numeric(19,0) not null, timestamp datetime not null, body text not null, author varchar(1024), url varchar(1024), email varchar(512) not null, status varchar(255) not null, resource_id varchar(36) not null, primary key (id));
alter table comments add constraint FK_COMMENT_RESOURCE_ID foreign key (resource_id) references resources;
