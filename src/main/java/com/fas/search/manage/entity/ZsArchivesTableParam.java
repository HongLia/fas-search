package com.fas.search.manage.entity;

import java.util.Date;

public class ZsArchivesTableParam {
    private String id;

    private String overview_field;

    private String showname;

    private String cname;

    private String ename;

    private String detail_field;

    private Integer thesort;

    private String subject_id;

    private String entity_id;

    private String updator;

    private Date update_time;

    private String enable;

    private String creator;

    private Date create_time;

    private String entity_field_id;

    private String archive_entity_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOverview_field() {
        return overview_field;
    }

    public void setOverview_field(String overview_field) {
        this.overview_field = overview_field == null ? null : overview_field.trim();
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname == null ? null : showname.trim();
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename == null ? null : ename.trim();
    }

    public String getDetail_field() {
        return detail_field;
    }

    public void setDetail_field(String detail_field) {
        this.detail_field = detail_field == null ? null : detail_field.trim();
    }

    public Integer getThesort() {
        return thesort;
    }

    public void setThesort(Integer thesort) {
        this.thesort = thesort;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id == null ? null : subject_id.trim();
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id == null ? null : entity_id.trim();
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

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getEntity_field_id() {
        return entity_field_id;
    }

    public void setEntity_field_id(String entity_field_id) {
        this.entity_field_id = entity_field_id == null ? null : entity_field_id.trim();
    }

    public String getArchive_entity_id() {
        return archive_entity_id;
    }

    public void setArchive_entity_id(String archive_entity_id) {
        this.archive_entity_id = archive_entity_id;
    }
}