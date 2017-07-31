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
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.machineAdmin.entities.cg.commons.EntitySQL;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "permiso")
@NamedQueries({
    @NamedQuery(name = "Permiso.findAll", query = "SELECT p FROM Permiso p")
    , @NamedQuery(name = "Permiso.findById", query = "SELECT p FROM Permiso p WHERE p.id = :id")
    , @NamedQuery(name = "Permiso.findByNombre", query = "SELECT p FROM Permiso p WHERE p.nombre = :nombre")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Permiso extends EntitySQL implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "id")
    private String id;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "menu", referencedColumnName = "id")
    @ManyToOne
    private Menu menu;
    @ManyToMany(mappedBy = "permisoList")
    private List<Perfil> perfilList;
    @JoinTable(name = "usuarios_permisos", joinColumns = {
        @JoinColumn(name = "permiso", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "usuario", referencedColumnName = "id")})
    @ManyToMany
    private List<Usuario> usuarioList;

    public Permiso() {
    }

    public Permiso(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @JsonIgnore
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Permiso other = (Permiso) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.Permiso[ id=" + id + " ]";
    }

    @Override
    public UUID getUsuarioCreador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUsuarioCreador(UUID usuarioCreador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @JsonIgnore
    public List<Perfil> getPerfilList() {
        return perfilList;
    }

    public void setPerfilList(List<Perfil> perfilList) {
        this.perfilList = perfilList;
    }

    @JsonIgnore
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

}
