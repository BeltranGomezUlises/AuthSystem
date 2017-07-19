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
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findByUsuario", query = "SELECT u FROM Usuarios u WHERE u.usuario = :usuario")
    , @NamedQuery(name = "Usuarios.findByCorreo", query = "SELECT u FROM Usuarios u WHERE u.correo = :correo")
    , @NamedQuery(name = "Usuarios.findByTelefono", query = "SELECT u FROM Usuarios u WHERE u.telefono = :telefono")
    , @NamedQuery(name = "Usuarios.findByContra", query = "SELECT u FROM Usuarios u WHERE u.contra = :contra")
    , @NamedQuery(name = "Usuarios.findByInhabilitado", query = "SELECT u FROM Usuarios u WHERE u.inhabilitado = :inhabilitado")
    , @NamedQuery(name = "Usuarios.findByFechaUltimoIntentoLogin", query = "SELECT u FROM Usuarios u WHERE u.fechaUltimoIntentoLogin = :fechaUltimoIntentoLogin")
    , @NamedQuery(name = "Usuarios.findByNumeroIntentosLogin", query = "SELECT u FROM Usuarios u WHERE u.numeroIntentosLogin = :numeroIntentosLogin")
    , @NamedQuery(name = "Usuarios.findByBloqueado", query = "SELECT u FROM Usuarios u WHERE u.bloqueado = :bloqueado")
    , @NamedQuery(name = "Usuarios.findByBloqueadoHastaFecha", query = "SELECT u FROM Usuarios u WHERE u.bloqueadoHastaFecha = :bloqueadoHastaFecha")
    , @NamedQuery(name = "Usuarios.findById", query = "SELECT u FROM Usuarios u WHERE u.id = :id")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "usuario")
    private String usuario;
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
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "id")
    private String id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarios")
    private List<BitacoraContras> bitacoraContrasList;

    public Usuarios() {
        bloqueado = false;
        inhabilitado = false;
        numeroIntentosLogin = 0;        
    }

    public Usuarios(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlTransient
    @JsonIgnore
    public List<BitacoraContras> getBitacoraContrasList() {
        return bitacoraContrasList;
    }

    public void setBitacoraContrasList(List<BitacoraContras> bitacoraContrasList) {
        this.bitacoraContrasList = bitacoraContrasList;
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
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.postgres.Usuarios[ id=" + id + " ]";
    }

    public void aumentarNumeroDeIntentosLogin() {
        this.numeroIntentosLogin++;
    }

}
