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
package com.machineAdmin.managers.cg.commons;

import com.machineAdmin.entities.cg.commons.IEntity;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;

/**
 * manager para entidades que tienen usuario creador y la habilidada de tener profundidad de acceso
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * @param <T> entidad a manejar
 * @param <K> objeto de llave primaria de la entidad
 */
public abstract class ManagerCatalog<T extends IEntity, K> extends ManagerFacade<T, K> {

    /**
     * indicador de profundiad de acceso a los registros
     */
    protected Profundidad profundidad;

    public ManagerCatalog() {
    }

    public ManagerCatalog(String token, Profundidad profundidad) throws TokenInvalidoException, TokenExpiradoException {
        super(token);
        this.profundidad = profundidad;
    }
                
    public Profundidad getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(Profundidad profundidad) {
        this.profundidad = profundidad;
    }
      
}
