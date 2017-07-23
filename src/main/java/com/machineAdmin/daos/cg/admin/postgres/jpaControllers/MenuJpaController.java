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
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.Modulo;
import com.machineAdmin.entities.cg.admin.postgres.Permiso;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class MenuJpaController implements Serializable {

    public MenuJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Menu menu) throws PreexistingEntityException, Exception {
        if (menu.getPermisoList() == null) {
            menu.setPermisoList(new ArrayList<Permiso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modulo modulo = menu.getModulo();
            if (modulo != null) {
                modulo = em.getReference(modulo.getClass(), modulo.getId());
                menu.setModulo(modulo);
            }
            List<Permiso> attachedPermisoList = new ArrayList<Permiso>();
            for (Permiso permisoListPermisoToAttach : menu.getPermisoList()) {
                permisoListPermisoToAttach = em.getReference(permisoListPermisoToAttach.getClass(), permisoListPermisoToAttach.getId());
                attachedPermisoList.add(permisoListPermisoToAttach);
            }
            menu.setPermisoList(attachedPermisoList);
            em.persist(menu);
            if (modulo != null) {
                modulo.getMenuList().add(menu);
                modulo = em.merge(modulo);
            }
            for (Permiso permisoListPermiso : menu.getPermisoList()) {
                Menu oldMenuOfPermisoListPermiso = permisoListPermiso.getMenu();
                permisoListPermiso.setMenu(menu);
                permisoListPermiso = em.merge(permisoListPermiso);
                if (oldMenuOfPermisoListPermiso != null) {
                    oldMenuOfPermisoListPermiso.getPermisoList().remove(permisoListPermiso);
                    oldMenuOfPermisoListPermiso = em.merge(oldMenuOfPermisoListPermiso);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMenu(menu.getId()) != null) {
                throw new PreexistingEntityException("Menu " + menu + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Menu menu) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Menu persistentMenu = em.find(Menu.class, menu.getId());
            Modulo moduloOld = persistentMenu.getModulo();
            Modulo moduloNew = menu.getModulo();
            List<Permiso> permisoListOld = persistentMenu.getPermisoList();
            List<Permiso> permisoListNew = menu.getPermisoList();
            if (moduloNew != null) {
                moduloNew = em.getReference(moduloNew.getClass(), moduloNew.getId());
                menu.setModulo(moduloNew);
            }
            List<Permiso> attachedPermisoListNew = new ArrayList<Permiso>();
            for (Permiso permisoListNewPermisoToAttach : permisoListNew) {
                permisoListNewPermisoToAttach = em.getReference(permisoListNewPermisoToAttach.getClass(), permisoListNewPermisoToAttach.getId());
                attachedPermisoListNew.add(permisoListNewPermisoToAttach);
            }
            permisoListNew = attachedPermisoListNew;
            menu.setPermisoList(permisoListNew);
            menu = em.merge(menu);
            if (moduloOld != null && !moduloOld.equals(moduloNew)) {
                moduloOld.getMenuList().remove(menu);
                moduloOld = em.merge(moduloOld);
            }
            if (moduloNew != null && !moduloNew.equals(moduloOld)) {
                moduloNew.getMenuList().add(menu);
                moduloNew = em.merge(moduloNew);
            }
            for (Permiso permisoListOldPermiso : permisoListOld) {
                if (!permisoListNew.contains(permisoListOldPermiso)) {
                    permisoListOldPermiso.setMenu(null);
                    permisoListOldPermiso = em.merge(permisoListOldPermiso);
                }
            }
            for (Permiso permisoListNewPermiso : permisoListNew) {
                if (!permisoListOld.contains(permisoListNewPermiso)) {
                    Menu oldMenuOfPermisoListNewPermiso = permisoListNewPermiso.getMenu();
                    permisoListNewPermiso.setMenu(menu);
                    permisoListNewPermiso = em.merge(permisoListNewPermiso);
                    if (oldMenuOfPermisoListNewPermiso != null && !oldMenuOfPermisoListNewPermiso.equals(menu)) {
                        oldMenuOfPermisoListNewPermiso.getPermisoList().remove(permisoListNewPermiso);
                        oldMenuOfPermisoListNewPermiso = em.merge(oldMenuOfPermisoListNewPermiso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = menu.getId();
                if (findMenu(id) == null) {
                    throw new NonexistentEntityException("The menu with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Menu menu;
            try {
                menu = em.getReference(Menu.class, id);
                menu.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The menu with id " + id + " no longer exists.", enfe);
            }
            Modulo modulo = menu.getModulo();
            if (modulo != null) {
                modulo.getMenuList().remove(menu);
                modulo = em.merge(modulo);
            }
            List<Permiso> permisoList = menu.getPermisoList();
            for (Permiso permisoListPermiso : permisoList) {
                permisoListPermiso.setMenu(null);
                permisoListPermiso = em.merge(permisoListPermiso);
            }
            em.remove(menu);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Menu> findMenuEntities() {
        return findMenuEntities(true, -1, -1);
    }

    public List<Menu> findMenuEntities(int maxResults, int firstResult) {
        return findMenuEntities(false, maxResults, firstResult);
    }

    private List<Menu> findMenuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Menu.class));
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

    public Menu findMenu(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Menu.class, id);
        } finally {
            em.close();
        }
    }

    public int getMenuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Menu> rt = cq.from(Menu.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
