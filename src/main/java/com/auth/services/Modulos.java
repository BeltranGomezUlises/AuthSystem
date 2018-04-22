/*
 * Copyright (C) 2018 
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
package com.auth.services;

import com.auth.entities.admin.Modulo;
import com.auth.managers.admin.ManagerModulo;
import com.auth.services.commons.ServiceFacade;
import javax.ws.rs.Path;

/**
 * Servicios LCRUD de modulos
 *
 * @author
 */
@Path("/modulos")
public class Modulos extends ServiceFacade<Modulo, String> {

    public Modulos() {
        super(new ManagerModulo());
    }

}
