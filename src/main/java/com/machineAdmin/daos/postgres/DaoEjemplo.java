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
package com.machineAdmin.daos.postgres;

import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.daos.postgres.jpaControllers.EjemploJpaController;
import com.machineAdmin.entities.postgres.Ejemplo;
import com.machineAdmin.utils.UtilsDB;

/**
 * entidad de ejemplo de uso de una unidad de persistencia distinta a la CG
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class DaoEjemplo extends DaoSQLFacade<Ejemplo>{

    public DaoEjemplo() {
        super(UtilsDB.getEMFactoryMachineAdmin(), EjemploJpaController.class, Ejemplo.class, "ejemplo");
    }

    @Override
    protected Class<?> getIdAttributeType() {
        return Integer.class;
    }
    
}
