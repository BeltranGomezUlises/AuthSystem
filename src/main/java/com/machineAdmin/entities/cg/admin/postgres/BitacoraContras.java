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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
    , @NamedQuery(name = "BitacoraContras.findByUsuario", query = "SELECT b FROM BitacoraContras b WHERE b.bitacoraContrasPK.usuario = :usuario")
    , @NamedQuery(name = "BitacoraContras.findByContra", query = "SELECT b FROM BitacoraContras b WHERE b.bitacoraContrasPK.contra = :contra")})
public class BitacoraContras implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BitacoraContrasPK bitacoraContrasPK;
    @JoinColumn(name = "usuario", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuarios usuarios;

    public BitacoraContras() {
    }

    public BitacoraContras(BitacoraContrasPK bitacoraContrasPK) {
        this.bitacoraContrasPK = bitacoraContrasPK;
    }

    public BitacoraContras(String usuario, String contra) {
        this.bitacoraContrasPK = new BitacoraContrasPK(usuario, contra);
    }

    public BitacoraContrasPK getBitacoraContrasPK() {
        return bitacoraContrasPK;
    }

    public void setBitacoraContrasPK(BitacoraContrasPK bitacoraContrasPK) {
        this.bitacoraContrasPK = bitacoraContrasPK;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
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
        if ((this.bitacoraContrasPK == null && other.bitacoraContrasPK != null) || (this.bitacoraContrasPK != null && !this.bitacoraContrasPK.equals(other.bitacoraContrasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.machineAdmin.entities.postgres.BitacoraContras[ bitacoraContrasPK=" + bitacoraContrasPK + " ]";
    }
    
}
