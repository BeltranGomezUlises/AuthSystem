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
package com.machineAdmin.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machineAdmin.entities.cg.admin.mongo.BitacoraAcceso;
import com.machineAdmin.entities.cg.commons.EntityMongo;
import java.util.Date;
import java.util.List;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsBitacora {

    public static void bitacorizar(String usuario, String accion, String collectionName, Object objectToPersist) {
        JacksonDBCollection<Object, String> coll = JacksonDBCollection.wrap(UtilsDB.getCGCollection("bitacora." + collectionName), Object.class, String.class);
        coll.insert(new ModeloBitacora(usuario, accion, objectToPersist));
    }

    public static void bitacorizarLogOut(String usuario) {
        JacksonDBCollection<Object, String> coll = JacksonDBCollection.wrap(UtilsDB.getCGCollection("bitacora.logout"), Object.class, String.class);
        BitacoraAcceso bitacoraAcceso = new BitacoraAcceso(usuario);
        coll.insert(bitacoraAcceso);
    }

    public static void bitacorizarLogIn(String usuario) {
        JacksonDBCollection<Object, String> coll = JacksonDBCollection.wrap(UtilsDB.getCGCollection("bitacora.login"), Object.class, String.class);
        BitacoraAcceso bitacoraAcceso = new BitacoraAcceso(usuario);
        coll.insert(bitacoraAcceso);
    }

    public static List<ModeloBitacora> bitacoras(String collectionName) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getCGCollection("bitacora." + collectionName), ModeloBitacora.class, String.class);
        return coll.find().toArray();
    }

    public static class ModeloBitacora extends EntityMongo {

        private String usuario;
        private Date fecha;
        private String accion;
        private Object objectoReferencia;

        public ModeloBitacora() {
        }

        public ModeloBitacora(String usuario, String accion, Object objectoReferencia) {
            this.usuario = usuario;
            this.fecha = new Date();
            this.accion = accion;
            this.objectoReferencia = objectoReferencia;
        }

        public Date getFecha() {
            return fecha;
        }

        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getAccion() {
            return accion;
        }

        public void setAccion(String accion) {
            this.accion = accion;
        }

        public Object getObjectoReferencia() {
            return objectoReferencia;
        }

        public void setObjectoReferencia(Object objectoReferencia) {
            this.objectoReferencia = objectoReferencia;
        }

        @JsonIgnore
        @Override
        public String getId() {
            return id;
        }

        @JsonIgnore
        @Override
        public void setId(String id) {
            this.id = id;
        }

    }

}
