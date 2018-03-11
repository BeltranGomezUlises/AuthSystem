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
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Ulises Beltrán Gómez - beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "sucursal")
@NamedQueries({
    @NamedQuery(name = "Sucursal.findAll", query = "SELECT s FROM Sucursal s")})
public class Sucursal extends IEntity<Integer> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    private String nombre;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursal1", fetch = FetchType.EAGER)
    private List<PerfilesPermisos> perfilesPermisosList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sucursal1", fetch = FetchType.EAGER)
    private List<UsuariosPermisos> usuariosPermisosList;

    public Sucursal() {
    }

    public Sucursal(Integer id) {
        this.id = id;
    }

    public Sucursal(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<PerfilesPermisos> getPerfilesPermisosList() {
        return perfilesPermisosList;
    }

    public void setPerfilesPermisosList(List<PerfilesPermisos> perfilesPermisosList) {
        this.perfilesPermisosList = perfilesPermisosList;
    }

    public List<UsuariosPermisos> getUsuariosPermisosList() {
        return usuariosPermisosList;
    }

    public void setUsuariosPermisosList(List<UsuariosPermisos> usuariosPermisosList) {
        this.usuariosPermisosList = usuariosPermisosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sucursal)) {
            return false;
        }
        Sucursal other = (Sucursal) object;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.auth.entities.admin.Sucursal[ id=" + id + " ]";
    }

    @Override
    public Integer obtenerIdentificador() {
        return id;
    }

}
