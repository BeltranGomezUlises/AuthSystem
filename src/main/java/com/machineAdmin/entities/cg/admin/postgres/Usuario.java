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
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.Generated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.eclipse.persistence.annotations.Convert;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Usuario.findByCorreo", query = "SELECT u FROM Usuario u WHERE u.correo = :correo")
    , @NamedQuery(name = "Usuario.findByTelefono", query = "SELECT u FROM Usuario u WHERE u.telefono = :telefono")
    , @NamedQuery(name = "Usuario.findByContra", query = "SELECT u FROM Usuario u WHERE u.contra = :contra")
    , @NamedQuery(name = "Usuario.findByInhabilitado", query = "SELECT u FROM Usuario u WHERE u.inhabilitado = :inhabilitado")
    , @NamedQuery(name = "Usuario.findByFechaUltimoIntentoLogin", query = "SELECT u FROM Usuario u WHERE u.fechaUltimoIntentoLogin = :fechaUltimoIntentoLogin")
    , @NamedQuery(name = "Usuario.findByNumeroIntentosLogin", query = "SELECT u FROM Usuario u WHERE u.numeroIntentosLogin = :numeroIntentosLogin")
    , @NamedQuery(name = "Usuario.findByBloqueado", query = "SELECT u FROM Usuario u WHERE u.bloqueado = :bloqueado")
    , @NamedQuery(name = "Usuario.findByBloqueadoHastaFecha", query = "SELECT u FROM Usuario u WHERE u.bloqueadoHastaFecha = :bloqueadoHastaFecha")})
public class Usuario extends EntitySQLCatalog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 2147483647)
    @Column(name = "correo")
    private String correo;
    @Size(max = 10)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 2147483647)
    @Column(name = "contra")
    private String contra;
    @Column(name = "inhabilitado")
    private boolean inhabilitado;
    @Column(name = "fecha_ultimo_intento_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimoIntentoLogin;
    @Column(name = "numero_intentos_login")
    private int numeroIntentosLogin;
    @Column(name = "bloqueado")
    private boolean bloqueado;
    @Column(name = "bloqueado_hasta_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bloqueadoHastaFecha;
    @Id
    @Basic(optional = false)
    @NotNull    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;    
    @Column(name = "usuario_creador")
    private Integer usuarioCreador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario1")
    private List<BitacoraContras> bitacoraContrasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario1")
    private List<UsuariosPermisos> usuariosPermisosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario1")
    private List<UsuariosPerfil> usuariosPerfilList;

    public Usuario() {
        id = 0;        
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public Boolean getInhabilitado() {
        return inhabilitado;
    }

    public void setInhabilitado(Boolean inhabilitado) {
        this.inhabilitado = inhabilitado;
    }

    public Date getFechaUltimoIntentoLogin() {
        return fechaUltimoIntentoLogin;
    }

    public void setFechaUltimoIntentoLogin(Date fechaUltimoIntentoLogin) {
        this.fechaUltimoIntentoLogin = fechaUltimoIntentoLogin;
    }

    public Integer getNumeroIntentosLogin() {
        return numeroIntentosLogin;
    }

    public void setNumeroIntentosLogin(Integer numeroIntentosLogin) {
        this.numeroIntentosLogin = numeroIntentosLogin;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Date getBloqueadoHastaFecha() {
        return bloqueadoHastaFecha;
    }

    public void setBloqueadoHastaFecha(Date bloqueadoHastaFecha) {
        this.bloqueadoHastaFecha = bloqueadoHastaFecha;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public List<BitacoraContras> getBitacoraContrasList() {
        return bitacoraContrasList;
    }

    public void setBitacoraContrasList(List<BitacoraContras> bitacoraContrasList) {
        this.bitacoraContrasList = bitacoraContrasList;
    }
    
    @JsonIgnore
    public List<UsuariosPermisos> getUsuariosPermisosList() {
        return usuariosPermisosList;
    }

    public void setUsuariosPermisosList(List<UsuariosPermisos> usuariosPermisosList) {
        this.usuariosPermisosList = usuariosPermisosList;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.Usuario[ id=" + id + " ]";
    }

}
