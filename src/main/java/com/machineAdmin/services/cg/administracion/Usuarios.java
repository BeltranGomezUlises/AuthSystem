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
package com.machineAdmin.services.cg.administracion;

import com.machineAdmin.daos.postgres.jpaControllers.TestJpaController;
import com.machineAdmin.daos.cg.exceptions.ConstraintException;
import com.machineAdmin.daos.cg.exceptions.SQLPersistenceException;
import com.machineAdmin.entities.cg.admin.postgres.Test;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuario;
import com.machineAdmin.managers.cg.exceptions.ContraseñaIncorrectaException;
import com.machineAdmin.managers.cg.exceptions.UsuarioBlockeadoException;
import com.machineAdmin.managers.cg.exceptions.UsuarioInexistenteException;
import com.machineAdmin.models.cg.ModelEncryptContent;
import com.machineAdmin.models.cg.ModelUsuarioLogeado;
import com.machineAdmin.models.cg.responsesCG.Response;
import com.machineAdmin.services.cg.commons.ServiceFacade;
import static com.machineAdmin.services.cg.commons.ServiceFacade.*;
import com.machineAdmin.utils.UtilsDB;
import com.machineAdmin.utils.UtilsJWT;
import com.machineAdmin.utils.UtilsJson;
import com.machineAdmin.utils.UtilsSecurity;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Path("/usuarios")
public class Usuarios extends ServiceFacade<Usuario> {

    public Usuarios() {
        super(new ManagerUsuario());
    }

    @Override
    public Response eliminar(String token, Usuario t) {
        return super.eliminar(token, t);
    }

    @Override
    public Response modificar(String token, Usuario t) {
        return super.modificar(token, t);
    }

    @Override
    public Response alta(String token, Usuario t) {
        return super.alta(token, t);
    }

    @Override
    public Response obtener(String token, String id) {
        return super.obtener(token, id);
    }

    @Override
    public Response listar(String token) {
        return super.listar(token);
    }

    @POST
    @Path("/login")
    public Response login(ModelEncryptContent content) {
        Response res = new Response();
        ManagerUsuario managerUsuario = new ManagerUsuario();
        try {
            Usuario usuarioAutenticando = UtilsJson.jsonDeserialize(UtilsSecurity.decryptBase64ByPrivateKey(content.getContent()), Usuario.class);
            usuarioAutenticando.setContra(UtilsSecurity.cifrarMD5(usuarioAutenticando.getContra()));
            Usuario usuarioLogeado = managerUsuario.login(usuarioAutenticando);

            ModelUsuarioLogeado modelUsuarioLogeado = new ModelUsuarioLogeado();

            BeanUtils.copyProperties(modelUsuarioLogeado, usuarioLogeado);

            res.setData(modelUsuarioLogeado);
            res.setMetaData(UtilsJWT.generateSessionToken(usuarioLogeado.getId().toString()));
            res.setMessage("Bienvenido " + usuarioLogeado.getNombre());
            res.setDevMessage("Token de sesion de usuario, necesario para las cabeceras de los demas servicios");
        } catch (UsuarioInexistenteException | ContraseñaIncorrectaException e) {
            setWarningResponse(res, "Usuario y/o contraseña incorrecto", "imposible inicio de sesión, por: " + e.getMessage());
        } catch (UsuarioBlockeadoException ex) {
            setWarningResponse(res, ex.getMessage(), "El Usuario está bloqueado temporalmente. Cause: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            setErrorResponse(res, ex);
        }
        return res;
    }

    @GET
    @Path("/initUser")
    public Response init() {
        Response r = new Response();
        try {                        
            ManagerUsuario managerUsuario = new ManagerUsuario();
            
            Usuario u = new Usuario();
            u.setNombre("ulises");
            u.setContra("1234");
            u.setCorreo("ubg700@gmail.com");
            u.setTelefono("6672118438");
            
            managerUsuario.persist(u);
            
            r.setData(u);            
        } catch (SQLPersistenceException ex) {
            setErrorResponse(r, ex);
        } catch (ConstraintException ex) {
            setWarningResponse(r, ex.getMessage(), ex.toString());
        }
        return r;
    }

    @GET
    @Path("/initTest")
    public Response initTest() {
        Response r = new Response();
        try {
            TestJpaController controller = new TestJpaController(UtilsDB.getEMFactoryPostgres());
            Test t = new Test();
            controller.create(t);

            r.setData(t);
        } catch (Exception ex) {
            setErrorResponse(r, ex);
            ex.printStackTrace();
        }
        return r;
    }

}
