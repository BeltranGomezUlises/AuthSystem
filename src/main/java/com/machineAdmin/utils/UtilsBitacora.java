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

import com.machineAdmin.entities.cg.admin.mongo.BitacoraAcceso;
import com.machineAdmin.entities.cg.commons.EntityMongo;
import java.util.Date;
import java.util.List;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsBitacora {

    private static final String PREFIJO_BITACORA = "bitacora.";

    public static void bitacorizar(String collectionName, ModeloBitacora model) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(PREFIJO_BITACORA + collectionName), ModeloBitacora.class, String.class);
        coll.insert(model);
    }

    public static void bitacorizarLogOut(String usuario) {
        JacksonDBCollection<Object, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(PREFIJO_BITACORA + "logout"), Object.class, String.class);
        BitacoraAcceso bitacoraAcceso = new BitacoraAcceso(usuario);
        coll.insert(bitacoraAcceso);
    }

    public static void bitacorizarLogIn(String usuario) {
        JacksonDBCollection<Object, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(PREFIJO_BITACORA + "login"), Object.class, String.class);
        BitacoraAcceso bitacoraAcceso = new BitacoraAcceso(usuario);
        coll.insert(bitacoraAcceso);
    }

    public static List<ModeloBitacora> bitacoras(String collectionName) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(PREFIJO_BITACORA + collectionName), ModeloBitacora.class, String.class);
        return coll.find().toArray();
    }

    public static List<ModeloBitacora> bitacorasEntre(String collectionName, Date fechaInicial, Date fechaFinal) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(PREFIJO_BITACORA + collectionName), ModeloBitacora.class, String.class);
        return coll.find(DBQuery.and(
                DBQuery.greaterThanEquals("fecha", fechaInicial),
                DBQuery.lessThanEquals("fecha", fechaFinal)
        )).toArray();
    }

    public static List<ModeloBitacora> bitacorasDesde(String collectionName, Date fechaInicial) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(PREFIJO_BITACORA + collectionName), ModeloBitacora.class, String.class);
        return coll.find(DBQuery.greaterThanEquals("fecha", fechaInicial)).toArray();
    }

    public static List<ModeloBitacora> bitacorasHasta(String collectionName, Date fechaFinal) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(PREFIJO_BITACORA + collectionName), ModeloBitacora.class, String.class);
        return coll.find(DBQuery.lessThanEquals("fecha", fechaFinal)).toArray();
    }

    public static class ModeloBitacora extends EntityMongo {

        private String usuario;
        private Date fecha;
        private String accion;
        private String ipCliente;
        private String navegadorCliente;
        private String sistemaOperativoCliente;

        public ModeloBitacora() {
        }
        
        public ModeloBitacora(String usuario, Date fecha, String accion) {
            this.usuario = usuario;
            this.fecha = fecha;
            this.accion = accion;
        }

        public ModeloBitacora(String usuario, Date fecha, String accion, String ipCliente, String navegadorCliente, String sistemaOperativoCliente) {
            this.usuario = usuario;
            this.fecha = fecha;
            this.accion = accion;
            this.ipCliente = ipCliente;
            this.navegadorCliente = navegadorCliente;
            this.sistemaOperativoCliente = sistemaOperativoCliente;
        }
                
        public String getIpCliente() {
            return ipCliente;
        }

        public void setIpCliente(String ipCliente) {
            this.ipCliente = ipCliente;
        }

        public String getNavegadorCliente() {
            return navegadorCliente;
        }

        public void setNavegadorCliente(String navegadorCliente) {
            this.navegadorCliente = navegadorCliente;
        }

        public String getSistemaOperativoCliente() {
            return sistemaOperativoCliente;
        }

        public void setSistemaOperativoCliente(String sistemaOperativoCliente) {
            this.sistemaOperativoCliente = sistemaOperativoCliente;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public Date getFecha() {
            return fecha;
        }

        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }

        public String getAccion() {
            return accion;
        }

        public void setAccion(String accion) {
            this.accion = accion;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

    }

}
