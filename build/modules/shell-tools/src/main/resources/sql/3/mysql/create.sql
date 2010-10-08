UPDATE settings SET value='3' WHERE name='DATABASE_VERSION';

ALTER TABLE page_revision_paragraphs ADD index_text varchar(1024);
UPDATE page_revision_paragraphs SET index_text=substring(value_text, 1, 1024) WHERE type='TEXT';

ALTER TABLE folders DROP FOREIGN KEY FK_FOLDER_INDEX_PAGE_ID;
ALTER TABLE folders ADD CONSTRAINT fk_folder_index_page_id foreign key (index_page) REFERENCES resources (id);
