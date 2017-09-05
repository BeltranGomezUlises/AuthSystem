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
package com.machineAdmin.services.cg.pruebas;

import com.machineAdmin.entities.cg.admin.mongo.EntidadMongoPrueba;
import com.machineAdmin.managers.cg.admin.mongo.ManagerMongoPrueba;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacadeCatalogMongo;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/mongoPrueba")
public class MongoPruebas extends ServiceFacadeCatalogMongo<EntidadMongoPrueba, Object> {

    public MongoPruebas() {
        super(new ManagerMongoPrueba());
    }

    @Override
    public Response eliminar(HttpServletRequest request, String token, EntidadMongoPrueba t) {
        return super.eliminar(request, token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response modificar(HttpServletRequest request, String token, EntidadMongoPrueba t) {
        return super.modificar(request, token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response alta(HttpServletRequest request, String token, EntidadMongoPrueba t) {
        return super.alta(request, token, t); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response detalle(HttpServletRequest request, String token, String id) {
        return super.detalle(request, token, id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response listar(HttpServletRequest request, String token) {
        return super.listar(request, token); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response eliminarVarios(HttpServletRequest request, String token, List<EntidadMongoPrueba> t) {
        return super.eliminarVarios(request, token, t);
    }

    @Override
    public Response modificarVarios(HttpServletRequest request, String token, List<EntidadMongoPrueba> ts) {
        return super.modificarVarios(request, token, ts);
    }
   
}
