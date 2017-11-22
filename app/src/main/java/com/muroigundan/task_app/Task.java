package com.muroigundan.task_app;

import android.graphics.Color;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by local-user on 2017/10/18.
 */

public class Task extends RealmObject{
    @PrimaryKey
    private long id;
    private String subject; //件名
    private Date date_and_time;
    private Date date; //〆切日付
    private Date time; //〆切時間
    private int importance; //重要度
    private String remarks; //備考
    private int color; // タスクの色

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(Date date_and_time) {
        this.date_and_time = date_and_time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
