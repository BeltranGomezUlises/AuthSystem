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

import com.machineAdmin.entities.cg.commons.EntitySQL;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Entity
@Table(name = "bitacora_contras")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BitacoraContras.findAll", query = "SELECT b FROM BitacoraContras b")
    , @NamedQuery(name = "BitacoraContras.findByContra", query = "SELECT b FROM BitacoraContras b WHERE b.bitacoraContrasPK.contra = :contra")
    , @NamedQuery(name = "BitacoraContras.findByFechaAsignada", query = "SELECT b FROM BitacoraContras b WHERE b.fechaAsignada = :fechaAsignada")})
public class BitacoraContras extends EntitySQL implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BitacoraContrasPK bitacoraContrasPK;
    @Column(name = "fecha_asignada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignada;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario1;

    public BitacoraContras() {
    }

    public BitacoraContras(BitacoraContrasPK bitacoraContrasPK) {
        this.bitacoraContrasPK = bitacoraContrasPK;
    }

    public BitacoraContras(UUID usuario, String contra) {
        this.bitacoraContrasPK = new BitacoraContrasPK(usuario, contra);
    }

    public BitacoraContrasPK getBitacoraContrasPK() {
        return bitacoraContrasPK;
    }

    public void setBitacoraContrasPK(BitacoraContrasPK bitacoraContrasPK) {
        this.bitacoraContrasPK = bitacoraContrasPK;
    }

    public Date getFechaAsignada() {
        return fechaAsignada;
    }

    public void setFechaAsignada(Date fechaAsignada) {
        this.fechaAsignada = fechaAsignada;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bitacoraContrasPK != null ? bitacoraContrasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraContras)) {
            return false;
        }
        BitacoraContras other = (BitacoraContras) object;
        return !((this.bitacoraContrasPK == null && other.bitacoraContrasPK != null) || (this.bitacoraContrasPK != null && !this.bitacoraContrasPK.equals(other.bitacoraContrasPK)));
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.cg.admin.postgres.BitacoraContras[ bitacoraContrasPK=" + bitacoraContrasPK + " ]";
    }

    @Override
    public Object getId() {
        return bitacoraContrasPK;
    }
    
}
