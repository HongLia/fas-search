package com.fas.search.manage.entity;

import java.util.Date;

public class ZsEntityField {
    private String id;

    private String ename;

    private String cname;

    private String tableename;

    private String fieldename;

    private String entity_id;

    private Date create_time;

    private String updator;

    private Date update_time;

    private String creator;

    private String enable;

    private String field_type;

    private Integer thesort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getTableename() {
        return tableename;
    }

    public void setTableename(String tableename) {
        this.tableename = tableename == null ? null : tableename.trim();
    }

    public String getFieldename() {
        return fieldename;
    }

    public void setFieldename(String fieldename) {
        this.fieldename = fieldename == null ? null : fieldename.trim();
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id == null ? null : entity_id.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type == null ? null : field_type.trim();
    }

    public Integer getThesort() {
        return thesort;
    }

    public void setThesort(Integer thesort) {
        this.thesort = thesort;
    }
}