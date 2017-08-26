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
package com.machineAdmin.entities.cg.commons;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * clase padre para entidades sql que tienen un usuario creador y puedes acceder
 * con profundidad
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@MappedSuperclass
public abstract class EntitySQLCatalog extends IEntity implements Serializable {

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Integer id;

    @Column(name = "usuario_creador")
    private Integer usuarioCreador;

    public EntitySQLCatalog() {
    }

    public EntitySQLCatalog(Integer id) {
        this.id = id;
    }

    public EntitySQLCatalog(Integer id, Integer usuarioCreador) {
        this.id = id;
        this.usuarioCreador = usuarioCreador;
    }
        
    //@Override
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(Integer usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final EntitySQLCatalog other = (EntitySQLCatalog) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
