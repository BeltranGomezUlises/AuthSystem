/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.entities.cg.admin;

import com.machineAdmin.entities.cg.commons.EntityMongo;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * configuracion de correo para su uso por el sistema
 */
public class ConfigMail extends EntityMongo {

    private String hostName;
    private int port;
    private AuthMail auth;
    private boolean ssl;

    public ConfigMail() {
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public AuthMail getAuth() {
        return auth;
    }

    public void setAuth(AuthMail auth) {
        this.auth = auth;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    /**
     * modelo contenedor de la autentificacion del correo
     */
    public static final class AuthMail {

        public AuthMail() {
        }

        public AuthMail(String mail, String pass) {
            this.mail = mail;
            this.pass = pass;
        }

        private String mail;
        private String pass;

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        @Override
        public String toString() {
            return "AuthMail{" + "mail=" + mail + ", pass=" + pass + '}';
        }

    }

    @Override
    public String toString() {
        return "ConfigMail{" + "hostName=" + hostName + ", port=" + port + ", auth=" + auth + ", ssl=" + ssl + '}';
    }

}
