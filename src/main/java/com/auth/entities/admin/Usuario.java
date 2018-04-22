/*
 * Copyright (C) 2018 
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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findById", query = "SELECT u FROM Usuario u WHERE u.id = :id")
    , @NamedQuery(name = "Usuario.findByNombre", query = "SELECT u FROM Usuario u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Usuario.findByCorreo", query = "SELECT u FROM Usuario u WHERE u.correo = :correo")
    , @NamedQuery(name = "Usuario.findByTelefono", query = "SELECT u FROM Usuario u WHERE u.telefono = :telefono")
    , @NamedQuery(name = "Usuario.findByContra", query = "SELECT u FROM Usuario u WHERE u.contra = :contra")
    , @NamedQuery(name = "Usuario.findByInhabilitado", query = "SELECT u FROM Usuario u WHERE u.inhabilitado = :inhabilitado")
    , @NamedQuery(name = "Usuario.findByFechaUltimoIntentoLogin", query = "SELECT u FROM Usuario u WHERE u.fechaUltimoIntentoLogin = :fechaUltimoIntentoLogin")
    , @NamedQuery(name = "Usuario.findByNumeroIntentosLogin", query = "SELECT u FROM Usuario u WHERE u.numeroIntentosLogin = :numeroIntentosLogin")
    , @NamedQuery(name = "Usuario.findByBloqueado", query = "SELECT u FROM Usuario u WHERE u.bloqueado = :bloqueado")
    , @NamedQuery(name = "Usuario.findByBloqueadoHastaFecha", query = "SELECT u FROM Usuario u WHERE u.bloqueadoHastaFecha = :bloqueadoHastaFecha")})
public class Usuario extends IEntity<Integer> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contra;
    @Column(name = "inhabilitado")
    private Boolean inhabilitado;
    @Column(name = "fecha_ultimo_intento_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltimoIntentoLogin;
    @Column(name = "numero_intentos_login")
    private Integer numeroIntentosLogin;
    @Column(name = "bloqueado")
    private Boolean bloqueado;
    @Column(name = "bloqueado_hasta_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bloqueadoHastaFecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario1")
    @JsonIgnore
    private List<BitacoraContras> bitacoraContrasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario1")
    @JsonIgnore
    private List<UsuariosPermisos> usuariosPermisosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario1")
    @JsonIgnore
    private List<UsuariosPerfil> usuariosPerfilList;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
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

    public List<BitacoraContras> getBitacoraContrasList() {
        return bitacoraContrasList;
    }

    public void setBitacoraContrasList(List<BitacoraContras> bitacoraContrasList) {
        this.bitacoraContrasList = bitacoraContrasList;
    }

    public List<UsuariosPermisos> getUsuariosPermisosList() {
        return usuariosPermisosList;
    }

    public void setUsuariosPermisosList(List<UsuariosPermisos> usuariosPermisosList) {
        this.usuariosPermisosList = usuariosPermisosList;
    }

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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.auth.entities.admin.Usuario[ id=" + id + " ]";
    }

    @Override
    public Integer obtenerIdentificador() {
        return id;
    }

}
