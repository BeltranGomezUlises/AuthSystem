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
package com.machineAdmin.entities.cg.commons;

import java.util.UUID;
import org.eclipse.persistence.annotations.Converter;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
@Converter(name = "uuidConverter", converterClass = UUIDConverter.class)
public abstract class EntitySQLCatalog extends IEntity {

    /**
     * la propiedad uuid del usuario creador de esta
     * entidad, es necesario para poder registrar la alta de esta entidad en
     * base de datos y poder operar con la produndidad de los permisos de la
     * configuracion general, si NO se requiere en esta entidad registrar el
     * usuario creador retornar null en igual de la propiedad.
     *
     *
     *
     * @return UUID del usuario que crea esta entidad
     */
    public abstract UUID getUsuarioCreador() throws UnsupportedOperationException;

    /**
     * sobreescribir para asignar un UUID a la propiedad del usuario que creará
     * esta entidad, si No se requiere en esta entidad registrar el usuario
     * creador dejar vacío.
     *
     * @param usuarioCreador uuid del usuario que crea esta entidad
     */
    public abstract void setUsuarioCreador(UUID usuarioCreador) throws UnsupportedOperationException;

}
