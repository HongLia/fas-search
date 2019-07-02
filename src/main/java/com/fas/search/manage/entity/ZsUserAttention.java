package com.fas.search.manage.entity;

import java.util.Date;

public class ZsUserAttention {
    private String id;

    private String subject_id;

    private String object_id;

    private String entity_id;

    private Date attention_time;

    private String userid;

    private String enable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id == null ? null : subject_id.trim();
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id == null ? null : object_id.trim();
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id == null ? null : entity_id.trim();
    }

    public Date getAttention_time() {
        return attention_time;
    }

    public void setAttention_time(Date attention_time) {
        this.attention_time = attention_time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }
}