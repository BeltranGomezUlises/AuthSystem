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
import com.auth.entities.admin.BitacoraContras;
import com.auth.entities.admin.BitacoraContrasPK;
import javax.persistence.NoResultException;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class DaoBitacoraContra extends DaoSQLFacade<BitacoraContras, BitacoraContrasPK> {

    public DaoBitacoraContra() {
        super(BitacoraContras.class, BitacoraContrasPK.class);
    }

    public boolean exists(BitacoraContrasPK bitacoraContrasPK) {
        try {
            return this.getEM().createQuery("SELECT t.bitacoraContrasPK.usuario FROM BitacoraContras t WHERE t.bitacoraContrasPK.contra = :contra AND t.bitacoraContrasPK.usuario = :usuario")
                    .setParameter("contra", bitacoraContrasPK.getContra())
                    .setParameter("usuario", bitacoraContrasPK.getUsuario())
                    .setMaxResults(1)
                    .getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }

}
