create or replace function flag_alert() returns trigger as $flag_alert$
declare
	id_creator bigint := (select author_id from target where id = new.target_id);
	flag_name varchar(255) := (select name from taxonomy where id = new.flag_id);
begin
	insert into alert values(nextval('alert_seq'), id_creator, 'flag set: ' || flag_name, now(), false);
	return new;
end;
$flag_alert$ language plpgsql;

create trigger flag_alert after insert on flag_target
for each row execute procedure flag_alert();