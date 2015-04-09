create or replace function many_documents_alert() returns boolean as $$
declare
	count_cursor cursor for
	select author_id, t.title, id_target, id_watched_target, count(*) as number_documents
	from document d
	inner join watched_target wt on wt.id = id_watched_target
	inner join target t on t.id = id_target
	where sha256hash is null
	group by author_id, t.title, id_target, id_watched_target, d.wayback_timestamp
	having count(*) > 300;
begin
	for r in count_cursor loop
		insert into alert values(nextval('alert_seq'), r.author_id,
		r.number_documents || ' new documents found for target ' ||
		'<a href="/targets/' || r.id_target || '">' || r.title || '</a>',
		now(), false);
	end loop;
	return true;
end;
$$ language plpgsql;
