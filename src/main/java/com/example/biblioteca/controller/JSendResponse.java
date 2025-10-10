package com.example.biblioteca.controller;

//da forma jsend a respuesta de la API
public class JSendResponse {

    private String status; // "success/fail/error"
    private Object data; // Data en "success" O detalles del error en "fail"
    private String message; // informativo

    public JSendResponse(String status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
