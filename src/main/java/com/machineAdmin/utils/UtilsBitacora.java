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
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuario;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsBitacora {

    public static void bitacorizar(String collectionName, ModeloBitacora model) {
        new Thread(() -> {
            try {
                //buscar el usuario en base de datos para obtener su nombre
                ManagerUsuario managerUsuario = new ManagerUsuario();
                model.setUsuario(managerUsuario.nombreDeUsuario(UUID.fromString(model.getUsuario())));

                JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(collectionName), ModeloBitacora.class, String.class);
                coll.insert(model);
            } catch (Exception e) {
                Logger.getLogger(UtilsBitacora.class.getName()).log(Level.WARNING, "No se pudo bitacorizar", e);
            }
        }).start();        
    }

    public static void bitacorizarLogOut(String usuario) {
        JacksonDBCollection<Object, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection("logout"), Object.class, String.class);
        BitacoraAcceso bitacoraAcceso = new BitacoraAcceso(usuario);
        coll.insert(bitacoraAcceso);
    }

    public static void bitacorizarLogIn(String usuario) {
        JacksonDBCollection<Object, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection("login"), Object.class, String.class);
        BitacoraAcceso bitacoraAcceso = new BitacoraAcceso(usuario);
        coll.insert(bitacoraAcceso);
    }

    public static List<ModeloBitacora> bitacoras(String collectionName) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(collectionName), ModeloBitacora.class, String.class);
        return coll.find().toArray();
    }

    public static List<ModeloBitacora> bitacorasEntre(String collectionName, Date fechaInicial, Date fechaFinal) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(collectionName), ModeloBitacora.class, String.class);
        return coll.find(DBQuery.and(
                DBQuery.greaterThanEquals("fecha", fechaInicial),
                DBQuery.lessThanEquals("fecha", fechaFinal)
        )).toArray();
    }

    public static List<ModeloBitacora> bitacorasDesde(String collectionName, Date fechaInicial) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(collectionName), ModeloBitacora.class, String.class);
        return coll.find(DBQuery.greaterThanEquals("fecha", fechaInicial)).toArray();
    }

    public static List<ModeloBitacora> bitacorasHasta(String collectionName, Date fechaFinal) {
        JacksonDBCollection<ModeloBitacora, String> coll = JacksonDBCollection.wrap(UtilsDB.getBitacoraCollection(collectionName), ModeloBitacora.class, String.class);
        return coll.find(DBQuery.lessThanEquals("fecha", fechaFinal)).toArray();
    }

    public static class ModeloBitacora extends EntityMongo {

        private String usuario;
        private Date fecha;
        private String accion;
        private String ipCliente;
        private String sistemaOperativoCliente;

        public ModeloBitacora() {
        }

        public ModeloBitacora(String usuario, Date fecha, String accion) {
            this.usuario = usuario;
            this.fecha = fecha;
            this.accion = accion;
        }

        public ModeloBitacora(String usuario, Date fecha, String accion, HttpServletRequest request) {
            this.usuario = usuario;
            this.fecha = fecha;
            this.accion = accion;
            this.ipCliente = request.getRemoteAddr();
            this.sistemaOperativoCliente = UtilsService.obtenerSistemaOperativo(request.getHeader("User-Agent"));
        }

        public String getIpCliente() {
            return ipCliente;
        }

        public void setIpCliente(String ipCliente) {
            this.ipCliente = ipCliente;
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
