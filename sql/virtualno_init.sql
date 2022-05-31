-- 创建用户
create user virtualno with password '123456';
grant all privileges on database postgres  to virtualno;

-- 添加sys_guid生成
create extension "uuid-ossp";
create or replace function public.sys_guid()
    returns character  varying
    language plpgsql
    as
$function$ begin
    return replace(uuid_generate_v4()::text, '-','');
  end $function$;

----------------------------------------------------------------------------------------------------------------
-- app接入信息
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
COMMENT ON TABLE public.virtualno_app_info IS '应用接入信息';
COMMENT ON COLUMN public.virtualno_app_info.id_virtualno_app_info IS '主键';
COMMENT ON COLUMN public.virtualno_app_info.created_by IS '创建人';
COMMENT ON COLUMN public.virtualno_app_info.created_date IS '创建时间';
COMMENT ON COLUMN public.virtualno_app_info.updated_by IS '更新人';
COMMENT ON COLUMN public.virtualno_app_info.updated_date IS '更新时间';
COMMENT ON COLUMN public.virtualno_app_info.app_id IS '应用Id';
COMMENT ON COLUMN public.virtualno_app_info.app_name IS '应用名';
COMMENT ON COLUMN public.virtualno_app_info.app_key IS 'key';
COMMENT ON COLUMN public.virtualno_app_info.secret IS 'secret';
COMMENT ON COLUMN public.virtualno_app_info.ext_info IS '附加信息';

create unique index idx_virtualno_app_info_id on public.virtualno_app_info using btree (app_id);

alter table public.virtualno_app_info owner to virtualno;
grant all on table public.virtualno_app_info to virtualno;

----------------------------------------------------------------------------------------------------------------
-- ax关系表
CREATE TABLE public.virtualno_ax_rel (
   id_virtualno_ax varchar(32) NOT NULL DEFAULT sys_guid(),
   created_by varchar(100) NOT NULL,
   created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
   updated_by varchar(100) NOT NULL,
   updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
   unbind_plan_date timestamp NULL,
   unbind_real_date timestamp NULL,
   business_id varchar(50) NULL,
   real_number varchar(20) NULL,
   virtual_number varchar(50) NULL,
   app_id varchar(20) NULL,
   serial_number varchar(32) NULL,
   area_code varchar(10) NULL,
   source varchar(10) NULL,
   ext_info jsonb null,
   constraint pk_virtualno_ax primary key (id_virtualno_ax)
);
COMMENT ON TABLE public.virtualno_ax_rel IS 'AX绑定关系表';
COMMENT ON COLUMN public.virtualno_ax_rel.id_virtualno_ax IS '主键';
COMMENT ON COLUMN public.virtualno_ax_rel.created_by IS '创建人';
COMMENT ON COLUMN public.virtualno_ax_rel.created_date IS '创建时间';
COMMENT ON COLUMN public.virtualno_ax_rel.updated_by IS '更新人';
COMMENT ON COLUMN public.virtualno_ax_rel.updated_date IS '更新时间';
COMMENT ON COLUMN public.virtualno_ax_rel.unbind_plan_date IS '计划解绑时间';
COMMENT ON COLUMN public.virtualno_ax_rel.unbind_real_date IS '实际解绑时间';
COMMENT ON COLUMN public.virtualno_ax_rel.business_id IS '业务ID';
COMMENT ON COLUMN public.virtualno_ax_rel.real_number IS '真实号码';
COMMENT ON COLUMN public.virtualno_ax_rel.virtual_number IS '虚拟号码（隐私号）';
COMMENT ON COLUMN public.virtualno_ax_rel.app_id IS '接入方ID';
COMMENT ON COLUMN public.virtualno_ax_rel.serial_number IS '号码池编号';
COMMENT ON COLUMN public.virtualno_ax_rel.area_code IS '区号';

create index idx_virtualno_ax_rel_real_num on public.virtualno_ax_rel using btree (real_number);
create index idx_virtualno_ax_rel_virtual_num on public.virtualno_ax_rel using btree (virtual_number);
create index idx_virtualno_ax_rel_biz_id on public.virtualno_ax_rel using btree (business_id);
create index idx_virtualno_ax_rel_biz_type on public.virtualno_ax_rel using btree (business_type);

alter table public.virtualno_ax_rel owner to virtualno;
grant all on table public.virtualno_ax_rel to virtualno;

----------------------------------------------------------------------------------------------------------------
-- axb关系表
CREATE TABLE public.virtualno_axb_rel (
    id_virtualno_axb varchar(32) NOT NULL DEFAULT sys_guid(),
    created_by varchar(100) NOT NULL,
    created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    updated_by varchar(100) NOT NULL,
    updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    unbind_plan_date timestamp NULL,
    unbind_real_date timestamp NULL,
    business_id varchar(50) NULL,
    real_number_a varchar(20) NULL,
    real_number_b varchar(20) NULL,
    virtual_number varchar(20) NULL,
    app_id varchar(20) NULL,
    serial_number varchar(32) NULL,
    area_code varchar(10) NULL,
    constraint pk_virtualno_axb primary key (id_virtualno_axb)
);

COMMENT ON TABLE public.virtualno_axb_rel IS 'AXB绑定关系表';

