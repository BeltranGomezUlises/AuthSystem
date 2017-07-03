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
import com.machineAdmin.utils.UtilsConfig.CGConfig.SMSConfig;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsConfig {

    protected static final String COLLECTION_NAME = "config.cg";
    protected static final JacksonDBCollection<CGConfig, String> COLLECTION = JacksonDBCollection.wrap(UtilsDB.getCollection(COLLECTION_NAME), CGConfig.class, String.class);

    public static int getSessionJwtExp() {
        return COLLECTION.findOne().getJwtConfig().getSessionJwtExp();
    }

    public static int getRecoverJwtExp() {
        return COLLECTION.findOne().getJwtConfig().getRecoverJwtExp();
    }

    public static ConfigMail getResetPasswordConfigMail() {
        String mailId = COLLECTION.findOne().getMailConfig().getResetPasswordMailId();
        ManagerConfigMail managerConfigMail = new ManagerConfigMail();
        return managerConfigMail.findOne(mailId);
    }

    public static SMSConfig getSMSConfig() {
        return COLLECTION.findOne().getSmsConfig();
    }

    protected static class CGConfig extends EntityMongo {

        private JwtsConfig jwtConfig;
        private MailsConfig mailConfig;
        private SMSConfig smsConfig;

        public SMSConfig getSmsConfig() {
            return smsConfig;
        }

        public void setSmsConfig(SMSConfig smsConfig) {
            this.smsConfig = smsConfig;
        }

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

        protected static class MailsConfig {

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

        protected static class JwtsConfig {

            private int sessionJwtExp;
            private int recoverJwtExp;

            public int getSessionJwtExp() {
                return sessionJwtExp;
            }

            public void setSessionJwtExp(int sessionJwtExp) {
                this.sessionJwtExp = sessionJwtExp;
            }

            public int getRecoverJwtExp() {
                return recoverJwtExp;
            }

            public void setRecoverJwtExp(int recoverJwtExp) {
                this.recoverJwtExp = recoverJwtExp;
            }

        }

        protected static class SMSConfig {

            private String uri;
            private String deviceImei;
            private String usuarioId;

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getDeviceImei() {
                return deviceImei;
            }

            public void setDeviceImei(String deviceImei) {
                this.deviceImei = deviceImei;
            }

            public String getUsuarioId() {
                return usuarioId;
            }

            public void setUsuarioId(String usuarioId) {
                this.usuarioId = usuarioId;
            }

        }
    }
}
