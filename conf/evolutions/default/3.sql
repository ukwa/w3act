# --- !Ups

ALTER TABLE crawl_permission ADD granted_at timestamp;
ALTER TABLE crawl_permission ADD requested_at timestamp;
ALTER TABLE creator ADD last_login timestamp;
ALTER TABLE contact_person ALTER COLUMN email SET DATA TYPE varchar(255);
--ALTER TABLE contact_person ADD CONSTRAINT uq_contact_person_email UNIQUE (email);
ALTER TABLE watched_target ADD archivist_notes_wt text;
ALTER TABLE target ADD second_language varchar(255);
ALTER TABLE instance ADD second_language varchar(255);
ALTER TABLE taxonomy ADD start_date timestamp;
ALTER TABLE taxonomy ADD end_date timestamp;
ALTER TABLE target ADD target_start_date timestamp;
ALTER TABLE target ADD target_end_date timestamp;
ALTER TABLE target ADD document_owner_id bigint;
ALTER TABLE instance ADD crawl_seeds text;
ALTER TABLE instance ADD crawl_scheduled_start_date timestamp;
ALTER TABLE instance ADD crawl_actual_start_date timestamp;
ALTER TABLE instance ADD crawl_duration_millis bigint;
ALTER TABLE instance ADD crawl_bytes_downloaded bigint;
ALTER TABLE instance ADD crawl_urls_downloaded bigint;
ALTER TABLE instance ADD crawl_urls_failed bigint;
ALTER TABLE instance ADD crawl_additional_information text;

CREATE TABLE highlight (
  id				bigint primary key,
  taxonomy_id		bigint not null references taxonomy (id),
  url				varchar(255),
  start_date		timestamp,
  end_date			timestamp
);

CREATE TABLE collection_instance (
  collection_id                      bigint not null,
  instance_id                        bigint not null,
  constraint pk_collection_instance primary key (collection_id, instance_id)
);


INSERT INTO taxonomy (ttype, id, url, created_at, name, description, updated_at)
    VALUES ('flags', 369, 'act-369', '2015-07-31 10:29:13.714', 'Video Content to add', 'This flag marks video content to add', '2015-07-31 10:29:13.714');


UPDATE crawl_permission SET contactperson_id = 825 WHERE contactperson_id = 824;
DELETE FROM contact_person WHERE id = 824 AND email = 'info@ukjs.org';

UPDATE crawl_permission SET contactperson_id = 1892 WHERE contactperson_id = 1891;
DELETE FROM contact_person WHERE id = 1891 AND email = '    ds';



# --- !Downs

ALTER TABLE crawl_permission DROP granted_at;
ALTER TABLE crawl_permission DROP requested_at;
ALTER TABLE creator DROP last_login;
--ALTER TABLE contact_person DROP CONSTRAINT uq_contact_person_email;
ALTER TABLE contact_person ALTER COLUMN email SET DATA TYPE text;
ALTER TABLE watched_target DROP archivist_notes_wt;
ALTER TABLE target DROP second_language;
ALTER TABLE instance DROP second_language;
ALTER TABLE taxonomy DROP start_date;
ALTER TABLE taxonomy DROP end_date;
ALTER TABLE target DROP target_start_date;
ALTER TABLE target DROP target_end_date;
ALTER TABLE target DROP document_owner_id;
ALTER TABLE instance DROP crawl_seeds;
ALTER TABLE instance DROP crawl_scheduled_start_date;
ALTER TABLE instance DROP crawl_actual_start_date;
ALTER TABLE instance DROP crawl_duration_millis;
ALTER TABLE instance DROP crawl_bytes_downloaded;
ALTER TABLE instance DROP crawl_urls_downloaded;
ALTER TABLE instance DROP crawl_urls_failed;
ALTER TABLE instance DROP crawl_additional_information;

DROP TABLE IF EXISTS highlight;
DROP TABLE IF EXISTS collection_instance;

DELETE FROM taxonomy WHERE ttype = 'flags' AND name = 'Video Content to add';

