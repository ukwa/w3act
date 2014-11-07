# --- !Ups

drop sequence if exists lookup_entry_seq;
create sequence lookup_entry_seq start with 1;

# --- !Downs

create sequence lookup_entry_seq start;
drop sequence if exists lookup_entry_seq;

