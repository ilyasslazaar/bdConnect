package com.nov.ExceptionHandler;

import java.util.Date;

public class ExceptionResponse {

    private Date timeStamp;
    private String message;


    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    private  String details;

    public ExceptionResponse(Date date, String message, String description) {
        this.timeStamp =date;
        if(message.startsWith("StatementCallback")){
            this.message = message.split(":")[1];

        }else{
            this.message = message;
        }

        this.details = description;

    }



}
