/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.models.cg.enums.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.machineAdmin.models.cg.enums.Status;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@JsonInclude(Include.NON_NULL)
public class MetaData {

    private String message;
    private Status status;
    private String devMessage;
    private Object metaData;

    private static final String DEFAULT_DEV_MESSAGE = "Operación Realizada";

    public MetaData() {
        this.devMessage = DEFAULT_DEV_MESSAGE;
        this.status = Status.OK;
    }

    public MetaData(String message) {
        this.message = message;
        this.devMessage = DEFAULT_DEV_MESSAGE;
        this.status = Status.OK;
    }

    public MetaData(Status status, String devMessage) {
        this.status = status;
        this.devMessage = devMessage;
    }

    public MetaData(String message, Status status, String devMessage) {
        this.message = message;
        this.status = status;
        this.devMessage = devMessage;
    }

    public MetaData(Object metaData, String message) {
        this.message = message;
        this.devMessage = DEFAULT_DEV_MESSAGE;
        this.status = Status.OK;
        this.metaData = metaData;
    }

    public MetaData(Object metaData, Status status, String devMessage) {
        this.metaData = metaData;
        this.status = status;
        this.devMessage = devMessage;
    }

    public MetaData(Object metaData, String message, Status status, String devMessage) {
        this.metaData = metaData;
        this.message = message;
        this.status = status;
        this.devMessage = devMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDevMessage(String devMessage) {
        this.devMessage = devMessage;
    }

    public void setMetaData(Object metaData) {
        this.metaData = metaData;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    public String getDevMessage() {
        return devMessage;
    }

    public Object getMetaData() {
        return metaData;
    }   
    
    @Override
    public String toString() {
        return "MetaData{" + "message=" + message + ", status=" + status + ", devMessage=" + devMessage + ", metaData=" + metaData + '}';
    }

}
