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
import com.auth.entities.admin.UsuariosPermisos;
import com.auth.entities.admin.UsuariosPermisosPK;
import com.auth.models.ModelPermisoAsignado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class DaoUsuariosPermisos extends DaoSQLFacade<UsuariosPermisos, UsuariosPermisosPK> {

    public DaoUsuariosPermisos() {
        super(UsuariosPermisos.class, UsuariosPermisosPK.class);
    }

    /**
     * reemplaza los permisos asignados a un usuario por los proporcionados en el modelos permisos
     *
     * @param usuarioId identificador del usuario a reemplazar los permisos
     * @param permisos lista de permisos con su profundidad
     * @return asignaciones de permisos al usuario
     */
    public List<UsuariosPermisos> reemplazarPermisos(Integer usuarioId, List<ModelPermisoAsignado> permisos) {
        EntityManager em = this.getEM();
        em.getTransaction().begin();
        //eliminar los actuales
        em.createQuery("DELETE FROM UsuariosPermisos u WHERE u.usuario1.id = :usuarioId")
                .setParameter("usuarioId", usuarioId)
                .executeUpdate();
        //reempleazar con los nuevos
        List<UsuariosPermisos> usuariosPermisos = new ArrayList<>();
        UsuariosPermisos usuarioPermiso;
        for (ModelPermisoAsignado permiso : permisos) {
            usuarioPermiso = new UsuariosPermisos(usuarioId, permiso.getId(), permiso.getProfundidad());
            em.persist(usuarioPermiso);
            usuariosPermisos.add(usuarioPermiso);
        }

        em.getTransaction().commit();
        return usuariosPermisos;
    }

}
