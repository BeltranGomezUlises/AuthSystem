/*
 * Copyright (C) 2017 Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.machineAdmin.utils;

import com.machineAdmin.entities.cg.EntityMongo;
import com.machineAdmin.entities.cg.admin.ConfigMail;
import com.machineAdmin.managers.cg.admin.ManagerConfigMail;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsConfig {

    protected static final String COLLECTION_NAME = "config.cg";
    protected static final JacksonDBCollection<CGConfig, String> COLLECTION = JacksonDBCollection.wrap(UtilsDB.getCollection(COLLECTION_NAME), CGConfig.class, String.class);

    public static int getJwtExp() {
        return COLLECTION.findOne().getJwtConfig().getJwtExp();
    }
    
    public static ConfigMail getResetPasswordConfigMail(){
        String mailId = COLLECTION.findOne().getMailConfig().getResetPasswordMailId();        
        ManagerConfigMail managerConfigMail = new ManagerConfigMail();        
        return managerConfigMail.findOne(mailId);                                
    }

    private static class CGConfig extends EntityMongo {

        private JwtsConfig jwtConfig;
        private MailsConfig mailConfig;

        public JwtsConfig getJwtConfig() {
            return jwtConfig;
        }       
        
        public void setJwtConfig(JwtsConfig jwtConfig) {
            this.jwtConfig = jwtConfig;
        }        

        public MailsConfig getMailConfig() {
            return mailConfig;
        }

        public void setMailConfig(MailsConfig mailConfig) {
            this.mailConfig = mailConfig;
        }
        
        
        private static class MailsConfig {

            private String resetPasswordMailId;
            private String supportMailId;
            private String contactMailId;

            public String getResetPasswordMailId() {
                return resetPasswordMailId;
            }

            public void setResetPasswordMailId(String resetPasswordMailId) {
                this.resetPasswordMailId = resetPasswordMailId;
            }

            public String getSupportMailId() {
                return supportMailId;
            }

            public void setSupportMailId(String supportMailId) {
                this.supportMailId = supportMailId;
            }

            public String getContactMailId() {
                return contactMailId;
            }

            public void setContactMailId(String contactMailId) {
                this.contactMailId = contactMailId;
            }                       
            
        }

        private static class JwtsConfig {

            private int jwtExp;

            public int getJwtExp() {
                return jwtExp;
            }

            public void setJwtExp(int jwtExp) {
                this.jwtExp = jwtExp;
            }

        }
    }
}
