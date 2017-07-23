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
import com.machineAdmin.entities.cg.admin.postgres.Perfil;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisosPK;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class PerfilesPermisosJpaController implements Serializable {

    public PerfilesPermisosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PerfilesPermisos perfilesPermisos) throws PreexistingEntityException, Exception {
        if (perfilesPermisos.getPerfilesPermisosPK() == null) {
            perfilesPermisos.setPerfilesPermisosPK(new PerfilesPermisosPK());
        }
        perfilesPermisos.getPerfilesPermisosPK().setPerfil(perfilesPermisos.getPerfil1().getId());
        perfilesPermisos.getPerfilesPermisosPK().setPermiso(perfilesPermisos.getPermiso1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Perfil perfil1 = perfilesPermisos.getPerfil1();
            if (perfil1 != null) {
                perfil1 = em.getReference(perfil1.getClass(), perfil1.getId());
                perfilesPermisos.setPerfil1(perfil1);
            }
            Permiso permiso1 = perfilesPermisos.getPermiso1();
            if (permiso1 != null) {
                permiso1 = em.getReference(permiso1.getClass(), permiso1.getId());
                perfilesPermisos.setPermiso1(permiso1);
            }
            em.persist(perfilesPermisos);
            if (perfil1 != null) {
                perfil1.getPerfilesPermisosList().add(perfilesPermisos);
                perfil1 = em.merge(perfil1);
            }
            if (permiso1 != null) {
                permiso1.getPerfilesPermisosList().add(perfilesPermisos);
                permiso1 = em.merge(permiso1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPerfilesPermisos(perfilesPermisos.getPerfilesPermisosPK()) != null) {
                throw new PreexistingEntityException("PerfilesPermisos " + perfilesPermisos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PerfilesPermisos perfilesPermisos) throws NonexistentEntityException, Exception {
        perfilesPermisos.getPerfilesPermisosPK().setPerfil(perfilesPermisos.getPerfil1().getId());
        perfilesPermisos.getPerfilesPermisosPK().setPermiso(perfilesPermisos.getPermiso1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PerfilesPermisos persistentPerfilesPermisos = em.find(PerfilesPermisos.class, perfilesPermisos.getPerfilesPermisosPK());
            Perfil perfil1Old = persistentPerfilesPermisos.getPerfil1();
            Perfil perfil1New = perfilesPermisos.getPerfil1();
            Permiso permiso1Old = persistentPerfilesPermisos.getPermiso1();
            Permiso permiso1New = perfilesPermisos.getPermiso1();
            if (perfil1New != null) {
                perfil1New = em.getReference(perfil1New.getClass(), perfil1New.getId());
                perfilesPermisos.setPerfil1(perfil1New);
            }
            if (permiso1New != null) {
                permiso1New = em.getReference(permiso1New.getClass(), permiso1New.getId());
                perfilesPermisos.setPermiso1(permiso1New);
            }
            perfilesPermisos = em.merge(perfilesPermisos);
            if (perfil1Old != null && !perfil1Old.equals(perfil1New)) {
                perfil1Old.getPerfilesPermisosList().remove(perfilesPermisos);
                perfil1Old = em.merge(perfil1Old);
            }
            if (perfil1New != null && !perfil1New.equals(perfil1Old)) {
                perfil1New.getPerfilesPermisosList().add(perfilesPermisos);
                perfil1New = em.merge(perfil1New);
            }
            if (permiso1Old != null && !permiso1Old.equals(permiso1New)) {
                permiso1Old.getPerfilesPermisosList().remove(perfilesPermisos);
                permiso1Old = em.merge(permiso1Old);
            }
            if (permiso1New != null && !permiso1New.equals(permiso1Old)) {
                permiso1New.getPerfilesPermisosList().add(perfilesPermisos);
                permiso1New = em.merge(permiso1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PerfilesPermisosPK id = perfilesPermisos.getPerfilesPermisosPK();
                if (findPerfilesPermisos(id) == null) {
                    throw new NonexistentEntityException("The perfilesPermisos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PerfilesPermisosPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PerfilesPermisos perfilesPermisos;
            try {
                perfilesPermisos = em.getReference(PerfilesPermisos.class, id);
                perfilesPermisos.getPerfilesPermisosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfilesPermisos with id " + id + " no longer exists.", enfe);
            }
            Perfil perfil1 = perfilesPermisos.getPerfil1();
            if (perfil1 != null) {
                perfil1.getPerfilesPermisosList().remove(perfilesPermisos);
                perfil1 = em.merge(perfil1);
            }
            Permiso permiso1 = perfilesPermisos.getPermiso1();
            if (permiso1 != null) {
                permiso1.getPerfilesPermisosList().remove(perfilesPermisos);
                permiso1 = em.merge(permiso1);
            }
            em.remove(perfilesPermisos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PerfilesPermisos> findPerfilesPermisosEntities() {
        return findPerfilesPermisosEntities(true, -1, -1);
    }

    public List<PerfilesPermisos> findPerfilesPermisosEntities(int maxResults, int firstResult) {
        return findPerfilesPermisosEntities(false, maxResults, firstResult);
    }

    private List<PerfilesPermisos> findPerfilesPermisosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PerfilesPermisos.class));
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

    public PerfilesPermisos findPerfilesPermisos(PerfilesPermisosPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PerfilesPermisos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilesPermisosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PerfilesPermisos> rt = cq.from(PerfilesPermisos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
