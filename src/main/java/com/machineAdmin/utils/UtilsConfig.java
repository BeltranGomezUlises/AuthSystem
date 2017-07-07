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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsConfig {

    protected static final String COLLECTION_NAME = "config.cg";
    protected static final JacksonDBCollection<CGConfig, String> COLLECTION = JacksonDBCollection.wrap(UtilsDB.getCollection(COLLECTION_NAME), CGConfig.class, String.class);

    public static int getSecondsSessionJwtExp() {
        return COLLECTION.findOne().getJwtConfig().getSecondsSessionJwtExp();
    }

    public static int getSecondsRecoverJwtExp() {
        return COLLECTION.findOne().getJwtConfig().getSecondsRecoverJwtExp();
    }

    public static ConfigMail getResetPasswordConfigMail() {
        String mailId = COLLECTION.findOne().getMailConfig().getResetPasswordMailId();
        ManagerConfigMail managerConfigMail = new ManagerConfigMail();
        return managerConfigMail.findOne(mailId);
    }

    public static SMSConfig getSMSConfig() {
        return COLLECTION.findOne().getSmsConfig();
    }

    public static CGConfig getCGConfig() {
        return COLLECTION.findOne();
    }

    public static int getSecondsBetweenLoginAttempt() {
        return getCGConfig().getLoginConfig().getSecondsBetweenEvents();
    }

    public static int getMaxNumberLoginAttempt() {
        return getCGConfig().getLoginConfig().getMaxNumberAttemps();
    }

    public static Date getDateUtilUserStillBlocked(){
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.SECOND, getCGConfig().getLoginConfig().getSecondsTermporalBlockingUser());
        return cal.getTime();
    }
    
    public static class CGConfig extends EntityMongo {

        private JwtsConfig jwtConfig;
        private MailsConfig mailConfig;
        private SMSConfig smsConfig;
        private LoginAttemptConfig loginConfig;

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

        public LoginAttemptConfig getLoginConfig() {
            return loginConfig;
        }

        public void setLoginConfig(LoginAttemptConfig loginConfig) {
            this.loginConfig = loginConfig;
        }

        /**
         * clases anidadas
         */
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

            @Override
            public String toString() {
                return "MailsConfig{" + "resetPasswordMailId=" + resetPasswordMailId + ", supportMailId=" + supportMailId + ", contactMailId=" + contactMailId + '}';
            }

        }

        protected static class JwtsConfig {

            private int secondsSessionJwtExp;
            private int secondsRecoverJwtExp;

            public int getSecondsSessionJwtExp() {
                return secondsSessionJwtExp;
            }

            public void setSecondsSessionJwtExp(int secondsSessionJwtExp) {
                this.secondsSessionJwtExp = secondsSessionJwtExp;
            }

            public int getSecondsRecoverJwtExp() {
                return secondsRecoverJwtExp;
            }

            public void setSecondsRecoverJwtExp(int secondsRecoverJwtExp) {
                this.secondsRecoverJwtExp = secondsRecoverJwtExp;
            }

            @Override
            public String toString() {
                return "JwtsConfig{" + "secondsSessionJwtExp=" + secondsSessionJwtExp + ", secondsRecoverJwtExp=" + secondsRecoverJwtExp + '}';
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

            @Override
            public String toString() {
                return "SMSConfig{" + "uri=" + uri + ", deviceImei=" + deviceImei + ", usuarioId=" + usuarioId + '}';
            }

        }

        protected static class LoginAttemptConfig {

            private int secondsBetweenEvents;
            private int maxNumberAttemps;
            private int secondsTermporalBlockingUser;

            public int getSecondsBetweenEvents() {
                return secondsBetweenEvents;
            }

            public void setSecondsBetweenEvents(int secondsBetweenEvents) {
                this.secondsBetweenEvents = secondsBetweenEvents;
            }

            public int getMaxNumberAttemps() {
                return maxNumberAttemps;
            }

            public void setMaxNumberAttemps(int maxNumberAttemps) {
                this.maxNumberAttemps = maxNumberAttemps;
            }

            public int getSecondsTermporalBlockingUser() {
                return secondsTermporalBlockingUser;
            }

            public void setSecondsTermporalBlockingUser(int secondsTermporalBlockingUser) {
                this.secondsTermporalBlockingUser = secondsTermporalBlockingUser;
            }

            @Override
            public String toString() {
                return "LoginAttemptConfig{" + "secondsBetweenEvents=" + secondsBetweenEvents + ", maxNumberAttemps=" + maxNumberAttemps + ", secondsTermporalBlockingUser=" + secondsTermporalBlockingUser + '}';
            }

        }

        @Override
        public String toString() {
            return "CGConfig{" + "jwtConfig=" + jwtConfig + ", mailConfig=" + mailConfig + ", smsConfig=" + smsConfig + ", loginConfig=" + loginConfig + '}';
        }

    }

}
