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
    private Long usuario;
    @Basic(optional = false)
    @NotNull    
    @Column(name = "perfil")
    private Long perfil;

    public UsuariosPerfilPK() {
    }

    public UsuariosPerfilPK(Long usuario, Long perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Long getPerfil() {
        return perfil;
    }

    public void setPerfil(Long perfil) {
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
        if ((this.usuario == null && other.usuario != null) || (this.usuario != null && !this.usuario.equals(other.usuario))) {
            return false;
        }
        if ((this.perfil == null && other.perfil != null) || (this.perfil != null && !this.perfil.equals(other.perfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfilPK[ usuario=" + usuario + ", perfil=" + perfil + " ]";
    }
    
}