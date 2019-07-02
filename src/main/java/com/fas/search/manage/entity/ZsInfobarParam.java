package com.fas.search.manage.entity;

import java.util.Date;

public class ZsInfobarParam {
    private String id;

    private String ename;

    private String cname;

    private String showname;

    private String overview_field;

    private String detail_field;

    private Integer thesort;

    private String infobar_entity_id;

    private Date create_time;

    private Date update_time;

    private String creator;

    private String updator;

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

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname == null ? null : showname.trim();
    }

    public String getOverview_field() {
        return overview_field;
    }

    public void setOverview_field(String overview_field) {
        this.overview_field = overview_field == null ? null : overview_field.trim();
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

    public String getInfobar_entity_id() {
        return infobar_entity_id;
    }

    public void setInfobar_entity_id(String infobar_entity_id) {
        this.infobar_entity_id = infobar_entity_id == null ? null : infobar_entity_id.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
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

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }
}