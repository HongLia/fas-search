package com.fas.search.manage.entity;

import java.util.Date;

public class ZsEtlTask {
    private Integer id;

    private String name;

    private String entity_id;

    private String type;

    private String execute_condition;

    private Date create_time;

    private String updator;

    private Date update_time;

    private String creator;

    private String enable;

    private String query_last_value;

    private Integer thesort;

    private Date last_execute_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id == null ? null : entity_id.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getExecute_condition() {
        return execute_condition;
    }

    public void setExecute_condition(String execute_condition) {
        this.execute_condition = execute_condition == null ? null : execute_condition.trim();
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

    public String getQuery_last_value() {
        return query_last_value;
    }

    public void setQuery_last_value(String query_last_value) {
        this.query_last_value = query_last_value == null ? null : query_last_value.trim();
    }

    public Integer getThesort() {
        return thesort;
    }

    public void setThesort(Integer thesort) {
        this.thesort = thesort;
    }

    public Date getLast_execute_time() {
        return last_execute_time;
    }

    public void setLast_execute_time(Date last_execute_time) {
        this.last_execute_time = last_execute_time;
    }
}