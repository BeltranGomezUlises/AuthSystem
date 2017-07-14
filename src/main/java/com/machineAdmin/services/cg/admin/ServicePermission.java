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
package com.machineAdmin.services.cg.admin;

import com.machineAdmin.entities.cg.admin.Permission;
import com.machineAdmin.managers.cg.admin.ManagerPermission;
import com.machineAdmin.managers.cg.exceptions.AccessDenied;
import com.machineAdmin.models.cg.ModelSetPermissionUser;
import com.machineAdmin.models.cg.enums.Status;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.ServiceFacade;
import com.machineAdmin.utils.UtilsPermissions;
import static com.machineAdmin.utils.UtilsPermissions.getPermissionId;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/permissions")
public class ServicePermission extends ServiceFacade<Permission> {

    public ServicePermission() {
        super(new ManagerPermission());
    }

    @Override
    public Response get(String token) {
        return super.get(token); //To change body of generated methods, choose Tools | Templates.
    }

    public Response setPermission(@HeaderParam("Authorization") String token, ModelSetPermissionUser modelSetPermissionUser){
        Response res = new Response();
        
        try {
            UtilsPermissions.getActionPermissionType(token, getPermissionId());
        } catch (AccessDenied ex) {
            res.setStatus(Status.ERROR);
            res.setMessage("Sin permiso para ésta accion");
            setCauseMessage(res, ex);            
        }                        
        return res;
        
    }
    
}
