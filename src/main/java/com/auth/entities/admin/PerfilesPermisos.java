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

import com.auth.entities.commons.IEntity;
import com.auth.entities.commons.Profundidad;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "perfiles_permisos")
@NamedQueries({
    @NamedQuery(name = "PerfilesPermisos.findAll", query = "SELECT p FROM PerfilesPermisos p")
    , @NamedQuery(name = "PerfilesPermisos.findByPerfil", query = "SELECT p FROM PerfilesPermisos p WHERE p.perfilesPermisosPK.perfil = :perfil")
    , @NamedQuery(name = "PerfilesPermisos.findByPermiso", query = "SELECT p FROM PerfilesPermisos p WHERE p.perfilesPermisosPK.permiso = :permiso")
    , @NamedQuery(name = "PerfilesPermisos.findByProfundidad", query = "SELECT p FROM PerfilesPermisos p WHERE p.profundidad = :profundidad")
    , @NamedQuery(name = "PerfilesPermisos.findBySucursal", query = "SELECT p FROM PerfilesPermisos p WHERE p.perfilesPermisosPK.sucursal = :sucursal")})
public class PerfilesPermisos extends IEntity<PerfilesPermisosPK> implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PerfilesPermisosPK perfilesPermisosPK;
    @Size(max = 2147483647)
    @Column(name = "profundidad")
    @Enumerated(EnumType.STRING)
    private Profundidad profundidad;
    @JoinColumn(name = "perfil", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Perfil perfil1;
    @JoinColumn(name = "permiso", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Permiso permiso1;
    @JoinColumn(name = "sucursal", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Sucursal sucursal1;

    public PerfilesPermisos() {
    }

    public PerfilesPermisos(PerfilesPermisosPK perfilesPermisosPK) {
        this.perfilesPermisosPK = perfilesPermisosPK;
    }

    public PerfilesPermisos(int perfil, String permiso, int sucursal) {
        this.perfilesPermisosPK = new PerfilesPermisosPK(perfil, permiso, sucursal);
    }    

    public PerfilesPermisosPK getPerfilesPermisosPK() {
        return perfilesPermisosPK;
    }

    public void setPerfilesPermisosPK(PerfilesPermisosPK perfilesPermisosPK) {
        this.perfilesPermisosPK = perfilesPermisosPK;
    }

    public Profundidad getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }

    public Perfil getPerfil1() {
        return perfil1;
    }

    public void setPerfil1(Perfil perfil1) {
        this.perfil1 = perfil1;
    }

    public Permiso getPermiso1() {
        return permiso1;
    }

    public void setPermiso1(Permiso permiso1) {
        this.permiso1 = permiso1;
    }

    public Sucursal getSucursal1() {
        return sucursal1;
    }

    public void setSucursal1(Sucursal sucursal1) {
        this.sucursal1 = sucursal1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perfilesPermisosPK != null ? perfilesPermisosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilesPermisos)) {
            return false;
        }
        PerfilesPermisos other = (PerfilesPermisos) object;
        if ((this.perfilesPermisosPK == null && other.perfilesPermisosPK != null) || (this.perfilesPermisosPK != null && !this.perfilesPermisosPK.equals(other.perfilesPermisosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.auth.entities.admin.PerfilesPermisos[ perfilesPermisosPK=" + perfilesPermisosPK + " ]";
    }

    @Override
    public PerfilesPermisosPK obtenerIdentificador() {
        return perfilesPermisosPK;
    }

}
