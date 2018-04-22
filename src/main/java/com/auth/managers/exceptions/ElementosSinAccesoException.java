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
package com.auth.managers.exceptions;

import java.util.List;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ElementosSinAccesoException extends Exception {

    List elementosSinAcceso;

    public ElementosSinAccesoException() {
    }

    public ElementosSinAccesoException(List elementosSinAcceso, String mensaje) {
        super(mensaje);
        this.elementosSinAcceso = elementosSinAcceso;
    }

    public List getElementosSinAcceso() {
        return elementosSinAcceso;
    }

    public void setElementosSinAcceso(List elementosSinAcceso) {
        this.elementosSinAcceso = elementosSinAcceso;
    }

}
