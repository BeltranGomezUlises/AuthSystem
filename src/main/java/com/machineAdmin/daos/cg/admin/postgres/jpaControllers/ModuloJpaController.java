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
import com.machineAdmin.entities.cg.admin.postgres.Seccion;
import com.machineAdmin.entities.cg.admin.postgres.Menu;
import com.machineAdmin.entities.cg.admin.postgres.Modulo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class ModuloJpaController implements Serializable {

    public ModuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Modulo modulo) throws PreexistingEntityException, Exception {
        if (modulo.getMenuList() == null) {
            modulo.setMenuList(new ArrayList<Menu>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion seccion = modulo.getSeccion();
            if (seccion != null) {
                seccion = em.getReference(seccion.getClass(), seccion.getId());
                modulo.setSeccion(seccion);
            }
            List<Menu> attachedMenuList = new ArrayList<Menu>();
            for (Menu menuListMenuToAttach : modulo.getMenuList()) {
                menuListMenuToAttach = em.getReference(menuListMenuToAttach.getClass(), menuListMenuToAttach.getId());
                attachedMenuList.add(menuListMenuToAttach);
            }
            modulo.setMenuList(attachedMenuList);
            em.persist(modulo);
            if (seccion != null) {
                seccion.getModuloList().add(modulo);
                seccion = em.merge(seccion);
            }
            for (Menu menuListMenu : modulo.getMenuList()) {
                Modulo oldModuloOfMenuListMenu = menuListMenu.getModulo();
                menuListMenu.setModulo(modulo);
                menuListMenu = em.merge(menuListMenu);
                if (oldModuloOfMenuListMenu != null) {
                    oldModuloOfMenuListMenu.getMenuList().remove(menuListMenu);
                    oldModuloOfMenuListMenu = em.merge(oldModuloOfMenuListMenu);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findModulo(modulo.getId()) != null) {
                throw new PreexistingEntityException("Modulo " + modulo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Modulo modulo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modulo persistentModulo = em.find(Modulo.class, modulo.getId());
            Seccion seccionOld = persistentModulo.getSeccion();
            Seccion seccionNew = modulo.getSeccion();
            List<Menu> menuListOld = persistentModulo.getMenuList();
            List<Menu> menuListNew = modulo.getMenuList();
            if (seccionNew != null) {
                seccionNew = em.getReference(seccionNew.getClass(), seccionNew.getId());
                modulo.setSeccion(seccionNew);
            }
            List<Menu> attachedMenuListNew = new ArrayList<Menu>();
            for (Menu menuListNewMenuToAttach : menuListNew) {
                menuListNewMenuToAttach = em.getReference(menuListNewMenuToAttach.getClass(), menuListNewMenuToAttach.getId());
                attachedMenuListNew.add(menuListNewMenuToAttach);
            }
            menuListNew = attachedMenuListNew;
            modulo.setMenuList(menuListNew);
            modulo = em.merge(modulo);
            if (seccionOld != null && !seccionOld.equals(seccionNew)) {
                seccionOld.getModuloList().remove(modulo);
                seccionOld = em.merge(seccionOld);
            }
            if (seccionNew != null && !seccionNew.equals(seccionOld)) {
                seccionNew.getModuloList().add(modulo);
                seccionNew = em.merge(seccionNew);
            }
            for (Menu menuListOldMenu : menuListOld) {
                if (!menuListNew.contains(menuListOldMenu)) {
                    menuListOldMenu.setModulo(null);
                    menuListOldMenu = em.merge(menuListOldMenu);
                }
            }
            for (Menu menuListNewMenu : menuListNew) {
                if (!menuListOld.contains(menuListNewMenu)) {
                    Modulo oldModuloOfMenuListNewMenu = menuListNewMenu.getModulo();
                    menuListNewMenu.setModulo(modulo);
                    menuListNewMenu = em.merge(menuListNewMenu);
                    if (oldModuloOfMenuListNewMenu != null && !oldModuloOfMenuListNewMenu.equals(modulo)) {
                        oldModuloOfMenuListNewMenu.getMenuList().remove(menuListNewMenu);
                        oldModuloOfMenuListNewMenu = em.merge(oldModuloOfMenuListNewMenu);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = modulo.getId();
                if (findModulo(id) == null) {
                    throw new NonexistentEntityException("The modulo with id " + id + " no longer exists.");
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
            Modulo modulo;
            try {
                modulo = em.getReference(Modulo.class, id);
                modulo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modulo with id " + id + " no longer exists.", enfe);
            }
            Seccion seccion = modulo.getSeccion();
            if (seccion != null) {
                seccion.getModuloList().remove(modulo);
                seccion = em.merge(seccion);
            }
            List<Menu> menuList = modulo.getMenuList();
            for (Menu menuListMenu : menuList) {
                menuListMenu.setModulo(null);
                menuListMenu = em.merge(menuListMenu);
            }
            em.remove(modulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Modulo> findModuloEntities() {
        return findModuloEntities(true, -1, -1);
    }

    public List<Modulo> findModuloEntities(int maxResults, int firstResult) {
        return findModuloEntities(false, maxResults, firstResult);
    }

    private List<Modulo> findModuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Modulo.class));
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

    public Modulo findModulo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Modulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getModuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modulo> rt = cq.from(Modulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
