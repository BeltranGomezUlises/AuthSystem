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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Convert;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "perfil")
@NamedQueries({
    @NamedQuery(name = "Perfil.findAll", query = "SELECT p FROM Perfil p")
    , @NamedQuery(name = "Perfil.findByNombre", query = "SELECT p FROM Perfil p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Perfil.findByDescripcion", query = "SELECT p FROM Perfil p WHERE p.descripcion = :descripcion")})
public class Perfil extends EntitySQLCatalog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;        
    @Column(name = "usuario_creador")
    private Integer usuarioCreador;
    @ManyToMany(mappedBy = "perfilList")
    private List<GrupoPerfiles> grupoPerfilesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perfil1")
    private List<PerfilesPermisos> perfilesPermisosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perfil1")
    private List<UsuariosPerfil> usuariosPerfilList;

    public Perfil() {
    }

    public Perfil(Integer id) {
        this.id = id;
    }

    @Override
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public Integer getUsuarioCreador() {
        return usuarioCreador;
    }

    @Override
    public void setUsuarioCreador(Integer usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }
   
    @JsonIgnore
    public List<GrupoPerfiles> getGrupoPerfilesList() {
        return grupoPerfilesList;
    }

    public void setGrupoPerfilesList(List<GrupoPerfiles> grupoPerfilesList) {
        this.grupoPerfilesList = grupoPerfilesList;
    }
    
    @JsonIgnore
    public List<PerfilesPermisos> getPerfilesPermisosList() {
        return perfilesPermisosList;
    }

    public void setPerfilesPermisosList(List<PerfilesPermisos> perfilesPermisosList) {
        this.perfilesPermisosList = perfilesPermisosList;
    }
    
    @JsonIgnore
    public List<UsuariosPerfil> getUsuariosPerfilList() {
        return usuariosPerfilList;
    }

    public void setUsuariosPerfilList(List<UsuariosPerfil> usuariosPerfilList) {
        this.usuariosPerfilList = usuariosPerfilList;
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
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.Perfil[ id=" + id + " ]";
    }
    
}
