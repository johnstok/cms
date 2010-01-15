UPDATE settings SET value='1' WHERE name='DATABASE_VERSION';
create table comments (id varchar2(36 char) not null, vn number(19,0) not null, timestamp timestamp not null, body clob not null, author varchar(1024 char), url varchar(1024 char), email varchar(512 char) not null, status varchar(255 char) not null, resource_id varchar(36 char) not null, primary key (id));
alter table comments add constraint FK_COMMENT_RESOURCE_ID foreign key (resource_id) references resources;
