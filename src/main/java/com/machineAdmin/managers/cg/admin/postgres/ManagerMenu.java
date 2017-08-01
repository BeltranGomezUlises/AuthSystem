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
package com.machineAdmin.managers.cg.admin.postgres;

import com.machineAdmin.daos.cg.admin.postgres.DaoMenu;
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import com.machineAdmin.managers.cg.commons.ManagerSQLFacade;
import com.machineAdmin.models.cg.ModelBitacoraGenerica;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerMenu extends ManagerSQLFacade<Menu, String>{
    
    public ManagerMenu(String usuario) {
        super(usuario, new DaoMenu());
    }

    @Override
    public ModelBitacoraGenerica getModeloBitacorizar(Menu entity) {
        return null;
    }

    @Override
    protected String getBitacoraCollectionName() {
        return null;
    }
    
   
}
