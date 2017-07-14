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

import com.machineAdmin.entities.cg.admin.Permission;
import com.machineAdmin.entities.cg.admin.User;
import com.machineAdmin.managers.cg.admin.ManagerPermission;
import com.machineAdmin.managers.cg.admin.ManagerUser;
import com.machineAdmin.managers.cg.exceptions.AccessDenied;
import com.machineAdmin.models.cg.ModelAsignedPermission;
import com.machineAdmin.models.cg.enums.PermissionType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsPermissions {

    private static final ManagerPermission MANAGER = new ManagerPermission();

    public static boolean hasPermission(User u, String accion) {
        return true;
    }

    public static List<ModelAsignedPermission> getAsignedPermissionsAvailable() {
        List<ModelAsignedPermission> asignedPermissions = new ArrayList<>();
        Permission permission = MANAGER.findFirst();
        for (Permission.Seccion seccion : permission.getSecciones()) {
            for (Permission.Seccion.Module modulo : seccion.getModulos()) {
                for (Permission.Seccion.Module.Menu menu : modulo.getMenus()) {
                    for (Permission.Seccion.Module.Menu.Action action : menu.getAcciones()) {
                        ModelAsignedPermission asigned = new ModelAsignedPermission();
                        asigned.setId(action.getId());
                        asigned.setType(PermissionType.ALL);
                        asignedPermissions.add(asigned);
                    }
                }
            }
        }
        return asignedPermissions;
    }

    public static PermissionType getActionPermissionType(String token, String permissionId) throws AccessDenied {
        ManagerUser managerUser = new ManagerUser();
        User u = managerUser.findOne(UtilsJWT.getBodyToken(token));

        ModelAsignedPermission asignedPermissionLockedFor = u.getAsignedPermissions().stream().filter(p -> p.getId().equals(permissionId)).findFirst().get();

        if (asignedPermissionLockedFor != null) {
            return asignedPermissionLockedFor.getType();
        } else {
            throw new AccessDenied("No cuenta con los permisos necesarios para esta acción");
        }
    }

    public static String getPermissionId(){
        String permissionId = "";
        
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            System.out.println("class: " + stackTraceElement.getClassName() + "method: " + stackTraceElement.getMethodName() + " otro: " + stackTraceElement.getFileName());
        }
        
        return permissionId;               
    }
    
    
}
