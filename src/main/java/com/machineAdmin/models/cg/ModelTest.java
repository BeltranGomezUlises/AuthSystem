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
package com.machineAdmin.models.cg;

import com.machineAdmin.entities.cg.commons.UUIDConverter;
import java.util.UUID;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
public class ModelTest {
    @Convert("uuidConverter")
    private UUID uuid;
    private String string;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }            
    
}
