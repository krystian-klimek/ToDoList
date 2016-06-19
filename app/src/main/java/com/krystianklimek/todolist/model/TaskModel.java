package com.krystianklimek.todolist.model;

/**
 * Created by: Krystian Klimek
 * Date: 19.06.2016.
 */

public class TaskModel {
    private int id;
    private String title;
    private String description;
    private String url_to_icon;
    private String created;
    private String time_end;

    public TaskModel() {
    }

    public TaskModel(String title, String description, String url_to_icon, String created, String time_end) {
        this.title = title;
        this.description = description;
        this.url_to_icon = url_to_icon;
        this.created = created;
        this.time_end = time_end;
    }

    public TaskModel(int id, String title, String description, String url_to_icon, String created, String time_end) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url_to_icon = url_to_icon;
        this.created = created;
        this.time_end = time_end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl_to_icon() {
        return url_to_icon;
    }

    public void setUrl_to_icon(String url_to_icon) {
        this.url_to_icon = url_to_icon;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
}
