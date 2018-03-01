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

import com.auth.entities.commons.IEntity;
import com.auth.entities.commons.Profundidad;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "usuarios_permisos")
@NamedQueries({
    @NamedQuery(name = "UsuariosPermisos.findAll", query = "SELECT u FROM UsuariosPermisos u")
    , @NamedQuery(name = "UsuariosPermisos.findByPermiso", query = "SELECT u FROM UsuariosPermisos u WHERE u.usuariosPermisosPK.permiso = :permiso")
    , @NamedQuery(name = "UsuariosPermisos.findByProfundidad", query = "SELECT u FROM UsuariosPermisos u WHERE u.profundidad = :profundidad")})
public class UsuariosPermisos extends IEntity<UsuariosPermisosPK> implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuariosPermisosPK usuariosPermisosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "profundidad")
    @Enumerated(EnumType.STRING)
    private Profundidad profundidad;
    @JoinColumn(name = "permiso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Permiso permiso1;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario1;

    public UsuariosPermisos() {
    }

    public UsuariosPermisos(UsuariosPermisosPK usuariosPermisosPK) {
        this.usuariosPermisosPK = usuariosPermisosPK;
    }

    public UsuariosPermisos(UsuariosPermisosPK usuariosPermisosPK, Profundidad profundidad) {
        this.usuariosPermisosPK = usuariosPermisosPK;
        this.profundidad = profundidad;
    }

    public UsuariosPermisos(Integer usuario, String permiso) {
        this.usuariosPermisosPK = new UsuariosPermisosPK(usuario, permiso);
    }

    public UsuariosPermisosPK getUsuariosPermisosPK() {
        return usuariosPermisosPK;
    }

    public void setUsuariosPermisosPK(UsuariosPermisosPK usuariosPermisosPK) {
        this.usuariosPermisosPK = usuariosPermisosPK;
    }

    public Profundidad getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }

    public Permiso getPermiso1() {
        return permiso1;
    }

    public void setPermiso1(Permiso permiso1) {
        this.permiso1 = permiso1;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuariosPermisosPK != null ? usuariosPermisosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosPermisos)) {
            return false;
        }
        UsuariosPermisos other = (UsuariosPermisos) object;
        if ((this.usuariosPermisosPK == null && other.usuariosPermisosPK != null) || (this.usuariosPermisosPK != null && !this.usuariosPermisosPK.equals(other.usuariosPermisosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos[ usuariosPermisosPK=" + usuariosPermisosPK + " ]";
    }

    @Override
    public UsuariosPermisosPK getId() {
        return usuariosPermisosPK;
    }
    
}
