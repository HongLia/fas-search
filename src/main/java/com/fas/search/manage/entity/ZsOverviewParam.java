package com.fas.search.manage.entity;

import java.util.Date;

public class ZsOverviewParam {
    private Integer id;

    private String entity_id;

    private String subject_id;

    private String attename;

    private String diccname;

    private String creator;

    private Date create_time;

    private String updator;

    private Date update_time;

    private Integer thesort;

    private String dic_id;

    private String overview_id;

    private String entity_field_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id == null ? null : entity_id.trim();
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id == null ? null : subject_id.trim();
    }

    public String getAttename() {
        return attename;
    }

    public void setAttename(String attename) {
        this.attename = attename == null ? null : attename.trim();
    }

    public String getDiccname() {
        return diccname;
    }

    public void setDiccname(String diccname) {
        this.diccname = diccname == null ? null : diccname.trim();
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

    public Integer getThesort() {
        return thesort;
    }

    public void setThesort(Integer thesort) {
        this.thesort = thesort;
    }

    public String getDic_id() {
        return dic_id;
    }

    public void setDic_id(String dic_id) {
        this.dic_id = dic_id == null ? null : dic_id.trim();
    }

    public String getOverview_id() {
        return overview_id;
    }

    public void setOverview_id(String overview_id) {
        this.overview_id = overview_id == null ? null : overview_id.trim();
    }

    public String getEntity_field_id() {
        return entity_field_id;
    }

    public void setEntity_field_id(String entity_field_id) {
        this.entity_field_id = entity_field_id == null ? null : entity_field_id.trim();
    }
}