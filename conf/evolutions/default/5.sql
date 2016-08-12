# --- !Ups

ALTER TABLE public.document ADD COLUMN title2 text;

ALTER TABLE public.book ADD COLUMN print_isbn character varying(255);
ALTER TABLE public.book ADD COLUMN  corporate_author_subordinate_unit character varying(255);
ALTER TABLE public.book ADD COLUMN  corporate_author2 character varying(255);
ALTER TABLE public.book ADD COLUMN  corporate_author2_subordinate_unit character varying(255);
ALTER TABLE public.book ADD COLUMN  corporate_author3 character varying(255);
ALTER TABLE public.book ADD COLUMN  corporate_author3_subordinate_unit character varying(255);
ALTER TABLE public.book ADD COLUMN  government_body character varying(255);
ALTER TABLE public.book ADD COLUMN  government_body_subordinate_unit character varying(255);
ALTER TABLE public.book ADD COLUMN  government_body2 character varying(255);
ALTER TABLE public.book ADD COLUMN  government_body2_subordinate_unit character varying(255);
ALTER TABLE public.book ADD COLUMN  government_body3 character varying(255);
ALTER TABLE public.book ADD COLUMN  government_body3_subordinate_unit character varying(255);
ALTER TABLE public.book ADD COLUMN  part_number character varying(255);
ALTER TABLE public.book ADD COLUMN  part_name character varying(255);

UPDATE public.mail_template
SET subject = 'UKWA Licence Received', updated_at = CURRENT_TIMESTAMP
WHERE subject = 'British Library UKWA Licence Received';

# --- !Downs

UPDATE public.mail_template
SET subject = 'British Library UKWA Licence Received', updated_at = CURRENT_TIMESTAMP
WHERE subject = 'UKWA Licence Received';

ALTER TABLE public.book DROP COLUMN print_isbn;
ALTER TABLE public.book DROP COLUMN  corporate_author_subordinate_unit;
ALTER TABLE public.book DROP COLUMN  corporate_author2;
ALTER TABLE public.book DROP COLUMN  corporate_author2_subordinate_unit;
ALTER TABLE public.book DROP COLUMN  corporate_author3;
ALTER TABLE public.book DROP COLUMN  corporate_author3_subordinate_unit;
ALTER TABLE public.book DROP COLUMN  government_body;
ALTER TABLE public.book DROP COLUMN  government_body_subordinate_unit;
ALTER TABLE public.book DROP COLUMN  government_body2;
ALTER TABLE public.book DROP COLUMN  government_body2_subordinate_unit;
ALTER TABLE public.book DROP COLUMN  government_body3;
ALTER TABLE public.book DROP COLUMN  government_body3_subordinate_unit;
ALTER TABLE public.book DROP COLUMN  part_number;
ALTER TABLE public.book DROP COLUMN  part_name;

ALTER TABLE public.document DROP COLUMN title2;


