CREATE TABLE public.virtualno_ax_rel (
    id_virtualno_ax varchar(32) NOT NULL DEFAULT sys_guid(),
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
    source varchar(10) NULL,
    ext_info jsonb null,
    constraint pk_virtualno_ax primary key (id_virtualno_ax)
);
COMMENT ON TABLE public.virtualno_ax_rel IS 'AX绑定关系表';

create index idx_virtualno_ax_rel_real_num on public.virtualno_ax_rel using btree (real_number);
create index idx_virtualno_ax_rel_virtual_num on public.virtualno_ax_rel using btree (virtual_number);
create index idx_virtualno_ax_rel_biz_id on public.virtualno_ax_rel using btree (business_id);
create index idx_virtualno_ax_rel_biz_type on public.virtualno_ax_rel using btree (business_type);