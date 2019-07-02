package com.fas.search.manage.entity;

import java.util.Date;

public class ZsOverviewTemplate {
    private String id;

    private String dicename;

    private String diccname;

    private String template_type;

    private String subject_id;

    private String updator;

    private Date update_time;

    private String creator;

    private Date create_time;

    private Integer thesort;

    private String dic_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDicename() {
        return dicename;
    }

    public void setDicename(String dicename) {
        this.dicename = dicename == null ? null : dicename.trim();
    }

    public String getDiccname() {
        return diccname;
    }

    public void setDiccname(String diccname) {
        this.diccname = diccname == null ? null : diccname.trim();
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type == null ? null : template_type.trim();
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id == null ? null : subject_id.trim();
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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
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
}