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

import com.machineAdmin.daos.cg.admin.mongo.DaoConfig;
import com.machineAdmin.entities.cg.admin.mongo.CGConfig;
import com.machineAdmin.entities.cg.admin.mongo.CGConfig.AccessConfig;
import com.machineAdmin.entities.cg.admin.mongo.CGConfig.SMSConfig;
import com.machineAdmin.entities.cg.admin.mongo.ConfigMail;
import com.machineAdmin.managers.cg.admin.mongo.ManagerConfigMail;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsConfig {
        
    private static final DaoConfig DAO = new DaoConfig();

    public static int getSecondsSessionJwtExp() {
        return DAO.findFirst().getJwtConfig().getSecondsSessionJwtExp();
    }

    public static int getSecondsRecoverJwtExp() {
        return DAO.findFirst().getJwtConfig().getSecondsRecoverJwtExp();
    }

    public static ConfigMail getResetPasswordConfigMail() {
        String mailId = DAO.findFirst().getMailConfig().getResetPasswordMailId();
        ManagerConfigMail managerConfigMail = new ManagerConfigMail();
        return managerConfigMail.findOne(mailId);
    }

    public static SMSConfig getSMSConfig() {
        return DAO.findFirst().getSmsConfig();
    }

    public static CGConfig getCGConfig() {
        return DAO.findFirst();
    }

    public static AccessConfig getAccessConfig(){
        return DAO.findFirst().getAccessConfig();
    }
    
    public static int getSecondsBetweenLoginAttempt() {
        return getCGConfig().getAccessConfig().getSecondsBetweenEvents();
    }

    public static int getMaxNumberLoginAttempt() {
        return getCGConfig().getAccessConfig().getMaxNumberAttemps();
    }

    public static Date getDateUtilUserStillBlocked() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.SECOND, getCGConfig().getAccessConfig().getSecondsTermporalBlockingUser());
        return cal.getTime();
    }

    public static int getMaxPasswordRecords(){
        return DAO.findFirst().getAccessConfig().getMaxPasswordRecords();
    }
        
}
