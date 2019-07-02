package com.fas.search.manage.entity;

import java.util.Date;

public class ZsEntityArchives {
    private String id;

    private String entity_id;

    private String archives_id;

    private String creator;

    private Date create_time;

    private Integer thesort;

    private String enable;

    private String show_source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id == null ? null : entity_id.trim();
    }

    public String getArchives_id() {
        return archives_id;
    }

    public void setArchives_id(String archives_id) {
        this.archives_id = archives_id == null ? null : archives_id.trim();
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

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }

    public String getShow_source() {
        return show_source;
    }

    public void setShow_source(String show_source) {
        this.show_source = show_source == null ? null : show_source.trim();
    }
}