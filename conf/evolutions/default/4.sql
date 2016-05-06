# --- !Ups

ALTER TABLE document ADD md_ark varchar(255);
ALTER TABLE document ADD d_ark varchar(255);
ALTER TABLE document ADD mets_d_ark varchar(255);

ALTER TABLE document ALTER COLUMN landing_page_url TYPE text;
ALTER TABLE document ALTER COLUMN document_url TYPE text;

ALTER TABLE watched_target ALTER COLUMN document_url_scheme TYPE text;
ALTER TABLE watched_target ALTER COLUMN login_page_url TYPE text;
ALTER TABLE watched_target ALTER COLUMN logout_url TYPE text;



# --- !Downs

ALTER TABLE document DROP md_ark;
ALTER TABLE document DROP d_ark;
ALTER TABLE document DROP mets_d_ark;

ALTER TABLE document ALTER COLUMN landing_page_url TYPE varchar(255);
ALTER TABLE watched_target ALTER COLUMN document_url TYPE varchar(255);

ALTER TABLE watched_target ALTER COLUMN document_url_scheme TYPE varchar(255);
ALTER TABLE watched_target ALTER COLUMN login_page_url TYPE varchar(255);
ALTER TABLE watched_target ALTER COLUMN logout_url TYPE varchar(255);
