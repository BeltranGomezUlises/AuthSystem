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
package com.machineAdmin.entities.cg.admin.postgres;

import com.machineAdmin.entities.cg.commons.UUIDConverter;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Embeddable
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
public class PerfilesPermisosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Convert("uuidConverter")
    @Column(name = "perfil")
    private UUID perfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "permiso")
    private String permiso;

    public PerfilesPermisosPK() {
    }

    public PerfilesPermisosPK(UUID perfil, String permiso) {
        this.perfil = perfil;
        this.permiso = permiso;
    }

    public Object getPerfil() {
        return perfil;
    }

    public void setPerfil(UUID perfil) {
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
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.perfil);
        hash = 97 * hash + Objects.hashCode(this.permiso);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PerfilesPermisosPK other = (PerfilesPermisosPK) obj;
        if (!Objects.equals(this.permiso, other.permiso)) {
            return false;
        }
        if (!Objects.equals(this.perfil, other.perfil)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisosPK[ perfil=" + perfil + ", permiso=" + permiso + " ]";
    }

}
