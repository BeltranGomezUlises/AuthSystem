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
package com.machineAdmin.daos.cg.admin.postgres.jpaControllers;

import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.NonexistentEntityException;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisosPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UsuariosPermisosJpaController implements Serializable {

    public UsuariosPermisosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuariosPermisos usuariosPermisos) throws PreexistingEntityException, Exception {
        if (usuariosPermisos.getUsuariosPermisosPK() == null) {
            usuariosPermisos.setUsuariosPermisosPK(new UsuariosPermisosPK());
        }
        usuariosPermisos.getUsuariosPermisosPK().setUsuario(usuariosPermisos.getUsuario1().getId());
        usuariosPermisos.getUsuariosPermisosPK().setPermiso(usuariosPermisos.getPermiso1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso permiso1 = usuariosPermisos.getPermiso1();
            if (permiso1 != null) {
                permiso1 = em.getReference(permiso1.getClass(), permiso1.getId());
                usuariosPermisos.setPermiso1(permiso1);
            }
            Usuario usuario1 = usuariosPermisos.getUsuario1();
            if (usuario1 != null) {
                usuario1 = em.getReference(usuario1.getClass(), usuario1.getId());
                usuariosPermisos.setUsuario1(usuario1);
            }
            em.persist(usuariosPermisos);
            if (permiso1 != null) {
                permiso1.getUsuariosPermisosList().add(usuariosPermisos);
                permiso1 = em.merge(permiso1);
            }
            if (usuario1 != null) {
                usuario1.getUsuariosPermisosList().add(usuariosPermisos);
                usuario1 = em.merge(usuario1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuariosPermisos(usuariosPermisos.getUsuariosPermisosPK()) != null) {
                throw new PreexistingEntityException("UsuariosPermisos " + usuariosPermisos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuariosPermisos usuariosPermisos) throws NonexistentEntityException, Exception {
        usuariosPermisos.getUsuariosPermisosPK().setUsuario(usuariosPermisos.getUsuario1().getId());
        usuariosPermisos.getUsuariosPermisosPK().setPermiso(usuariosPermisos.getPermiso1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuariosPermisos persistentUsuariosPermisos = em.find(UsuariosPermisos.class, usuariosPermisos.getUsuariosPermisosPK());
            Permiso permiso1Old = persistentUsuariosPermisos.getPermiso1();
            Permiso permiso1New = usuariosPermisos.getPermiso1();
            Usuario usuario1Old = persistentUsuariosPermisos.getUsuario1();
            Usuario usuario1New = usuariosPermisos.getUsuario1();
            if (permiso1New != null) {
                permiso1New = em.getReference(permiso1New.getClass(), permiso1New.getId());
                usuariosPermisos.setPermiso1(permiso1New);
            }
            if (usuario1New != null) {
                usuario1New = em.getReference(usuario1New.getClass(), usuario1New.getId());
                usuariosPermisos.setUsuario1(usuario1New);
            }
            usuariosPermisos = em.merge(usuariosPermisos);
            if (permiso1Old != null && !permiso1Old.equals(permiso1New)) {
                permiso1Old.getUsuariosPermisosList().remove(usuariosPermisos);
                permiso1Old = em.merge(permiso1Old);
            }
            if (permiso1New != null && !permiso1New.equals(permiso1Old)) {
                permiso1New.getUsuariosPermisosList().add(usuariosPermisos);
                permiso1New = em.merge(permiso1New);
            }
            if (usuario1Old != null && !usuario1Old.equals(usuario1New)) {
                usuario1Old.getUsuariosPermisosList().remove(usuariosPermisos);
                usuario1Old = em.merge(usuario1Old);
            }
            if (usuario1New != null && !usuario1New.equals(usuario1Old)) {
                usuario1New.getUsuariosPermisosList().add(usuariosPermisos);
                usuario1New = em.merge(usuario1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuariosPermisosPK id = usuariosPermisos.getUsuariosPermisosPK();
                if (findUsuariosPermisos(id) == null) {
                    throw new NonexistentEntityException("The usuariosPermisos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuariosPermisosPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuariosPermisos usuariosPermisos;
            try {
                usuariosPermisos = em.getReference(UsuariosPermisos.class, id);
                usuariosPermisos.getUsuariosPermisosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuariosPermisos with id " + id + " no longer exists.", enfe);
            }
            Permiso permiso1 = usuariosPermisos.getPermiso1();
            if (permiso1 != null) {
                permiso1.getUsuariosPermisosList().remove(usuariosPermisos);
                permiso1 = em.merge(permiso1);
            }
            Usuario usuario1 = usuariosPermisos.getUsuario1();
            if (usuario1 != null) {
                usuario1.getUsuariosPermisosList().remove(usuariosPermisos);
                usuario1 = em.merge(usuario1);
            }
            em.remove(usuariosPermisos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuariosPermisos> findUsuariosPermisosEntities() {
        return findUsuariosPermisosEntities(true, -1, -1);
    }

    public List<UsuariosPermisos> findUsuariosPermisosEntities(int maxResults, int firstResult) {
        return findUsuariosPermisosEntities(false, maxResults, firstResult);
    }

    private List<UsuariosPermisos> findUsuariosPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuariosPermisos.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public UsuariosPermisos findUsuariosPermisos(UsuariosPermisosPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuariosPermisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuariosPermisos> rt = cq.from(UsuariosPermisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