COMMENT ON COLUMN public.virtualno_axb_rel.id_virtualno_axb IS '主键';
COMMENT ON COLUMN public.virtualno_axb_rel.created_by IS '创建人';
COMMENT ON COLUMN public.virtualno_axb_rel.created_date IS '创建时间';
COMMENT ON COLUMN public.virtualno_axb_rel.updated_by IS '更新人';
COMMENT ON COLUMN public.virtualno_axb_rel.updated_date IS '更新时间';
COMMENT ON COLUMN public.virtualno_axb_rel.unbind_plan_date IS '计划解绑时间';
COMMENT ON COLUMN public.virtualno_axb_rel.unbind_real_date IS '实际解绑时间';
COMMENT ON COLUMN public.virtualno_axb_rel.business_id IS '业务ID';
COMMENT ON COLUMN public.virtualno_axb_rel.real_number_a IS '真实号码A';
COMMENT ON COLUMN public.virtualno_axb_rel.real_number_b IS '真实号码B';
COMMENT ON COLUMN public.virtualno_axb_rel.virtual_number IS '虚拟号码（隐私号）';
COMMENT ON COLUMN public.virtualno_axb_rel.app_id IS '接入方ID';
COMMENT ON COLUMN public.virtualno_axb_rel.serial_number IS '号码池编号';
COMMENT ON COLUMN public.virtualno_axb_rel.area_code IS '区号';


create index idx_virtualno_axb_rel_real_num_a on public.virtualno_axb_rel using btree (real_number_a);
create index idx_virtualno_axb_rel_real_num_b on public.virtualno_axb_rel using btree (real_number_b);
create index idx_virtualno_axb_rel_virtual_num on public.virtualno_axb_rel using btree (virtual_number);
create index idx_virtualno_axb_rel_biz_id on public.virtualno_axb_rel using btree (business_id);

alter table public.virtualno_axb_rel owner to virtualno;
grant all on table public.virtualno_axb_rel to virtualno;

----------------------------------------------------------------------------------------------------------------
--管理端用户表
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

COMMENT ON TABLE public.virtualno_user IS '管理端用户表';
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

----------------------------------------------------------------------------------------------------------------
-- 号码池表
CREATE TABLE public.virtualno_pool (
   id_virtualno_pool varchar(32) NOT NULL DEFAULT sys_guid(),
   created_by varchar(100) NOT NULL,
   created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
   updated_by varchar(100) NOT NULL,
   updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
   serial_number varchar(32) NULL,
   description varchar(200) NULL,
   constraint pk_virtualno_pool primary key (id_virtualno_pool),
   constraint uk_serial_number unique(serial_number)
);
COMMENT ON TABLE public.virtualno_pool IS '虚拟号号码池';
COMMENT ON COLUMN public.virtualno_pool.id_virtualno_pool IS '主键';
COMMENT ON COLUMN public.virtualno_pool.created_by IS '创建人';
COMMENT ON COLUMN public.virtualno_pool.created_date IS '创建时间';
COMMENT ON COLUMN public.virtualno_pool.updated_by IS '更新人';
COMMENT ON COLUMN public.virtualno_pool.updated_date IS '更新时间';
COMMENT ON COLUMN public.virtualno_pool.serial_number IS '号码池编号';
COMMENT ON COLUMN public.virtualno_pool.description IS '描述信息';


create index idx_virtualno_pool_serial_num on public.virtualno_pool using btree (serial_number);

alter table public.virtualno_pool owner to virtualno;
grant all on table public.virtualno_pool to virtualno;

----------------------------------------------------------------------------------------------------------------
-- 号码池号码表
CREATE TABLE public.virtualno_pool_number (
    id_virtualno_pool_number varchar(32) NOT NULL DEFAULT sys_guid(),
    created_by varchar(100) NOT NULL,
    created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    updated_by varchar(100) NOT NULL,
    updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    serial_number varchar(32) NULL,
    virtual_number varchar(32) NULL,
    area_code varchar(10) NULL,
    enable boolean NOT NULL DEFAULT true,
    constraint pk_virtualno_pool_num primary key (id_virtualno_pool_number),
    constraint uk_virtual_number unique(virtual_number)
);
COMMENT ON TABLE public.virtualno_pool_number IS '号码池号码信息';
COMMENT ON COLUMN public.virtualno_pool_number.id_virtualno_pool_number IS '主键';
COMMENT ON COLUMN public.virtualno_pool_number.created_by IS '创建人';
COMMENT ON COLUMN public.virtualno_pool_number.created_date IS '创建时间';
COMMENT ON COLUMN public.virtualno_pool_number.updated_by IS '更新人';
COMMENT ON COLUMN public.virtualno_pool_number.updated_date IS '更新时间';
COMMENT ON COLUMN public.virtualno_pool_number.serial_number IS '号码池编号';
COMMENT ON COLUMN public.virtualno_pool_number.virtual_number IS '虚拟号';
COMMENT ON COLUMN public.virtualno_pool_number.area_code IS '区号';
COMMENT ON COLUMN public.virtualno_pool_number.enable IS '是否可用';

create index idx_virtualno_pn_vn on public.virtualno_pool_number using btree (virtual_number);

alter table public.virtualno_pool_number owner to virtualno;
grant all on table public.virtualno_pool_number to virtualno;
alter table public.virtualno_pool_number add column virtual_type varchar(5);
COMMENT ON COLUMN public.virtualno_pool_number.virtual_type IS '虚拟号类型，AX, AXB';

----------------------------------------------------------------------------------------------------------------
