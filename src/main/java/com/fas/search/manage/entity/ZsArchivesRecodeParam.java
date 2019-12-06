package com.fas.search.manage.entity;

import java.util.Date;

public class ZsArchivesRecodeParam {
    private String id;

    private String showContent;

    private String entity_id;

    private String archives_id;

    private Date create_time;

    private String updator;

    private Date update_time;

    private String enable;

    private String creator;

    private Integer thesort;

    private String archive_entity_id;

    private String subject_id;

    private String search_field;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent == null ? null : showContent.trim();
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

    public Integer getThesort() {
        return thesort;
    }

    public void setThesort(Integer thesort) {
        this.thesort = thesort;
    }

    public String getArchive_entity_id() {
        return archive_entity_id;
    }

    public void setArchive_entity_id(String archive_entity_id) {
        this.archive_entity_id = archive_entity_id == null ? null : archive_entity_id.trim();
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id == null ? null : subject_id.trim();
    }

    public String getSearch_field() {
        return search_field;
    }

    public void setSearch_field(String search_field) {
        this.search_field = search_field;
    }
}