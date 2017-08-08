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

import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.eclipse.persistence.annotations.Convert;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "grupo_perfiles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupoPerfiles.findAll", query = "SELECT g FROM GrupoPerfiles g")
    , @NamedQuery(name = "GrupoPerfiles.findByNombre", query = "SELECT g FROM GrupoPerfiles g WHERE g.nombre = :nombre")
    , @NamedQuery(name = "GrupoPerfiles.findByDescripcion", query = "SELECT g FROM GrupoPerfiles g WHERE g.descripcion = :descripcion")})
public class GrupoPerfiles extends EntitySQLCatalog implements Serializable {

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
    @Lob
    @Convert("uuidConverter")
    @Column(name = "usuario_creador")
    private UUID usuarioCreador;
    @JoinTable(name = "perfil_grupo_perfiles", joinColumns = {
        @JoinColumn(name = "grupo_perfiles", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "perfil", referencedColumnName = "id")})
    @ManyToMany
    private List<Perfil> perfilList;

    public GrupoPerfiles() {
        this.id = UUID.randomUUID();
    }

    public GrupoPerfiles(UUID id) {
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

    @Override
    public UUID getUsuarioCreador() {
        return usuarioCreador;
    }

    @Override
    public void setUsuarioCreador(UUID usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    @XmlTransient
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.GrupoPerfiles[ id=" + id + " ]";
    }

}
