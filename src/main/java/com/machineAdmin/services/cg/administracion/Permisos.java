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
package com.machineAdmin.services.cg.administracion;

import com.machineAdmin.entities.cg.admin.mongo.AvailablePermission;
import com.machineAdmin.managers.cg.admin.mongo.ManagerPermission;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import java.util.List;
import javax.ws.rs.Path;

/**
 * servicios de administracion de permisos del sistema (los permisos son generados al arrancar la api)
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/permisos")
public class Permisos extends ServiceFacade<AvailablePermission> {

    public Permisos() {
        super(new ManagerPermission());
    }

    @Override
    public Response listar(String token) {        
        Response res = super.listar(token);        
        AvailablePermission availablePermission = ((List<AvailablePermission>) res.getData()).get(0);
        availablePermission.setId(null);
        
        res.setData(availablePermission);        
        return res;
    }
       
}
