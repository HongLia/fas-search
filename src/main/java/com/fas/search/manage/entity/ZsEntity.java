package com.fas.search.manage.entity;

import java.util.Date;

public class ZsEntity {
    private String id;

    private String entity_name;

    private String category_id;

    private String remark;

    private String creator;

    private String enable;

    private String execute_sql;

    private String datasource_id;

    private String metadata_id;

    private String increment_field;

    private String privilege_level;

    private String filter_sql;

    private Long scount;

    private Date execute_time;

    private String collection_name;

    private String updator;

    private Date update_time;

    private Date create_time;

    private String query_last_value;

    private String tablename;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name == null ? null : entity_name.trim();
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id == null ? null : category_id.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public String getExecute_sql() {
        return execute_sql;
    }

    public void setExecute_sql(String execute_sql) {
        this.execute_sql = execute_sql == null ? null : execute_sql.trim();
    }

    public String getDatasource_id() {
        return datasource_id;
    }

    public void setDatasource_id(String datasource_id) {
        this.datasource_id = datasource_id == null ? null : datasource_id.trim();
    }

    public String getMetadata_id() {
        return metadata_id;
    }

    public void setMetadata_id(String metadata_id) {
        this.metadata_id = metadata_id == null ? null : metadata_id.trim();
    }

    public String getIncrement_field() {
        return increment_field;
    }

    public void setIncrement_field(String increment_field) {
        this.increment_field = increment_field == null ? null : increment_field.trim();
    }

    public String getPrivilege_level() {
        return privilege_level;
    }

    public void setPrivilege_level(String privilege_level) {
        this.privilege_level = privilege_level == null ? null : privilege_level.trim();
    }

    public String getFilter_sql() {
        return filter_sql;
    }

    public void setFilter_sql(String filter_sql) {
        this.filter_sql = filter_sql == null ? null : filter_sql.trim();
    }

    public Long getScount() {
        return scount;
    }

    public void setScount(Long scount) {
        this.scount = scount;
    }

    public Date getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(Date execute_time) {
        this.execute_time = execute_time;
    }

    public String getCollection_name() {
        return collection_name;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name == null ? null : collection_name.trim();
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

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getQuery_last_value() {
        return query_last_value;
    }

    public void setQuery_last_value(String query_last_value) {
        this.query_last_value = query_last_value == null ? null : query_last_value.trim();
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
}