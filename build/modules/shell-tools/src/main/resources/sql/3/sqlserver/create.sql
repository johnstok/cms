UPDATE settings SET value='3' WHERE name='DATABASE_VERSION';

ALTER TABLE page_revision_paragraphs ADD index_text nvarchar(1024);
UPDATE page_revision_paragraphs SET index_text=substring(value_text, 1, 1024) WHERE type='TEXT';

ALTER TABLE folders DROP CONSTRAINT FK_FOLDER_INDEX_PAGE_ID;
ALTER TABLE folders ADD  CONSTRAINT FK_FOLDER_INDEX_PAGE_ID FOREIGN KEY (index_page) REFERENCES resources;

ALTER TABLE searches DROP FOREIGN KEY FK_SEARCH_RESOURCE_ID;
DELETE FROM resources WHERE id IN (SELECT id FROM searches);
DELETE FROM searches;
DROP TABLE searches;

ALTER TABLE page_revision_paragraphs ADD value_decimal DECIMAL(19,6);