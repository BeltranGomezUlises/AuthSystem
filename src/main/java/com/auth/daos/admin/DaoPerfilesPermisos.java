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
import com.auth.entities.admin.PerfilesPermisos;
import com.auth.entities.admin.PerfilesPermisosPK;
import com.auth.entities.admin.Sucursal;
import com.auth.models.ModelPermisoAsignado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class DaoPerfilesPermisos extends DaoSQLFacade<PerfilesPermisos, PerfilesPermisosPK> {

    public DaoPerfilesPermisos() {
        super(PerfilesPermisos.class, PerfilesPermisosPK.class);
    }

    /**
     * reemplaza los permisos asignados al perfil por los nuevos proporsioandos
     *
     * @param perfilId identificador del perfil a asignar los permisos
     * @param permisosAAsignar modelos con los permisos a asignar y sus profundidades
     * @return asignaciones de permisos con el perfil
     */
    public List<PerfilesPermisos> reemplazarPermisosDelPerfil(final int perfilId, List<ModelPermisoAsignado> permisosAAsignar) {
        EntityManager em = this.getEM();
        em.getTransaction().begin();
        //borrar la asignacion de permisos del perfil         
        em.createQuery("DELETE FROM PerfilesPermisos t WHERE t.perfil1.id = :perfilId")
                .setParameter("perfilId", perfilId).executeUpdate();
        //crear las nuevas asignaciones y persistir
        List<PerfilesPermisos> perfilesPermisos = new ArrayList<>();
        PerfilesPermisos perfilPermiso;
        for (ModelPermisoAsignado modelPermisoAsignado : permisosAAsignar) {
            perfilPermiso = new PerfilesPermisos(perfilId, modelPermisoAsignado.getId(), em.find(Sucursal.class, modelPermisoAsignado.getSucursalId()).getId());
            perfilPermiso.setProfundidad(modelPermisoAsignado.getProfundidad());
            em.persist(perfilPermiso);
            perfilesPermisos.add(perfilPermiso);
        }
        em.getTransaction().commit();
        return perfilesPermisos;

    }

}
