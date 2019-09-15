package com.example.Kafka.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Employee implements Serializable {
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
                ", doj=" + doj +
                ", isScreened=" + isScreened +
                '}';
    }

}
