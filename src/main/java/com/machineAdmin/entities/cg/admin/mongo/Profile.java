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
package com.machineAdmin.entities.cg.admin.mongo;

import com.machineAdmin.entities.cg.commons.EntityMongo;
import com.machineAdmin.models.cg.ModelPermisoAsignado;
import com.machineAdmin.models.cg.ModelUsuarioAsignado;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * perfil contenedor de permisos y usuarios
 */
public class Profile extends EntityMongo {

    private String name;
    private List<ModelUsuarioAsignado> users;
    private List<ModelPermisoAsignado> permisos;

    public Profile() {
        users = new ArrayList<>();
        permisos = new ArrayList<>();
    }

    public Profile(String name) {
        this.name = name;
    }

    public List<ModelPermisoAsignado> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<ModelPermisoAsignado> permisos) {
        this.permisos = permisos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ModelUsuarioAsignado> getUsers() {
        return users;
    }

    public void setUsers(List<ModelUsuarioAsignado> users) {
        this.users = users;
    }

}
