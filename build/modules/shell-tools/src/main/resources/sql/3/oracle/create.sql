UPDATE settings SET value='3' WHERE name='DATABASE_VERSION';

ALTER TABLE page_revision_paragraphs ADD index_text varchar2(1024 char);
UPDATE page_revision_paragraphs SET index_text=dbms_lob.substr(value_text, 1024) where type='TEXT';

ALTER TABLE folders DROP CONSTRAINT fk_folder_index_page_id;
ALTER TABLE folders ADD  CONSTRAINT fk_folder_index_page_id FOREIGN KEY (index_page) REFERENCES resources;

CREATE OR REPLACE VIEW resource_metadata_by_relevance AS SELECT r.id resource_id, m.datum_key, m.datum_value, instr( r.id_path, m.resource_id) relevance FROM (SELECT id, SYS_CONNECT_BY_PATH(id, '/') id_path FROM resources START WITH parent_id is null CONNECT BY prior id = parent_id) r, resource_metadata m WHERE id_path LIKE '%/'||m.resource_id||'%';

CREATE OR REPLACE VIEW resource_metadata_hierarchical AS SELECT meta.* FROM resource_metadata_by_relevance meta, (SELECT resource_id, datum_key, max(relevance) most_relevant FROM resource_metadata_by_relevance GROUP BY resource_id, datum_key) relevant WHERE meta.datum_key=relevant.datum_key AND meta.resource_id=relevant.resource_id AND meta.relevance=relevant.most_relevant;

CREATE OR REPLACE VIEW resource_hierarchical AS SELECT id resource_id, CASE WHEN length(SYS_CONNECT_BY_PATH(published_by_id, '/'))<(37*level) THEN 0 ELSE 1 END published, CASE WHEN to_number(replace(SYS_CONNECT_BY_PATH(perms, '/'), '/', '0')) > 0 THEN 1 ELSE 0 END secure, SYS_CONNECT_BY_PATH(name, '/') absolute_path FROM (SELECT r.*, (SELECT count(*) FROM resource_users u WHERE r.id = u.resource_id) + (SELECT count(*) FROM resource_roles g WHERE r.id = g.resource_id) perms FROM resources r) START WITH parent_id is null CONNECT BY prior id = parent_id;

CREATE OR REPLACE VIEW resource_template_hierarchical AS SELECT id resource_id, substr(template, instr(template, '/', -1)+1) template_id FROM (SELECT id, rtrim(SYS_CONNECT_BY_PATH(template_id, '/'), '/') template FROM resources r START WITH parent_id is null CONNECT BY prior id = parent_id);
  
CREATE OR REPLACE VIEW resource_tags_hierarchical AS SELECT DISTINCT r.id resource_id, t.tag_value FROM (SELECT id, SYS_CONNECT_BY_PATH(id, '/') id_path FROM resources START WITH parent_id is null CONNECT BY prior id = parent_id) r, resource_tags t WHERE id_path LIKE '%/'||t.resource_id||'%';

ALTER TABLE searches DROP FOREIGN KEY FK_SEARCH_RESOURCE_ID;
DELETE FROM resources WHERE id IN (SELECT id FROM searches);
DELETE FROM searches;
DROP TABLE searches;
