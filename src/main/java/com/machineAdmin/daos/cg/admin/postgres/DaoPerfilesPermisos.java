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
package com.machineAdmin.daos.cg.admin.postgres;

import com.machineAdmin.daos.cg.commons.DaoSQLFacade;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisosPK;
import com.machineAdmin.utils.UtilsDB;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class DaoPerfilesPermisos extends DaoSQLFacade<PerfilesPermisos> {

    public DaoPerfilesPermisos() {
        super(UtilsDB.getEMFactoryCG(), PerfilesPermisos.class, "perfilesPermisos");
    }

    public void borrarRelaciones(List<PerfilesPermisosPK> pks) {
        EntityManager em = this.getEM();
        try {
            em.getTransaction().begin();
            PerfilesPermisos perfilesPermisos;

            for (PerfilesPermisosPK pk : pks) {
                perfilesPermisos = em.getReference(PerfilesPermisos.class, pk);                
                em.remove(perfilesPermisos);
            }

            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
//        this.getEM().createQuery("DELETE FROM PerfilesPermisos t WHERE t.perfilesPermisosPK = :pk")
//                .setParameter("pk", pk)
//                .executeUpdate();

    }  

}
