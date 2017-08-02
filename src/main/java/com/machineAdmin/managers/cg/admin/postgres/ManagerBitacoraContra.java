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

import com.machineAdmin.daos.cg.admin.postgres.DaoBitacoraContra;
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContras;
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContrasPK;
import com.machineAdmin.managers.cg.commons.ManagerSQLFacade;
import com.machineAdmin.models.cg.ModelBitacoraGenerica;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerBitacoraContra extends ManagerSQLFacade<BitacoraContras, BitacoraContrasPK> {

    public ManagerBitacoraContra(String usuario) {
        super(usuario, new DaoBitacoraContra());
    }

    @Override
    public ModelBitacoraGenerica modeloBitacorizar(BitacoraContras entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String bitacoraCollectionName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}
