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

import com.machineAdmin.entities.cg.commons.EntityMongo;
import com.machineAdmin.managers.cg.admin.postgres.ManagerUsuario;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UtilsAuditoria {

    public static void auditar(String collectionName, ModeloAuditoria model) {
        new Thread(() -> {
            try {
                //buscar el usuario en base de datos para obtener su nombre
                ManagerUsuario managerUsuario = new ManagerUsuario();
                model.setUsuario(managerUsuario.nombreDeUsuario(UUID.fromString(model.getUsuario())));

                JacksonDBCollection<ModeloAuditoria, String> coll = JacksonDBCollection.wrap(UtilsDB.getAuditoriaCollection(collectionName), ModeloAuditoria.class, String.class);
                coll.insert(model);
            } catch (Exception e) {
                Logger.getLogger(UtilsAuditoria.class.getName()).log(Level.WARNING, "No se pudo auditar", e);
            }            
        }).start();
    }

    public static List<UtilsAuditoria.ModeloAuditoria> auditorias(String collectionName) {
        JacksonDBCollection<UtilsAuditoria.ModeloAuditoria, String> coll = JacksonDBCollection.wrap(UtilsDB.getAuditoriaCollection(collectionName), UtilsAuditoria.ModeloAuditoria.class, String.class);
        return coll.find().toArray();
    }

    public static List<UtilsAuditoria.ModeloAuditoria> auditoriasEntre(String collectionName, Date fechaInicial, Date fechaFinal) {
        JacksonDBCollection<UtilsAuditoria.ModeloAuditoria, String> coll = JacksonDBCollection.wrap(UtilsDB.getAuditoriaCollection(collectionName), UtilsAuditoria.ModeloAuditoria.class, String.class);
        return coll.find(DBQuery.and(
                DBQuery.greaterThanEquals("fecha", fechaInicial),
                DBQuery.lessThanEquals("fecha", fechaFinal)
        )).toArray();
    }

    public static List<UtilsAuditoria.ModeloAuditoria> auditoriasDesde(String collectionName, Date fechaInicial) {
        JacksonDBCollection<UtilsAuditoria.ModeloAuditoria, String> coll = JacksonDBCollection.wrap(UtilsDB.getAuditoriaCollection(collectionName), UtilsAuditoria.ModeloAuditoria.class, String.class);
        return coll.find(DBQuery.greaterThanEquals("fecha", fechaInicial)).toArray();
    }

    public static List<UtilsAuditoria.ModeloAuditoria> auditoriasHasta(String collectionName, Date fechaFinal) {
        JacksonDBCollection<UtilsAuditoria.ModeloAuditoria, String> coll = JacksonDBCollection.wrap(UtilsDB.getAuditoriaCollection(collectionName), UtilsAuditoria.ModeloAuditoria.class, String.class);
        return coll.find(DBQuery.lessThanEquals("fecha", fechaFinal)).toArray();
    }

    public static class ModeloAuditoria extends EntityMongo {

        private String usuario;
        private Date fecha;
        private String accion;
        private Object objetoAnterior;
        private Object objetoNuevo;
        private Object objectoReferencia;

        public ModeloAuditoria() {
        }

        public ModeloAuditoria(String usuario, Date fecha, String accion, Object objetoAnterior, Object objetoNuevo, Object objectoReferencia) {
            this.usuario = usuario;
            this.fecha = fecha;
            this.accion = accion;
            this.objetoAnterior = objetoAnterior;
            this.objetoNuevo = objetoNuevo;
            this.objectoReferencia = objectoReferencia;
        }

        public ModeloAuditoria(String usuario, String accion, Object objectoReferencia) {
            this.usuario = usuario;
            this.fecha = new Date();
            this.accion = accion;
            this.objectoReferencia = objectoReferencia;
        }

        public Object getObjetoAnterior() {
            return objetoAnterior;
        }

        public void setObjetoAnterior(Object objetoAnterior) {
            this.objetoAnterior = objetoAnterior;
        }

        public Object getObjetoNuevo() {
            return objetoNuevo;
        }

        public void setObjetoNuevo(Object objetoNuevo) {
            this.objetoNuevo = objetoNuevo;
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
