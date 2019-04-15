package com.example.dell.concession;

public class ConcessionDetails {

    private String pass_interval,train_class,destination,source,pass_startDate,pass_endDate,con_date;

    public ConcessionDetails(){}

    public ConcessionDetails(String pass_interval,String train_class, String destination, String source, String pass_startDate, String pass_endDate, String con_date) {
        this.pass_interval = pass_interval;
        this.train_class = train_class;
        this.destination = destination;
        this.source = source;
        this.pass_startDate = pass_startDate;
        this.pass_endDate = pass_endDate;
        this.con_date = con_date;
    }

    public String getPass_interval() {
        return pass_interval;
    }

    public String getTrain_class() {
        return train_class;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public String getPass_startDate() {
        return pass_startDate;
    }

    public String getPass_endDate() {
        return pass_endDate;
    }

    public String getCon_date() {
        return con_date;
    }
}
