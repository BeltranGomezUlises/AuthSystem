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
package com.auth.entities.admin;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author
 */
@Embeddable
public class UsuariosPerfilPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario")
    private int usuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "perfil")
    private int perfil;

    public UsuariosPerfilPK() {
    }

    public UsuariosPerfilPK(int usuario, int perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) usuario;
        hash += (int) perfil;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosPerfilPK)) {
            return false;
        }
        UsuariosPerfilPK other = (UsuariosPerfilPK) object;
        if (this.usuario != other.usuario) {
            return false;
        }
        if (this.perfil != other.perfil) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.auth.entities.admin.UsuariosPerfilPK[ usuario=" + usuario + ", perfil=" + perfil + " ]";
    }

}
