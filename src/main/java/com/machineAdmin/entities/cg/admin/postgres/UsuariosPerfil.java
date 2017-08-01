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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.machineAdmin.entities.cg.commons.EntitySQLCatalog;
import com.machineAdmin.entities.cg.commons.IEntity;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "usuarios_perfil")
@NamedQueries({
    @NamedQuery(name = "UsuariosPerfil.findAll", query = "SELECT u FROM UsuariosPerfil u")
    , @NamedQuery(name = "UsuariosPerfil.findByHereda", query = "SELECT u FROM UsuariosPerfil u WHERE u.hereda = :hereda")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuariosPerfil extends EntitySQLCatalog implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuariosPerfilPK usuariosPerfilPK;
    @Column(name = "hereda")
    private Boolean hereda;
    @JoinColumn(name = "perfil", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Perfil perfil1;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario1;

    public UsuariosPerfil() {
        this.hereda = false;
    }

    public UsuariosPerfil(UsuariosPerfilPK usuariosPerfilPK) {
        this.usuariosPerfilPK = usuariosPerfilPK;
    }

    public UsuariosPerfil(UUID usuario, UUID perfil) {
        this.usuariosPerfilPK = new UsuariosPerfilPK(usuario, perfil);
    }

    public UsuariosPerfilPK getUsuariosPerfilPK() {
        return usuariosPerfilPK;
    }

    public void setUsuariosPerfilPK(UsuariosPerfilPK usuariosPerfilPK) {
        this.usuariosPerfilPK = usuariosPerfilPK;
    }

    public Boolean getHereda() {
        return hereda;
    }

    public void setHereda(Boolean hereda) {
        this.hereda = hereda;
    }

    public Perfil getPerfil1() {
        return perfil1;
    }

    public void setPerfil1(Perfil perfil1) {
        this.perfil1 = perfil1;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.usuariosPerfilPK);
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
        final UsuariosPerfil other = (UsuariosPerfil) obj;
        return Objects.equals(this.usuariosPerfilPK, other.usuariosPerfilPK);
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfil[ usuariosPerfilPK=" + usuariosPerfilPK + " ]";
    }

    @Override
    public Object getId() {
        return usuariosPerfilPK;
    }

    @Override
    public UUID getUsuarioCreador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUsuarioCreador(UUID usuarioCreador) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
