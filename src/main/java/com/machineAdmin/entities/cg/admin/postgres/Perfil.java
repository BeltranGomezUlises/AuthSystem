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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import com.machineAdmin.entities.cg.commons.UUIDConverter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

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
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
public class Perfil extends EntitySQLCatalog implements Serializable {

    @Lob
    @Convert("uuidConverter")
    @Column(name = "usuario_creador")
    private UUID usuarioCreador;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Convert("uuidConverter")
    @Column(name = "id")
    private UUID id;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinTable(name = "perfiles_permisos", joinColumns = {
        @JoinColumn(name = "perfil", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "permiso", referencedColumnName = "id")})
    @ManyToMany
    private List<Permiso> permisoList;
    @ManyToMany(mappedBy = "perfilList")
    private List<GrupoPerfiles> grupoPerfilesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perfil1")
    private List<UsuariosPerfil> usuariosPerfilList;

    public Perfil() {
        this.id = UUID.randomUUID();
        this.permisoList = new ArrayList<>();
    }

    public Perfil(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
    public List<GrupoPerfiles> getGrupoPerfilesList() {
        return grupoPerfilesList;
    }

    public void setGrupoPerfilesList(List<GrupoPerfiles> grupoPerfilesList) {
        this.grupoPerfilesList = grupoPerfilesList;
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
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final Perfil other = (Perfil) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.Perfil[ id=" + id + " ]";
    }

    @Override
    public UUID getUsuarioCreador() {
        return this.usuarioCreador;
    }

    @Override
    public void setUsuarioCreador(UUID usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    @JsonIgnore
    public List<Permiso> getPermisoList() {
        return permisoList;
    }

    public void setPermisoList(List<Permiso> permisoList) {
        this.permisoList = permisoList;
    }

}
