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

import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.IllegalOrphanException;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.NonexistentEntityException;
import com.machineAdmin.daos.cg.admin.postgres.jpaControllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import com.machineAdmin.entities.cg.admin.postgres.PerfilesPermisos;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import java.util.ArrayList;
import java.util.List;
import com.machineAdmin.entities.cg.admin.postgres.UsuariosPermisos;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) throws PreexistingEntityException, Exception {
        if (permiso.getPerfilesPermisosList() == null) {
            permiso.setPerfilesPermisosList(new ArrayList<PerfilesPermisos>());
        }
        if (permiso.getUsuariosPermisosList() == null) {
            permiso.setUsuariosPermisosList(new ArrayList<UsuariosPermisos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Menu menu = permiso.getMenu();
            if (menu != null) {
                menu = em.getReference(menu.getClass(), menu.getId());
                permiso.setMenu(menu);
            }
            List<PerfilesPermisos> attachedPerfilesPermisosList = new ArrayList<PerfilesPermisos>();
            for (PerfilesPermisos perfilesPermisosListPerfilesPermisosToAttach : permiso.getPerfilesPermisosList()) {
                perfilesPermisosListPerfilesPermisosToAttach = em.getReference(perfilesPermisosListPerfilesPermisosToAttach.getClass(), perfilesPermisosListPerfilesPermisosToAttach.getPerfilesPermisosPK());
                attachedPerfilesPermisosList.add(perfilesPermisosListPerfilesPermisosToAttach);
            }
            permiso.setPerfilesPermisosList(attachedPerfilesPermisosList);
            List<UsuariosPermisos> attachedUsuariosPermisosList = new ArrayList<UsuariosPermisos>();
            for (UsuariosPermisos usuariosPermisosListUsuariosPermisosToAttach : permiso.getUsuariosPermisosList()) {
                usuariosPermisosListUsuariosPermisosToAttach = em.getReference(usuariosPermisosListUsuariosPermisosToAttach.getClass(), usuariosPermisosListUsuariosPermisosToAttach.getUsuariosPermisosPK());
                attachedUsuariosPermisosList.add(usuariosPermisosListUsuariosPermisosToAttach);
            }
            permiso.setUsuariosPermisosList(attachedUsuariosPermisosList);
            em.persist(permiso);
            if (menu != null) {
                menu.getPermisoList().add(permiso);
                menu = em.merge(menu);
            }
            for (PerfilesPermisos perfilesPermisosListPerfilesPermisos : permiso.getPerfilesPermisosList()) {
                Permiso oldPermiso1OfPerfilesPermisosListPerfilesPermisos = perfilesPermisosListPerfilesPermisos.getPermiso1();
                perfilesPermisosListPerfilesPermisos.setPermiso1(permiso);
                perfilesPermisosListPerfilesPermisos = em.merge(perfilesPermisosListPerfilesPermisos);
                if (oldPermiso1OfPerfilesPermisosListPerfilesPermisos != null) {
                    oldPermiso1OfPerfilesPermisosListPerfilesPermisos.getPerfilesPermisosList().remove(perfilesPermisosListPerfilesPermisos);
                    oldPermiso1OfPerfilesPermisosListPerfilesPermisos = em.merge(oldPermiso1OfPerfilesPermisosListPerfilesPermisos);
                }
            }
            for (UsuariosPermisos usuariosPermisosListUsuariosPermisos : permiso.getUsuariosPermisosList()) {
                Permiso oldPermiso1OfUsuariosPermisosListUsuariosPermisos = usuariosPermisosListUsuariosPermisos.getPermiso1();
                usuariosPermisosListUsuariosPermisos.setPermiso1(permiso);
                usuariosPermisosListUsuariosPermisos = em.merge(usuariosPermisosListUsuariosPermisos);
                if (oldPermiso1OfUsuariosPermisosListUsuariosPermisos != null) {
                    oldPermiso1OfUsuariosPermisosListUsuariosPermisos.getUsuariosPermisosList().remove(usuariosPermisosListUsuariosPermisos);
                    oldPermiso1OfUsuariosPermisosListUsuariosPermisos = em.merge(oldPermiso1OfUsuariosPermisosListUsuariosPermisos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPermiso(permiso.getId()) != null) {
                throw new PreexistingEntityException("Permiso " + permiso + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getId());
            Menu menuOld = persistentPermiso.getMenu();
            Menu menuNew = permiso.getMenu();
            List<PerfilesPermisos> perfilesPermisosListOld = persistentPermiso.getPerfilesPermisosList();
            List<PerfilesPermisos> perfilesPermisosListNew = permiso.getPerfilesPermisosList();
            List<UsuariosPermisos> usuariosPermisosListOld = persistentPermiso.getUsuariosPermisosList();
            List<UsuariosPermisos> usuariosPermisosListNew = permiso.getUsuariosPermisosList();
            List<String> illegalOrphanMessages = null;
            for (PerfilesPermisos perfilesPermisosListOldPerfilesPermisos : perfilesPermisosListOld) {
                if (!perfilesPermisosListNew.contains(perfilesPermisosListOldPerfilesPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PerfilesPermisos " + perfilesPermisosListOldPerfilesPermisos + " since its permiso1 field is not nullable.");
                }
            }
            for (UsuariosPermisos usuariosPermisosListOldUsuariosPermisos : usuariosPermisosListOld) {
                if (!usuariosPermisosListNew.contains(usuariosPermisosListOldUsuariosPermisos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuariosPermisos " + usuariosPermisosListOldUsuariosPermisos + " since its permiso1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (menuNew != null) {
                menuNew = em.getReference(menuNew.getClass(), menuNew.getId());
                permiso.setMenu(menuNew);
            }
            List<PerfilesPermisos> attachedPerfilesPermisosListNew = new ArrayList<PerfilesPermisos>();
            for (PerfilesPermisos perfilesPermisosListNewPerfilesPermisosToAttach : perfilesPermisosListNew) {
                perfilesPermisosListNewPerfilesPermisosToAttach = em.getReference(perfilesPermisosListNewPerfilesPermisosToAttach.getClass(), perfilesPermisosListNewPerfilesPermisosToAttach.getPerfilesPermisosPK());
                attachedPerfilesPermisosListNew.add(perfilesPermisosListNewPerfilesPermisosToAttach);
            }
            perfilesPermisosListNew = attachedPerfilesPermisosListNew;
            permiso.setPerfilesPermisosList(perfilesPermisosListNew);
            List<UsuariosPermisos> attachedUsuariosPermisosListNew = new ArrayList<UsuariosPermisos>();
            for (UsuariosPermisos usuariosPermisosListNewUsuariosPermisosToAttach : usuariosPermisosListNew) {
                usuariosPermisosListNewUsuariosPermisosToAttach = em.getReference(usuariosPermisosListNewUsuariosPermisosToAttach.getClass(), usuariosPermisosListNewUsuariosPermisosToAttach.getUsuariosPermisosPK());
                attachedUsuariosPermisosListNew.add(usuariosPermisosListNewUsuariosPermisosToAttach);
            }
            usuariosPermisosListNew = attachedUsuariosPermisosListNew;
            permiso.setUsuariosPermisosList(usuariosPermisosListNew);
            permiso = em.merge(permiso);
            if (menuOld != null && !menuOld.equals(menuNew)) {
                menuOld.getPermisoList().remove(permiso);
                menuOld = em.merge(menuOld);
            }
            if (menuNew != null && !menuNew.equals(menuOld)) {
                menuNew.getPermisoList().add(permiso);
                menuNew = em.merge(menuNew);
            }
            for (PerfilesPermisos perfilesPermisosListNewPerfilesPermisos : perfilesPermisosListNew) {
                if (!perfilesPermisosListOld.contains(perfilesPermisosListNewPerfilesPermisos)) {
                    Permiso oldPermiso1OfPerfilesPermisosListNewPerfilesPermisos = perfilesPermisosListNewPerfilesPermisos.getPermiso1();
                    perfilesPermisosListNewPerfilesPermisos.setPermiso1(permiso);
                    perfilesPermisosListNewPerfilesPermisos = em.merge(perfilesPermisosListNewPerfilesPermisos);
                    if (oldPermiso1OfPerfilesPermisosListNewPerfilesPermisos != null && !oldPermiso1OfPerfilesPermisosListNewPerfilesPermisos.equals(permiso)) {
                        oldPermiso1OfPerfilesPermisosListNewPerfilesPermisos.getPerfilesPermisosList().remove(perfilesPermisosListNewPerfilesPermisos);
                        oldPermiso1OfPerfilesPermisosListNewPerfilesPermisos = em.merge(oldPermiso1OfPerfilesPermisosListNewPerfilesPermisos);
                    }
                }
            }
            for (UsuariosPermisos usuariosPermisosListNewUsuariosPermisos : usuariosPermisosListNew) {
                if (!usuariosPermisosListOld.contains(usuariosPermisosListNewUsuariosPermisos)) {
                    Permiso oldPermiso1OfUsuariosPermisosListNewUsuariosPermisos = usuariosPermisosListNewUsuariosPermisos.getPermiso1();
                    usuariosPermisosListNewUsuariosPermisos.setPermiso1(permiso);
                    usuariosPermisosListNewUsuariosPermisos = em.merge(usuariosPermisosListNewUsuariosPermisos);
                    if (oldPermiso1OfUsuariosPermisosListNewUsuariosPermisos != null && !oldPermiso1OfUsuariosPermisosListNewUsuariosPermisos.equals(permiso)) {
                        oldPermiso1OfUsuariosPermisosListNewUsuariosPermisos.getUsuariosPermisosList().remove(usuariosPermisosListNewUsuariosPermisos);
                        oldPermiso1OfUsuariosPermisosListNewUsuariosPermisos = em.merge(oldPermiso1OfUsuariosPermisosListNewUsuariosPermisos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = permiso.getId();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PerfilesPermisos> perfilesPermisosListOrphanCheck = permiso.getPerfilesPermisosList();
            for (PerfilesPermisos perfilesPermisosListOrphanCheckPerfilesPermisos : perfilesPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permiso (" + permiso + ") cannot be destroyed since the PerfilesPermisos " + perfilesPermisosListOrphanCheckPerfilesPermisos + " in its perfilesPermisosList field has a non-nullable permiso1 field.");
            }
            List<UsuariosPermisos> usuariosPermisosListOrphanCheck = permiso.getUsuariosPermisosList();
            for (UsuariosPermisos usuariosPermisosListOrphanCheckUsuariosPermisos : usuariosPermisosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Permiso (" + permiso + ") cannot be destroyed since the UsuariosPermisos " + usuariosPermisosListOrphanCheckUsuariosPermisos + " in its usuariosPermisosList field has a non-nullable permiso1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Menu menu = permiso.getMenu();
            if (menu != null) {
                menu.getPermisoList().remove(permiso);
                menu = em.merge(menu);
            }
            em.remove(permiso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
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

    public Permiso findPermiso(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
