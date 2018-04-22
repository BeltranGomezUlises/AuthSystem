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
package com.auth.models;

import com.auth.entities.commons.Profundidad;
import java.util.List;

/**
 *
 * @author Alonso --- alonso@kriblet.com
 */
public class ModelPermisosAsignados {

    private List<ModelSeccion> secciones;

    public ModelPermisosAsignados(List<ModelSeccion> secciones) {
        this.secciones = secciones;
    }

    public ModelPermisosAsignados() {
    }

    public List<ModelSeccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<ModelSeccion> secciones) {
        this.secciones = secciones;
    }

    @Override
    public String toString() {
        return "Permission{" + "secciones=" + secciones + '}';
    }

    /**
     * modelo de seccion represtativo de una seccion del sistema
     */
    public static class ModelSeccion {

        private String nombre;
        private String id;

        private List<ModelModulo> modulos;

        public ModelSeccion() {
        }

        public ModelSeccion(String nombre, String id, List<ModelModulo> modulos) {
            this.nombre = nombre;
            this.id = id;
            this.modulos = modulos;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<ModelModulo> getModulos() {
            return modulos;
        }

        public void setModulos(List<ModelModulo> modulos) {
            this.modulos = modulos;
        }

        /**
         * modelo de modulo representativo a un modulo de una seccion del sistema
         */
        public static class ModelModulo {

            private String name;
            private String id;
            private List<ModelMenu> menus;

            public ModelModulo() {
            }

            public ModelModulo(String name, String id) {
                this.name = name;
                this.id = id;
            }

            public ModelModulo(String name, String id, List<ModelMenu> menus) {
                this.name = name;
                this.id = id;
                this.menus = menus;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public ModelModulo(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ModelMenu> getMenus() {
                return menus;
            }

            public void setMenus(List<ModelMenu> menus) {
                this.menus = menus;
            }

            @Override
            public String toString() {
                return "Module{" + "name=" + name + ", menus=" + menus + '}';
            }

            /**
             * modelo de menu representativo de un menu de un modulo del sistema
             */
            public static class ModelMenu {

                private String nombre;
                private String id;

                private List<ModelPermiso> permisos;

                public ModelMenu() {
                }

                public ModelMenu(String nombre, String id, List<ModelPermiso> permisos) {
                    this.nombre = nombre;
                    this.id = id;
                    this.permisos = permisos;
                }

                public ModelMenu(String id) {
                    this.id = id;
                }

                public String getNombre() {
                    return nombre;
                }

                public void setNombre(String nombre) {
                    this.nombre = nombre;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public List<ModelPermiso> getPermisos() {
                    return permisos;
                }

                public void setPermisos(List<ModelPermiso> permisos) {
                    this.permisos = permisos;
                }

                /**
                 * modelo de accion representativa a un accion de un modulo del sistema
                 */
                public static class ModelPermiso {

                    private String nombre;
                    private String id;
                    private Profundidad profundidad;
                    private Integer sucursalId;

                    public ModelPermiso() {
                    }

                    public ModelPermiso(String nombre, String id, Profundidad profundidad, int sucursalId) {
                        this.nombre = nombre;
                        this.id = id;
                        this.profundidad = profundidad;
                        this.sucursalId = sucursalId;
                    }

                    public Integer getSucursalId() {
                        return sucursalId;
                    }

                    public void setSucursalId(Integer sucursalId) {
                        this.sucursalId = sucursalId;
                    }

                    public ModelPermiso(String id) {
                        this.id = id;
                    }

                    public String getNombre() {
                        return nombre;
                    }

                    public void setNombre(String nombre) {
                        this.nombre = nombre;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public Profundidad getProfundidad() {
                        return profundidad;
                    }

                    public void setProfundidad(Profundidad profundidad) {
                        this.profundidad = profundidad;
                    }

                }

            }
        }

    }

}
