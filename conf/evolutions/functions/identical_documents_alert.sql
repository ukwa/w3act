create or replace function identical_documents_alert() returns trigger as $identical_documents_alert$
declare
	id_creator bigint;
	id bigint;
	title text;
begin
	if old.sha256hash is null then
		select d.id, d.title, author_id
		into id, title, id_creator
		from document d
		inner join watched_target wt on wt.id = id_watched_target
		inner join target t on t.id = id_target
		where sha256hash = new.sha256hash
		and d.id != old.id
		limit 1;
		
		if id is not null then
			insert into alert values(nextval('alert_seq'), id_creator, 'the documents ' ||
			'<a href="/documents/' || new.id || '">' || new.title || '</a> and ' ||
			'<a href="/documents/' || id || '">' || title || '</a>' ||
			' seem to be identical (' ||
			'<a href="/documents/compare/' || new.id || '..' || id || '">compare</a>' ||
			')', now(), false);
		end if;
	end if;
	return new;
end;
$identical_documents_alert$ language plpgsql;

create trigger identical_documents_alert after update on document
for each row execute procedure identical_documents_alert();