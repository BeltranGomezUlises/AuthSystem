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
package com.auth.services;

import com.auth.entities.admin.Perfil;
import com.auth.entities.admin.Usuario;
import com.auth.entities.admin.UsuariosPerfil;
import com.auth.entities.admin.UsuariosPermisos;
import com.auth.managers.admin.ManagerUsuario;
import com.auth.managers.admin.ManagerUsuariosPerfil;
import com.auth.managers.exceptions.ParametroInvalidoException;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;
import com.auth.models.ModelAltaUsuario;
import com.auth.models.ModelAsignarPerfilesAlUsuario;
import com.auth.models.ModelAsignarPermisos;
import com.auth.models.ModelPermisosAsignados;
import com.auth.services.commons.ServiceFacadeLRUD;
import com.auth.utils.UtilsJWT;
import com.auth.utils.UtilsPermissions;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * servicios de administracion de usuarios del sistema
 *
 * @author Alonso --- alonso@kriblet.com
 */
@Path("/usuarios")
public class Usuarios extends ServiceFacadeLRUD<Usuario, Integer> {

    public Usuarios() {
        super(new ManagerUsuario());
    }

    /**
     * Da de alta un usuario al sistema
     *
     * @param token token de sesion
     * @param model modelo de alta de usuario, todos los campos necesarios
     * @return Usuario dado de alta en el sistema
     * @throws TokenInvalidoException si el token proporsionado no es valido
     * @throws TokenExpiradoException si el token proporsionado ya caducó
     * @throws ParametroInvalidoException si los datos proporcionados no son validos para un usuario del sistema
     * @throws Exception si existe un error de I/O
     */
    @POST
    @Path("/registrar")
    public Usuario registrar(@HeaderParam("Authorization") String token, ModelAltaUsuario model) throws TokenInvalidoException, TokenExpiradoException, ParametroInvalidoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return new ManagerUsuario().altaUsuario(model);
    }

    /**
     * asigna los permisos al usuario reemplazando los que tenia por los nuevos proporsionados
     *
     * @param token token de sesion
     * @param modelo modelo contener del usuario y los permisos a asignar
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     * @return sin data
     */
    @POST
    @Path("/asignarPermisos")
    public List<UsuariosPermisos> asignarPermisos(@HeaderParam("Authorization") String token, ModelAsignarPermisos modelo) throws TokenInvalidoException, TokenExpiradoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return new ManagerUsuario().reemplazarPermisos(modelo);
    }

    /**
     * asigna los perfiles al usuario reemplazando los que tenia por los nuevos proporsionados
     *
     * @param token token de sesion
     * @param modelo modelo generico para relacionar los permisos al usuario
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     * @return sin data
     */
    @POST
    @Path("/asignarPerfiles")
    public List<UsuariosPerfil> asignarPerfiles(@HeaderParam("Authorization") String token, ModelAsignarPerfilesAlUsuario modelo) throws TokenExpiradoException, TokenInvalidoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return new ManagerUsuariosPerfil().asignarPerfilesAlUsuario(modelo);
    }

    /**
     * sirve para obtener la lista de permisos asignados al usuario junto a su profundidad de acceso
     *
     * @param token token de sesion
     * @param usuario id de usuario a obtener permisos
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     * @return retorna en data, la lista de permisos asignados al usuario
     */
    @GET
    @Path("/permisos/{userId}")
    public ModelPermisosAsignados obtenerPermisosAsignados(@HeaderParam("Authorization") String token, @PathParam("userId") Integer usuario) throws TokenExpiradoException, TokenInvalidoException, Exception {
        UtilsJWT.validateSessionToken(token);
        return UtilsPermissions.permisosAsignadosAlUsuarioConProfundidad(usuario);
    }

    /**
     * sirve para obtener la lista de perfiles asignados al usuario
     *
     * @param token token de sesion
     * @param usuario id de usuario a obtener sus perfiles
     * @throws com.auth.managers.exceptions.TokenInvalidoException si el token proporsionado no es valido
     * @throws com.auth.managers.exceptions.TokenExpiradoException si el token proporsionado ya caducó
     * @return retorna en data, la lista de perfiles asignados al usuario
     */
    @GET
    @Path("/perfiles/{userId}")
    public List<Perfil> obtenerPerfilesAsignados(@HeaderParam("Authorization") String token, @PathParam("userId") int usuario) throws TokenInvalidoException, TokenExpiradoException {
        UtilsJWT.validateSessionToken(token);
        return new ManagerUsuariosPerfil().perfilesDeUsuario(usuario);
    }

}
