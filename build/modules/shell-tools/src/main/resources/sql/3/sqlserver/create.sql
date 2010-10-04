UPDATE settings SET value='3' WHERE name='DATABASE_VERSION';

ALTER TABLE page_revision_paragraphs ADD index_text nvarchar(1024);
UPDATE page_revision_paragraphs SET index_text=substring(value_text, 1, 1024) WHERE type='TEXT';

ALTER TABLE folders DROP CONSTRAINT fk_folder_index_page_id;
ALTER TABLE folders ADD  CONSTRAINT fk_folder_index_page_id FOREIGN KEY (index_page) REFERENCES resources;