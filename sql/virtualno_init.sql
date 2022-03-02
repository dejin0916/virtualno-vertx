create user virtualno with password '123456';
grant all privileges on database postgres  to virtualno;


create or replace function public.sys_guid()
    returns character  varying
    language plpgsql
    as
$function$ begin
    return replace(uuid_generate_v4()::text, '-','');
  end $function$;
