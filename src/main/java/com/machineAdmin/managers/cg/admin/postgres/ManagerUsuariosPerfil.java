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

import com.machineAdmin.daos.cg.admin.postgres.DaoUsuariosPerfil;
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfil;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPerfilPK;
import com.machineAdmin.managers.cg.commons.ManagerSQLCatalogFacade;
import com.machineAdmin.models.cg.ModelAsignarPerfilesAlUsuario;
import com.machineAdmin.models.cg.ModelPerfilYHereda;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ManagerUsuariosPerfil extends ManagerSQLCatalogFacade<UsuariosPerfil, UsuariosPerfilPK> {

    public ManagerUsuariosPerfil(String usuario) {
        super(usuario, new DaoUsuariosPerfil());
    }

    public ManagerUsuariosPerfil() {
        super(new DaoUsuariosPerfil());
    }

    public void asignarPerfilesAlUsuario(ModelAsignarPerfilesAlUsuario modelo) throws Exception {
        //remover los perfiles actuales
        List<UsuariosPerfilPK> idsActuales = this.stream()
                .filter(up -> up.getUsuariosPerfilPK().getUsuario().equals(modelo.getUserId()))
                .map(up -> up.getUsuariosPerfilPK())
                .collect(toList());

        dao.deleteAll(idsActuales);

        //asignar los perfiles del modelo
        List<UsuariosPerfil> usuariosPerfilNuevos = new ArrayList<>();
        UsuariosPerfil entidadRelacion;
        for (ModelPerfilYHereda perfil : modelo.getPerfiles()) {
            entidadRelacion = new UsuariosPerfil(modelo.getUserId(), UUID.fromString(perfil.getPerfilId()));
            entidadRelacion.setHereda(perfil.isHereda());
            usuariosPerfilNuevos.add(entidadRelacion);
        }

        dao.persistAll(usuariosPerfilNuevos);
    }

    public List<Perfil> perfilesDeUsuario(UUID usuarioId) {
        return dao.stream()
                .where(up -> up.getUsuariosPerfilPK().getUsuario().equals(usuarioId))
                .map(up -> up.getPerfil1())
                .collect(toList());
    }

    @Override
    protected String nombreColeccionParaRegistros() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
