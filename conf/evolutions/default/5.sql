# --- !Ups

INSERT INTO taxonomy (ttype, id, url, name, description, taxonomytype_id, created_at, updated_at)
    VALUES ('quality_issues', nextval('taxonomy_seq'), 'act-201603041', 'QA issues, but OK to publish', '<p>QA issues, but OK to publish</p>', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

# --- !Downs

DELETE FROM taxonomy WHERE url =  'act-201603041';

