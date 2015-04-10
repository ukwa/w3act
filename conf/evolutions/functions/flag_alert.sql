create or replace function flag_alert() returns trigger as $flag_alert$
declare
	id_creator bigint;
	title varchar(255);
	flag_name varchar(255) := (select name from taxonomy where id = new.flag_id);
begin
	select author_id, target.title
	into id_creator, title
	from target where id = new.target_id;
	
	insert into alert values(nextval('alert_seq'), id_creator, 'flag ' || flag_name || ' for target ' ||
	'<a href="/targets/' || new.target_id || '">' || title || '</a> set' , now(), false);
	return new;
end;
$flag_alert$ language plpgsql;

create trigger flag_alert after insert on flag_target
for each row execute procedure flag_alert();