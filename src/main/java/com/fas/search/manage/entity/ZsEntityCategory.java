package com.fas.search.manage.entity;

import java.util.Date;

public class ZsEntityCategory {
    private String id;

    private String name;

    private String parent_id;

    private String remark;

    private Date create_time;

    private String creator;

    private String enable;

    private String updator;

    private Date update_time;

    private Integer thesort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id == null ? null : parent_id.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
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

    public ZsEntityCategory() {
        System.out.println();
    }


    public ZsEntityCategory(String id, String creator, String enable, Date update_time,String updator) {
        this.id = id;
        this.creator = creator;
        this.enable = enable;
        this.update_time = update_time;
        this.updator = updator;
    }

    @Override
    public String toString() {
        return "ZsEntityCategory{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parend_id='" + parent_id + '\'' +
                ", remark='" + remark + '\'' +
                ", create_time=" + create_time +
                ", creator='" + creator + '\'' +
                ", enable='" + enable + '\'' +
                ", updator='" + updator + '\'' +
                ", update_time=" + update_time +
                ", thesort=" + thesort +
                '}';
    }
}