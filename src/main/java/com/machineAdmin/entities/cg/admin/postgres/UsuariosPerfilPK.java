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
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Embeddable
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
public class UsuariosPerfilPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Convert("uuidConverter")
    @Column(name = "usuario")
    private UUID usuario;
    @Basic(optional = false)
    @NotNull
    @Convert("uuidConverter")
    @Column(name = "perfil")
    private UUID perfil;

    public UsuariosPerfilPK() {
    }

    public UsuariosPerfilPK(UUID usuario, UUID perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }

    public UUID getUsuario() {
        return usuario;
    }

    public void setUsuario(UUID usuario) {
        this.usuario = usuario;
    }

    public UUID getPerfil() {
        return perfil;
    }

    public void setPerfil(UUID perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.usuario);
        hash = 67 * hash + Objects.hashCode(this.perfil);
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
        final UsuariosPerfilPK other = (UsuariosPerfilPK) obj;
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        return Objects.equals(this.perfil, other.perfil);
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfilPK[ usuario=" + usuario + ", perfil=" + perfil + " ]";
    }

}
