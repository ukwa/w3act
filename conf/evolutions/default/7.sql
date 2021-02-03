# --- !Ups

ALTER TABLE target ADD COLUMN ignore_cookies BOOLEAN;
ALTER TABLE target ADD COLUMN crawl_rate VARCHAR(32);
ALTER TABLE target ADD COLUMN parallel_queues VARCHAR(32);

# --- !Downs

ALTER TABLE target DROP COLUMN ignore_cookies;
ALTER TABLE target DROP COLUMN crawl_rate;
ALTER TABLE target DROP COLUMN parallel_queues;
