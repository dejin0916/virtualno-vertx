CREATE TABLE public.virtualno_yb_rel (
    id_virtualno_yb varchar(32) NOT NULL DEFAULT sys_guid(),
    id_virtualno_ax varchar(32) NOT NULL,
    created_by varchar(100) NOT NULL,
    created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    updated_by varchar(100) NOT NULL,
    updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    unbind_plan_date timestamp NULL,
    unbind_real_date timestamp NULL,
    business_id varchar(50) NULL,
    business_type varchar(50) NULL,
    real_number varchar(20) NULL,
    virtual_number varchar(50) NULL,
    app_id varchar(20) NULL,
    serial_number varchar(32) NULL,
    area_code varchar(10) NULL,
    constraint pk_virtualno_yb primary key (id_virtualno_yb)
);
COMMENT ON TABLE public.virtualno_yb_rel IS 'YB绑定关系表';

COMMENT ON COLUMN public.virtualno_yb_rel.id_virtualno_yb IS '主键';
COMMENT ON COLUMN public.virtualno_yb_rel.id_virtualno_ax IS '关联AX关系的主键';
COMMENT ON COLUMN public.virtualno_yb_rel.created_by IS '创建人';
COMMENT ON COLUMN public.virtualno_yb_rel.created_date IS '创建时间';
COMMENT ON COLUMN public.virtualno_yb_rel.updated_by IS '更新人';
COMMENT ON COLUMN public.virtualno_yb_rel.updated_date IS '更新时间';
COMMENT ON COLUMN public.virtualno_yb_rel.unbind_plan_date IS '计划解绑时间';
COMMENT ON COLUMN public.virtualno_yb_rel.unbind_real_date IS '实际解绑时间';
COMMENT ON COLUMN public.virtualno_yb_rel.business_id IS '业务ID';
COMMENT ON COLUMN public.virtualno_yb_rel.business_type IS '业务类型';
COMMENT ON COLUMN public.virtualno_yb_rel.real_number IS '真实号码';
COMMENT ON COLUMN public.virtualno_yb_rel.virtual_number IS '虚拟号码（隐私号）';
COMMENT ON COLUMN public.virtualno_yb_rel.app_id IS '接入方ID';
COMMENT ON COLUMN public.virtualno_yb_rel.serial_number IS '号码池编号';
COMMENT ON COLUMN public.virtualno_yb_rel.area_code IS '区域编码';


create index idx_virtualno_yb_rel_ax_id on public.virtualno_yb_rel using btree (id_virtualno_ax);
create index idx_virtualno_yb_rel_real_num on public.virtualno_yb_rel using btree (real_number);
create index idx_virtualno_yb_rel_virtual_num on public.virtualno_yb_rel using btree (virtual_number);
create index idx_virtualno_yb_rel_biz_id on public.virtualno_yb_rel using btree (business_id);
create index idx_virtualno_yb_rel_biz_type on public.virtualno_yb_rel using btree (business_type);

alter table public.virtualno_yb_rel owner to virtualno;
grant all on table public.virtualno_yb_rel to virtualno;
