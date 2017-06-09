/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.models;

import com.machineAdmin.models.enums.Status;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class Response {

    private String message;
    private Status status;
    private Object body;

    public Response() {
        this.status = Status.OK;       
    }

    public Response(String message, Status status, Object body) {
        this.message = message;
        this.status = status;
        this.body = body;
    }

    public Response(Status status) {
        this.status = status;
    }

    public Response(String message, Status status) {
        this.message = message;
        this.status = status;
    }

    public Response(Status status, Object body) {
        this.status = status;
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" + "message=" + message + ", status=" + status + ", body=" + body + '}';
    }

}
