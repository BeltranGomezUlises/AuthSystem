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
package com.machineAdmin.entities.cg.admin.mongo;

import com.machineAdmin.entities.cg.commons.EntityMongo;
import com.machineAdmin.models.cg.enums.PermissionType;
import java.util.List;

/**
 * modelo contenedor de todos los permisos disponibles por el sistema
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 * 
 */
public class AvailablePermission extends EntityMongo {

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
        private List<Module> Modulos;

        public Seccion(String name) {
            this.name = name;
        }

        public Seccion() {
        }

        public List<Module> getModulos() {
            return Modulos;
        }

        public void setModulos(List<Module> Modulos) {
            this.Modulos = Modulos;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Seccion{" + "name=" + name + ", Modulos=" + Modulos + '}';
        }
        /**
         * modelo de modulo representativo a un modulo de una seccion del sistema
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
                private List<Action> acciones;

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

                public List<Action> getAcciones() {
                    return acciones;
                }

                public void setAcciones(List<Action> acciones) {
                    this.acciones = acciones;
                }

                @Override
                public String toString() {
                    return "Menu{" + "name=" + name + ", acciones=" + acciones + '}';
                }
                /**
                 * modelo de accion representativa a un accion de un modulo del sistema
                 */
                public static class Action {

                    private String name;
                    private String id;
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

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    @Override
                    public String toString() {
                        return "Action{" + "name=" + name + ", id=" + id + ", types=" + types + '}';
                    }

                }

            }
        }

    }
   
}
