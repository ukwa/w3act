# --- !Ups

UPDATE public.mail_template
SET subject = 'UKWA Licence Received', updated_at = CURRENT_TIMESTAMP
WHERE subject = 'British Library UKWA Licence Received';

# --- !Downs

UPDATE public.mail_template
SET subject = 'British Library UKWA Licence Received', updated_at = CURRENT_TIMESTAMP
WHERE subject = 'UKWA Licence Received';

