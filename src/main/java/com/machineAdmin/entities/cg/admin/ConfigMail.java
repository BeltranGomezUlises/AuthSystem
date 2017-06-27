/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.machineAdmin.entities.cg.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machineAdmin.entities.cg.EntityMongo;
import com.machineAdmin.utils.UtilsSecurity;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ulises
 */
public class ConfigMail extends EntityMongo{

    private String hostName;
    private int port;    
    private AuthMail auth;
    private boolean ssl;
    private String from;
        
    public static final class AuthMail {

        public AuthMail() {
        }

        public AuthMail(String mail, String pass) {
            this.mail = mail;
            this.pass = pass;
        }

        private String mail;
        
        @JsonIgnore
        private String pass;

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getPass() {
            try {
                return UtilsSecurity.decifrarMD5(pass);
            } catch (Exception ex) {
                Logger.getLogger(ConfigMail.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pass;
        }

        public void setPass(String pass) {
            this.pass = UtilsSecurity.cifrarMD5(pass);
        }
        
    }

}
