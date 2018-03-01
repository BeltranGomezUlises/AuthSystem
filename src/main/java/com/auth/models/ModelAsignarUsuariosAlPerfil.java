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
package com.auth.models;

import java.util.List;

/**
 * modelo para asignar usuarios a un perfil
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ModelAsignarUsuariosAlPerfil {

    private String id;
    private List<ModelUsuarioAsignado> usuarios;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ModelUsuarioAsignado> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<ModelUsuarioAsignado> usuarios) {
        this.usuarios = usuarios;
    }

}
