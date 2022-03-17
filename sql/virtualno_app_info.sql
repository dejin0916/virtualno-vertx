CREATE TABLE public.virtualno_app_info (
    id_virtualno_app_info varchar(32) NOT NULL DEFAULT sys_guid(),
    created_by varchar(100) NOT NULL,
    created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    updated_by varchar(100) NOT NULL,
    updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    app_id varchar(20) NOT NULL,
    app_name varchar(100) NULL,
    app_key varchar(32) NOT NULL,
    secret varchar(32) NOT NULL,
    ext_info jsonb NULL,
    constraint pk_virtualno_app_info primary key (id_virtualno_app_info)
);
COMMENT ON TABLE public.virtualno_app_info IS 'app接入方信息';

create unique index idx_virtualno_app_info_id on public.virtualno_app_info using btree (app_id);

alter table public.virtualno_app_info owner to virtualno;
grant all on table public.virtualno_app_info to virtualno;
