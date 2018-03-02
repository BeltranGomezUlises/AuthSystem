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

import com.auth.daos.admin.DaoModulo;
import com.auth.entities.admin.Modulo;
import com.auth.managers.commons.ManagerSQL;
import com.auth.managers.exceptions.TokenExpiradoException;
import com.auth.managers.exceptions.TokenInvalidoException;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ManagerModulo extends ManagerSQL<Modulo, String> {

    public ManagerModulo() {
        super(new DaoModulo());
    }

    public ManagerModulo(String token) throws TokenInvalidoException, TokenExpiradoException {
        super(new DaoModulo(), token);
    }
    
}
