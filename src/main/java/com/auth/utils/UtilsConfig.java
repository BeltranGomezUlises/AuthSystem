/*
 * Copyright (C) 2018 Alonso --- alonso@kriblet.com
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
package com.auth.utils;

import com.auth.entities.admin.ConfigMail;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class UtilsConfig {

    public static int getSecondsSessionJwtExp() {
        return 7200;
    }

    public static int getSecondsRecoverJwtExp() {
        return 1800;
    }

    public static ConfigMail getResetPasswordConfigMail() {
        return new ConfigMail("smtp.googlemail.com", 465, "ubg700@gmail.com", "ELECTRO-nic", true);
    }

    public static int getSecondsBetweenLoginAttempt() {
        return 120;
    }

    public static int getMaxNumberLoginAttempt() {
        return 10;
    }

    public static Date getDateUtilUserStillBlocked() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.SECOND, 1800);
        return cal.getTime();
    }

    public static int getMaxPasswordRecords() {
        return 3;
    }
}
