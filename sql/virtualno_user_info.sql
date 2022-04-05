CREATE TABLE public.virtualno_user (
     id_virtualno_user varchar(32) NOT NULL DEFAULT sys_guid(),
     created_by varchar(100) NOT NULL,
     created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
     updated_by varchar(100) NOT NULL,
     updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
     username varchar(255) NOT NULL,
     nickname varchar(255) NOT NULL,
     password varchar(255) NOT NULL,
     contact varchar(255) NULL,
     constraint pk_virtualno_user primary key (id_virtualno_user)
);

COMMENT ON TABLE public.virtualno_user IS '用户表';
COMMENT ON COLUMN public.virtualno_user.id_virtualno_user IS '主键';
COMMENT ON COLUMN public.virtualno_user.created_by IS '创建人';
COMMENT ON COLUMN public.virtualno_user.created_date IS '创建时间';
COMMENT ON COLUMN public.virtualno_user.updated_by IS '更新人';
COMMENT ON COLUMN public.virtualno_user.updated_date IS '更新时间';
COMMENT ON COLUMN public.virtualno_user.username IS '用户名';
COMMENT ON COLUMN public.virtualno_user.nickname IS '昵称';
COMMENT ON COLUMN public.virtualno_user.password IS '密码';
COMMENT ON COLUMN public.virtualno_user.contact IS '联系方式';

create index idx_virtualno_user_username on public.virtualno_user using btree (username);
alter table public.virtualno_user owner to virtualno;
grant all on table public.virtualno_user to virtualno;



