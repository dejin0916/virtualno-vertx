CREATE TABLE public.virtualno_axb_rel (
    id_virtualno_axb varchar(32) NOT NULL DEFAULT sys_guid(),
    created_by varchar(100) NOT NULL,
    created_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    updated_by varchar(100) NOT NULL,
    updated_date timestamp NOT NULL DEFAULT 'NOW'::text::timestamp(0) with time zone,
    unbind_plan_date timestamp NULL,
    unbind_real_date timestamp NULL,
    business_id varchar(50) NULL,
    business_type varchar(50) NULL,
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
COMMENT ON COLUMN public.virtualno_axb_rel.business_type IS '业务类型';
COMMENT ON COLUMN public.virtualno_axb_rel.real_number_a IS '真实号码A';
COMMENT ON COLUMN public.virtualno_axb_rel.real_number_b IS '真实号码B';
COMMENT ON COLUMN public.virtualno_axb_rel.virtual_number IS '虚拟号码（隐私号）';
COMMENT ON COLUMN public.virtualno_axb_rel.app_id IS '接入方ID';
COMMENT ON COLUMN public.virtualno_axb_rel.serial_number IS '号码池编号';
COMMENT ON COLUMN public.virtualno_axb_rel.area_code IS '区域编码';


create index idx_virtualno_axb_rel_real_num_a on public.virtualno_axb_rel using btree (real_number_a);
create index idx_virtualno_axb_rel_real_num_b on public.virtualno_axb_rel using btree (real_number_b);
create index idx_virtualno_axb_rel_virtual_num on public.virtualno_axb_rel using btree (virtual_number);
create index idx_virtualno_axb_rel_biz_id on public.virtualno_axb_rel using btree (business_id);
create index idx_virtualno_axb_rel_biz_type on public.virtualno_axb_rel using btree (business_type);