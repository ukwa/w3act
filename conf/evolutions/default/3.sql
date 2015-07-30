# --- !Ups

ALTER TABLE crawl_permission ADD granted_at timestamp;
ALTER TABLE crawl_permission ADD requested_at timestamp;
ALTER TABLE creator ADD last_login timestamp;
ALTER TABLE contact_person ADD CONSTRAINT uq_contact_person_email UNIQUE (email);

# --- !Downs

ALTER TABLE crawl_permission DROP granted_at;
ALTER TABLE crawl_permission DROP requested_at;
ALTER TABLE creator DROP last_login;
ALTER TABLE contact_person DROP CONSTRAINT uq_contact_person_email;
