/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.auth.entities.commons.IEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
@Entity
@Table(name = "grupo_perfiles")
@NamedQueries({
    @NamedQuery(name = "GrupoPerfiles.findAll", query = "SELECT g FROM GrupoPerfiles g")
    , @NamedQuery(name = "GrupoPerfiles.findByNombre", query = "SELECT g FROM GrupoPerfiles g WHERE g.nombre = :nombre")
    , @NamedQuery(name = "GrupoPerfiles.findByDescripcion", query = "SELECT g FROM GrupoPerfiles g WHERE g.descripcion = :descripcion")})
public class GrupoPerfiles extends IEntity<Integer> implements Serializable {

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

    @JoinTable(name = "perfil_grupo_perfiles", joinColumns = {
        @JoinColumn(name = "grupo_perfiles", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "perfil", referencedColumnName = "id")})
    @ManyToMany
    private List<Perfil> perfilList;

    public GrupoPerfiles() {        
    }

    public GrupoPerfiles(Integer id) {
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
    
    @JsonIgnore
    public List<Perfil> getPerfilList() {
        return perfilList;
    }

    public void setPerfilList(List<Perfil> perfilList) {
        this.perfilList = perfilList;
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
        if (!(object instanceof GrupoPerfiles)) {
            return false;
        }
        GrupoPerfiles other = (GrupoPerfiles) object;
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.GrupoPerfiles[ id=" + id + " ]";
    }

    @Override
    public Integer getId() {
        return id;
    }

}
