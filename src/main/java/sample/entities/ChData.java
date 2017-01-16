package sample.entities;

import java.util.Date;

public class ChData {
    private Integer id;
    private Date date;
    private Integer completed;
    private Integer leftTo;

    public ChData(Date date, Integer completed, Integer leftTo) {
        this.date = date;
        this.completed = completed;
        this.leftTo = leftTo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getLeftTo() {
        return leftTo;
    }

    public void setLeftTo(Integer leftTo) {
        this.leftTo = leftTo;
    }
}
