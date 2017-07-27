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
package com.machineAdmin.models.cg;

import com.machineAdmin.models.cg.enums.PermissionType;
import java.util.List;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ModelPermisosAsignados {

    private List<Seccion> secciones;

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    @Override
    public String toString() {
        return "Permission{" + "secciones=" + secciones + '}';
    }

    /**
     * modelo de seccion represtativo de una seccion del sistema
     */
    public static class Seccion {

        private String name;
        private List<Module> modulos;

        public Seccion(String name) {
            this.name = name;
        }

        public Seccion() {
        }

        public List<Module> getModulos() {
            return modulos;
        }

        public void setModulos(List<Module> modulos) {
            this.modulos = modulos;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Seccion{" + "name=" + name + ", modulos=" + modulos + '}';
        }

        /**
         * modelo de modulo representativo a un modulo de una seccion del
         * sistema
         */
        public static class Module {

            private String name;
            private List<Menu> menus;

            public Module() {
            }

            public Module(String name) {
                this.name = name;
            }

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

            @Override
            public String toString() {
                return "Module{" + "name=" + name + ", menus=" + menus + '}';
            }

            /**
             * modelo de menu representativo de un menu de un modulo del sistema
             */
            public static class Menu {

                private String name;
                private List<Permiso> permisos;

                public Menu(String name) {
                    this.name = name;
                }

                public Menu() {
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<Permiso> getPermisos() {
                    return permisos;
                }

                public void setPermisos(List<Permiso> permisos) {
                    this.permisos = permisos;
                }

                /**
                 * modelo de accion representativa a un accion de un modulo del
                 * sistema
                 */
                public static class Permiso {

                    private String name;
                    private String id;
                    private PermissionType types;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public PermissionType getTypes() {
                        return types;
                    }

                    public void setTypes(PermissionType types) {
                        this.types = types;
                    }

                }

            }
        }

    }

}
