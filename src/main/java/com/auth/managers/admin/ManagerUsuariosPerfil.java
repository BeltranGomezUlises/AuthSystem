/*
 * Copyright (C) 2017 Alonso --- alonso@kriblet.com
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
package com.auth.managers.admin;

import com.auth.daos.admin.DaoUsuariosPerfil;
import com.auth.entities.admin.Perfil;
import com.auth.entities.admin.UsuariosPerfil;
import com.auth.entities.admin.UsuariosPerfilPK;
import com.auth.managers.commons.ManagerSQL;
import com.auth.managers.exceptions.ParametroInvalidoException;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.ModelAsignarPerfilesAlUsuario;
import com.auth.models.ModelPerfilYHereda;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ManagerUsuariosPerfil extends ManagerSQL<UsuariosPerfil, UsuariosPerfilPK> {

    public ManagerUsuariosPerfil(){
        super(new DaoUsuariosPerfil());
    }

    public ManagerUsuariosPerfil(String token) throws TokenInvalidoException, TokenExpiradoException {
        super(new DaoUsuariosPerfil(), token);
    }
       
    public List<UsuariosPerfil> asignarPerfilesAlUsuario(ModelAsignarPerfilesAlUsuario modelo) throws Exception {
        
        if (modelo.getPerfiles().isEmpty()) {
            throw new ParametroInvalidoException("No puede dejar un usuario sin ningun perfil");
        }
        
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
            entidadRelacion = new UsuariosPerfil(modelo.getUserId(), perfil.getPerfilId());
            entidadRelacion.setHereda(perfil.isHereda());
            usuariosPerfilNuevos.add(entidadRelacion);
        }

        return dao.persistAll(usuariosPerfilNuevos);
    }

    public List<Perfil> perfilesDeUsuario(Integer usuarioId) {
        return dao.stream()
                .where(up -> up.getUsuariosPerfilPK().getUsuario().equals(usuarioId))
                .map(up -> up.getPerfil1())
                .collect(toList());
    }   

}
