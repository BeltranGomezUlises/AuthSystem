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
package com.auth.entities.admin;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Embeddable
public class UsuariosPerfilPK implements Serializable {

    @Basic(optional = false)
    @NotNull    
    @Column(name = "usuario")
    private Integer usuario;
    @Basic(optional = false)
    @NotNull    
    @Column(name = "perfil")
    private Integer perfil;

    public UsuariosPerfilPK() {
    }

    public UsuariosPerfilPK(Integer usuario, Integer perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getPerfil() {
        return perfil;
    }

    public void setPerfil(Integer perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuario != null ? usuario.hashCode() : 0);
        hash += (perfil != null ? perfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosPerfilPK)) {
            return false;
        }
        UsuariosPerfilPK other = (UsuariosPerfilPK) object;
        return this.usuario.equals(other.usuario) && this.perfil.equals(other.perfil);
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfilPK[ usuario=" + usuario + ", perfil=" + perfil + " ]";
    }
    
}