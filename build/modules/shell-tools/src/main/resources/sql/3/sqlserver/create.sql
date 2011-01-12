UPDATE settings SET value='3' WHERE name='DATABASE_VERSION';

ALTER TABLE page_revision_paragraphs ADD index_text nvarchar(1024);

ALTER TABLE page_revision_paragraphs ADD value_decimal decimal(19,6);

UPDATE page_revision_paragraphs SET index_text=substring(value_text, 1, 1024) WHERE type='TEXT';

ALTER TABLE folders DROP CONSTRAINT FK_FOLDER_INDEX_PAGE_ID; 

ALTER TABLE folders ADD CONSTRAINT FK_FOLDER_INDEX_PAGE_ID FOREIGN KEY (index_page) REFERENCES resources;

--CREATE VIEW resource_metadata_hierarchical AS
--WITH abs_path AS
--(
--	SELECT id, title, convert(varchar(1024), '/'+id) AS "path" ,0 AS "lvl"
--	FROM resources r
--	WHERE parent_id IS NULL
--	UNION ALL
--	SELECT r.id, r.title, convert(varchar(1024), ap.path+'/'+r.id), ap.lvl+1
--	FROM resources r
--	INNER JOIN abs_path AS ap ON r.parent_id = ap.id	
--)
--, resource_metadata_by_relevance AS (
--SELECT id AS resource_id, rm.datum_key, rm.datum_value, charindex(rm.resource_id, ap.path) relevance
--FROM abs_path ap, resource_metadata rm
--WHERE path LIKE '%'+rm.resource_id+'%'
--)
--SELECT meta.resource_id, meta.datum_key, meta.datum_value, meta.relevance
--FROM resource_metadata_by_relevance AS meta 
--LEFT JOIN resource_metadata_by_relevance AS meta2 ON meta.resource_id = meta2.resource_id 
--AND meta.relevance < meta2.relevance 
--WHERE meta2.resource_id IS NULL;

-- much faster but ending up having reversed relevance. i.e. the less the most relevant 

-- commit before creating views.

CREATE VIEW resource_metadata_hierarchical AS
WITH rr AS
(
	SELECT r.id, r.parent_id, rm.datum_value, rm.datum_key, r.id AS "forId", 1 AS "lvl"
	FROM resources r
	JOIN resource_metadata rm ON r.id = rm.resource_id
	UNION ALL
	SELECT r2.id, r2.parent_id, rm2.datum_value, rm2.datum_key, ra.forId, ra.lvl + 1
	FROM resources r2
	JOIN resource_metadata rm2 ON r2.id = rm2.resource_id
	JOIN rr ra ON r2.id = ra.parent_id	
)
SELECT DISTINCT forId AS "id", datum_key, datum_value, lvl AS "relevance"
FROM rr r
WHERE lvl = (SELECT MIN(lvl) FROM rr WHERE forId = r.forId AND datum_key = r.datum_key);

CREATE VIEW resource_hierarchical AS
WITH abs_path AS
(
	SELECT id, title  	
	, CONVERT(VARCHAR(1024), '/'+name) AS "name_path"
	, CASE published_by_id WHEN NULL THEN 0 ELSE 1 END "published"	
	, CASE WHEN EXISTS (SELECT * FROM resource_users u WHERE u.resource_id = r.id)
				AND EXISTS(SELECT * FROM resource_roles u WHERE u.resource_id = r.id)
			THEN 1 ELSE 0 END "secure"
	,0 AS "lvl"
	FROM resources r
	WHERE parent_id IS NULL
	UNION ALL
	SELECT r.id, r.title	
	,CONVERT(VARCHAR(1024), ap.name_path+'/'+r.name) AS "name_path"
	, CASE WHEN ap.published = 0 THEN 0 WHEN published_by_id IS NOT NULL THEN 1 ELSE 0 END "published"
	, CASE WHEN ap.secure = 1 THEN 1
		   WHEN EXISTS (SELECT * FROM resource_users u WHERE u.resource_id = r.id)
				AND EXISTS(SELECT * FROM resource_roles u WHERE u.resource_id = r.id)
			THEN 1 ELSE 0 END "secure"
	, ap.lvl+1
	FROM resources r
	INNER JOIN abs_path AS ap ON r.parent_id = ap.id	
)
SELECT id resource_id, published, secure, name_path AS absolute_path FROM abs_path;

CREATE VIEW resource_template_hierarchical AS 
WITH abs_path AS
(
	SELECT id, r.template_id template
	FROM resources r
	WHERE parent_id IS NULL
	UNION ALL
	SELECT r.id, CASE r.template_id WHEN NULL THEN ap.template ELSE r.template_id END template
	FROM resources r
	INNER JOIN abs_path AS ap ON r.parent_id = ap.id	
)
SELECT id resource_id, template FROM abs_path;

-- resource_tags_hierarchical

--CREATE VIEW resource_tags_hierarchical AS 
--WITH abs_path AS
--(
--	SELECT id, CONVERT(VARCHAR(1024), '/'+r.id) "path"
--	FROM resources r
--	WHERE parent_id IS NULL
--	UNION ALL
--	SELECT r.id, CONVERT(VARCHAR(1024), ap.path+'/'+r.id) "path"
--	FROM resources r
--	INNER JOIN abs_path AS ap ON r.parent_id = ap.id	
--)
--SELECT id resource_id, rt.tag_value FROM abs_path, resource_tags rt 
--where path like '%/'+rt.resource_id+'%';


-- Alternative much faster way to the above

CREATE VIEW resource_tags_hierarchical AS
WITH rr AS
(
	SELECT r.id, r.parent_id, rt.tag_value, r.id AS "forId", 1 AS "lvl"
	FROM resources r
	JOIN resource_tags rt ON r.id = rt.resource_id
	UNION ALL
	SELECT r2.id, r2.parent_id, rt2.tag_value, ra.forId, ra.lvl + 1
	FROM resources r2
	JOIN resource_tags rt2 ON r2.id = rt2.resource_id
	JOIN rr ra ON r2.id = ra.parent_id	
)
SELECT DISTINCT forId AS "id", tag_value
FROM rr r
WHERE lvl = (SELECT MIN(lvl) FROM rr WHERE forId = r.forId);

ALTER TABLE searches DROP CONSTRAINT FK_SEARCH_RESOURCE_ID;

DELETE FROM resources WHERE id IN (SELECT id FROM searches);

DELETE FROM searches;

DROP TABLE searches;

