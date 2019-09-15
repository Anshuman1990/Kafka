package com.example.Kafka.streams;

import java.util.Date;

public class Employee {
    private int id;
    private Date doj;
    private Boolean isScreened;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDoj() {
        return doj;
    }

    public void setDoj(Date doj) {
        this.doj = doj;
    }

    public Boolean getScreened() {
        return isScreened;
    }

    public void setScreened(Boolean screened) {
        isScreened = screened;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", doj=" + doj.getDay() +"/"+doj.getMonth()+"/"+doj.getYear()+
                ", isScreened=" + isScreened +
                '}';
    }
}
