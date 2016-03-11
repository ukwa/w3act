# --- !Ups

ALTER TABLE document ADD md_ark varchar(255);
ALTER TABLE document ADD d_ark varchar(255);
ALTER TABLE document ADD mets_d_ark varchar(255);


# --- !Downs

ALTER TABLE document DROP md_ark;
ALTER TABLE document DROP d_ark;
ALTER TABLE document DROP mets_d_ark;

