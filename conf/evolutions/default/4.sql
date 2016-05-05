# --- !Ups

ALTER TABLE document ADD md_ark varchar(255);
ALTER TABLE document ADD d_ark varchar(255);
ALTER TABLE document ADD mets_d_ark varchar(255);

# Likely syntax required to make sensible URL fields - Downs needed.
# ALTER TABLE document ALTER COLUMN landing_page_url TYPE text;
# ALTER TABLE document ALTER COLUMN document_url TYPE text;




# --- !Downs

ALTER TABLE document DROP md_ark;
ALTER TABLE document DROP d_ark;
ALTER TABLE document DROP mets_d_ark;

