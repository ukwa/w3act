
--Insert it if its not already there
INSERT INTO taxonomy (ttype, id, url, name, description, taxonomytype_id, created_at, updated_at)
       SELECT 'quality_issues', nextval('taxonomy_seq'), 'act-201603041', 'QA issues, but OK to publish', '<p>QA issues, but OK to publish</p>', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
       WHERE NOT EXISTS (SELECT 1 FROM taxonomy WHERE url='act-201603041');

--Just insert it 
--INSERT INTO taxonomy (ttype, id, url, name, description, taxonomytype_id, created_at, updated_at)
--    VALUES ('quality_issues', nextval('taxonomy_seq'), 'act-201603041', 'QA issues, but OK to publish', '<p>QA issues, but OK to publish</p>', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
	
--To Remove	the new QA Status
--DELETE FROM taxonomy WHERE url =  'act-201603041';

