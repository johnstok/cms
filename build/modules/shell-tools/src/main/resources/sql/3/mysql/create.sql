UPDATE settings SET value='3' WHERE name='DATABASE_VERSION';

ALTER TABLE page_revision_paragraphs ADD index_text varchar(1024);

UPDATE page_revision_paragraphs SET index_text=substring(value_text, 1, 1024) WHERE type='TEXT';

ALTER TABLE folders DROP FOREIGN KEY FK_FOLDER_INDEX_PAGE_ID;

ALTER TABLE folders ADD CONSTRAINT FK_FOLDER_INDEX_PAGE_ID foreign key (index_page) REFERENCES resources (id);

ALTER TABLE page_revision_paragraphs ADD value_decimal DECIMAL(19,6); 

-- resource_metadata_by_relevance

CREATE FUNCTION abs_path (param1 VARCHAR(36)) RETURNS longtext charSET utf8
BEGIN
DECLARE parentId VARCHAR(36);
DECLARE acolumn VARCHAR(1024);
DECLARE absPath VARCHAR(4000);
SET parentId := param1;
SET absPath := '';
while (parentId IS NOT NULL) DO
SELECT id, parent_id INTO acolumn, parentId FROM resources WHERE id = parentId;
SET absPath := concat('/',acolumn,absPath);
END while;
SET absPath := trim(trailing '/' FROM absPath);
RETURN absPath;
END;

CREATE OR REPLACE VIEW resource_metadata_by_relevance AS 
SELECT r.id resource_id, m.datum_key, m.datum_value, instr( abs_path(r.id), m.resource_id) relevance 
FROM resource_metadata m, resources r
WHERE abs_path(id) LIKE concat('%/',m.resource_id,'%');

 --resource_metadata_hierarchical

CREATE OR REPLACE VIEW resource_metadata_hierarchical AS 
SELECT meta.resource_id, meta.datum_key, meta.datum_value, meta.relevance
FROM resource_metadata_by_relevance AS meta 
LEFT JOIN resource_metadata_by_relevance AS meta2 ON meta.resource_id = meta2.resource_id 
AND meta.relevance < meta2.relevance 
WHERE meta2.resource_id IS NULL;

-- Alternative to the above... cannot be made to a view though since it contains subqueries

--CREATE FUNCTION `get_metavalue`(param1 varchar(36), metakey varchar(100)) RETURNS longtext CHARSET utf8 DETERMINISTIC
--BEGIN
--DECLARE _value varchar(1024);
--DECLARE _id varchar(36);
--SET _id = param1;
--SET @relevance := 0;
--LOOP
--    SET @relevance := @relevance + 1;
--    SELECT datum_value INTO _value FROM resource_metadata WHERE resource_id = _id AND datum_key = metakey;
--    IF _value IS NOT NULL THEN RETURN _value; END IF;
--    SELECT parent_id INTO _id FROM resources WHERE id = _id;
--    IF _id IS NULL THEN RETURN NULL; END IF;
--END LOOP;
--END;
--
---- creating temporary table instead of view
--
--CREATE TEMPORARY TABLE IF NOT EXISTS resource_metadata_hierarchical
--SELECT * FROM(
--SELECT r.id, rm.datum_key, get_metavalue(r.id, rm.datum_key) datum_value, @relevance relevance
--FROM resources r
--, (SELECT DISTINCT datum_key FROM resource_metadata) rm
--) v WHERE v.datum_value IS NOT NULL;


-- resource_hierarchical

CREATE FUNCTION isPublished (param1 VARCHAR(36)) RETURNS BIT(1)
BEGIN
DECLARE parentId VARCHAR(36);
DECLARE publishedby VARCHAR(36);
DECLARE ispublished BIT(1) DEFAULT 1;
SET parentId := param1;
mainloop: WHILE (parentId IS NOT NULL) DO
SELECT published_by_id, parent_id INTO publishedby, parentId FROM resources WHERE id = parentId;
IF publishedby IS NULL THEN SET ispublished := 0; leave mainloop; END IF;
END while mainloop;
RETURN ispublished;
END;

CREATE FUNCTION isSecure (param1 VARCHAR(36)) RETURNS BIT(1)
BEGIN
DECLARE parentId VARCHAR(36);
DECLARE issecure BIT(1) DEFAULT 0;
DECLARE FROMusers SMALLINT DEFAULT 0;
DECLARE FROMroles SMALLINT DEFAULT 0;
SET parentId := param1;
mainloop: WHILE (parentId IS NOT NULL) DO
	SELECT count(*) INTO FROMusers FROM resource_users WHERE resource_id = parentId;
	SELECT count(*) INTO FROMroles FROM resource_roles WHERE resource_id = parentId;
	IF FROMusers + FROMroles > 0 THEN SET issecure := 1; leave mainloop; END IF;
	SELECT parent_id INTO parentId FROM resources WHERE id = parentId;
END while mainloop;
RETURN issecure;
END;

CREATE FUNCTION abs_name (param1 VARCHAR(36)) RETURNS longtext charSET utf8
BEGIN
DECLARE parentId VARCHAR(36);
DECLARE acolumn VARCHAR(1024);
DECLARE absPath VARCHAR(4000);
SET parentId := param1;
SET absPath := '';
while (parentId IS NOT NULL) DO
SELECT name, parent_id INTO acolumn, parentId FROM resources WHERE id = parentId;
SET absPath := concat('/',acolumn,absPath);
END while;
SET absPath := trim(trailing '/' FROM absPath);
RETURN absPath;
END;

CREATE OR REPLACE VIEW resource_hierarchical AS 
SELECT r.id resource_id, isPublished(id) published, isSecure(id) secure, abs_name(id) absolute_path FROM resources r;

-- resource_template_hierarchical

CREATE FUNCTION abs_template (param1 VARCHAR(36)) RETURNS longtext charSET utf8
BEGIN
DECLARE parentId VARCHAR(36);
DECLARE acolumn VARCHAR(1024);
DECLARE absPath VARCHAR(4000);
SET parentId := param1;
SET absPath := '';
while (parentId IS NOT NULL) DO
SELECT template_id, parent_id INTO acolumn, parentId FROM resources WHERE id = parentId;
SET absPath := concat('/',acolumn,absPath);
END while;
SET absPath := trim(trailing '/' FROM absPath);
RETURN absPath;
END;

CREATE OR REPLACE VIEW resource_template_hierarchical AS 
SELECT id resource_id,reverse(substr(reverse(abs_template(id)),1,instr(reverse(abs_template(id)),'/')-1)) template_id FROM resources;

-- resource_tags_hierarchical

CREATE OR REPLACE VIEW resource_tags_hierarchical AS 
SELECT DISTINCT r.id resource_id, t.tag_value, abs_path(id)
FROM resources r, resource_tags t 
WHERE abs_path(id) LIKE concat('%/',t.resource_id,'%');

ALTER TABLE searches DROP FOREIGN KEY FK_SEARCH_RESOURCE_ID;

DELETE FROM resources WHERE id IN (SELECT id FROM searches);

DELETE FROM searches;

DROP TABLE searches;

ALTER TABLE template_revisions ADD body_mime_type_primary varchar(255);

ALTER TABLE template_revisions ADD body_mime_type_sub varchar(255);
