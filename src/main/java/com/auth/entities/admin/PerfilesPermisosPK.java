/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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
package com.auth.entities.admin;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
@Embeddable
public class PerfilesPermisosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil")
    private Integer perfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "permiso")
    private String permiso;

    public PerfilesPermisosPK() {
    }

    public PerfilesPermisosPK(Integer perfil, String permiso) {
        this.perfil = perfil;
        this.permiso = permiso;
    }

    public Integer getPerfil() {
        return perfil;
    }

    public void setPerfil(Integer perfil) {
        this.perfil = perfil;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perfil != null ? perfil.hashCode() : 0);
        hash += (permiso != null ? permiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PerfilesPermisosPK)) {
            return false;
        }
        PerfilesPermisosPK other = (PerfilesPermisosPK) object;
        if (!this.perfil.equals(other.perfil)) {
            return false;
        }
        return this.permiso.equals(other.permiso);
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisosPK[ perfil=" + perfil + ", permiso=" + permiso + " ]";
    }

}
