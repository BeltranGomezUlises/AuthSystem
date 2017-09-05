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
package com.machineAdmin.managers.cg.admin.mongo;

import com.machineAdmin.daos.cg.admin.mongo.DaoCGConfig;
import com.machineAdmin.entities.cg.admin.mongo.CGConfig;
import com.machineAdmin.entities.cg.commons.Profundidad;
import com.machineAdmin.managers.cg.commons.ManagerMongo;
import com.machineAdmin.managers.cg.exceptions.TokenExpiradoException;
import com.machineAdmin.managers.cg.exceptions.TokenInvalidoException;

/**
 * manejador de la entidad de configuraciones generales
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerCGConfig extends ManagerMongo<CGConfig> {

    public ManagerCGConfig() {
        super(new DaoCGConfig());
    }
        
    public ManagerCGConfig(String token, Profundidad profundidad) throws TokenInvalidoException, TokenExpiradoException {
        super(new DaoCGConfig(), token, profundidad);
    }
                                       
    @Override
    public String nombreColeccionParaRegistros() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
      
}
