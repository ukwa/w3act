UPDATE crawl_permission SET contactperson_id = 825 WHERE contactperson_id = 824;
DELETE FROM contact_person WHERE id = 824 AND email = 'info@ukjs.org';

UPDATE crawl_permission SET contactperson_id = 1892 WHERE contactperson_id = 1891;
DELETE FROM contact_person WHERE id = 1891 AND email = '    ds';