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
package com.machineAdmin.entities.cg.admin;

import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class Permission {

    private List<Seccion> secciones;

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public class Seccion {

        private String name;
        private List<Seccion> Modulos;

        public List<Seccion> getModulos() {
            return Modulos;
        }

        public void setModulos(List<Seccion> Modulos) {
            this.Modulos = Modulos;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public class Module {

            private String name;
            private List<Menu> menus;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Menu> getMenus() {
                return menus;
            }

            public void setMenus(List<Menu> menus) {
                this.menus = menus;
            }

            public class Menu {

                private String name;
                private List<Action> acciones;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<Action> getAcciones() {
                    return acciones;
                }

                public void setAcciones(List<Action> acciones) {
                    this.acciones = acciones;
                }

                public class Action {

                    private String name;
                    private List<PermissionType> types;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public List<PermissionType> getTypes() {
                        return types;
                    }

                    public void setTypes(List<PermissionType> types) {
                        this.types = types;
                    }

                }

            }
        }

    }

    public static enum PermissionType {
        ALL, OWNER, OWNER_AND_PROFILE
    }

}
