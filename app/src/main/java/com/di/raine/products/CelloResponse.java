package com.di.raine.products;

import java.util.List;

/**
 * Created by jim on 28/8/2016.
 */

public class CelloResponse {

    private List< ? extends Product> data;
    private int status;
    private String message;
    private String messageCode;

    public CelloResponse() {
    }

    public CelloResponse(List<Product> data, int status, String message, String messageCode) {
        this.data = data;
        this.status = status;
        this.message = message;
        this.messageCode = messageCode;
    }

    public List<? extends Product> getData() {
        return data;
    }

    public void setData(List<? extends Product> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }
}
