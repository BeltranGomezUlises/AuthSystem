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
package com.auth.daos.admin;

import com.auth.daos.commons.DaoSQLFacade;
import com.auth.entities.admin.Usuario;
import com.auth.entities.admin.UsuariosPerfil;
import com.auth.entities.admin.UsuariosPerfilPK;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class DaoUsuario extends DaoSQLFacade<Usuario, Integer> {

    public DaoUsuario() {
        super(Usuario.class, Integer.class);
    }

    public void registrarUsuario(Usuario nuevoUsuario, List<Integer> perfilesAsignar) {
        EntityManager em = this.getEM();
        em.getTransaction().begin();
        em.persist(nuevoUsuario);
        em.flush();        
        //generar relacion con los ids de los perfiles del usuario
        UsuariosPerfil up;
        for (Integer perfilId : perfilesAsignar) {
            up = new UsuariosPerfil();
            up.setHereda(Boolean.TRUE);
            up.setUsuariosPerfilPK(new UsuariosPerfilPK(nuevoUsuario.getId(), perfilId));
            em.persist(up);
        }
        em.getTransaction().commit();
    }

}
