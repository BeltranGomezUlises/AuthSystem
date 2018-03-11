/*
 * Copyright (C) 2018 Ulises Beltrán Gómez - beltrangomezulises@gmail.com
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
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
@Embeddable
public class PerfilesPermisosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil")
    private int perfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "permiso")
    private String permiso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sucursal")
    private int sucursal;

    public PerfilesPermisosPK() {
    }

    public PerfilesPermisosPK(int perfil, String permiso, int sucursal) {
        this.perfil = perfil;
        this.permiso = permiso;
        this.sucursal = sucursal;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public int getSucursal() {
        return sucursal;
    }

    public void setSucursal(int sucursal) {
        this.sucursal = sucursal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) perfil;
        hash += (permiso != null ? permiso.hashCode() : 0);
        hash += (int) sucursal;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilesPermisosPK)) {
            return false;
        }
        PerfilesPermisosPK other = (PerfilesPermisosPK) object;
        if (this.perfil != other.perfil) {
            return false;
        }
        if ((this.permiso == null && other.permiso != null) || (this.permiso != null && !this.permiso.equals(other.permiso))) {
            return false;
        }
        if (this.sucursal != other.sucursal) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.auth.entities.admin.PerfilesPermisosPK[ perfil=" + perfil + ", permiso=" + permiso + ", sucursal=" + sucursal + " ]";
    }
    
}
