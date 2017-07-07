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
package com.machineAdmin.daos;

import com.machineAdmin.daos.cg.DaoSQLFacade;
import com.machineAdmin.daos.jpaControllers.PermisoJpaController;
import com.machineAdmin.entities.postgres.Permiso;
import com.machineAdmin.utils.UtilsDB;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class DaoPermiso extends DaoSQLFacade<Permiso> {

    public DaoPermiso() {
        super(UtilsDB.getEMFactoryPostgres(), PermisoJpaController.class, Permiso.class);
    }

    @Override
    protected Class<?> getIdAttributeType() {
        return Integer.class;
    }
    
}
