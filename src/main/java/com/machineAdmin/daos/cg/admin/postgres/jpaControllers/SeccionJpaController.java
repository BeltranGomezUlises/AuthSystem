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
import com.machineAdmin.entities.cg.admin.postgres.Modulo;
import com.machineAdmin.entities.cg.admin.postgres.Seccion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class SeccionJpaController implements Serializable {

    public SeccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Seccion seccion) throws PreexistingEntityException, Exception {
        if (seccion.getModuloList() == null) {
            seccion.setModuloList(new ArrayList<Modulo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Modulo> attachedModuloList = new ArrayList<Modulo>();
            for (Modulo moduloListModuloToAttach : seccion.getModuloList()) {
                moduloListModuloToAttach = em.getReference(moduloListModuloToAttach.getClass(), moduloListModuloToAttach.getId());
                attachedModuloList.add(moduloListModuloToAttach);
            }
            seccion.setModuloList(attachedModuloList);
            em.persist(seccion);
            for (Modulo moduloListModulo : seccion.getModuloList()) {
                Seccion oldSeccionOfModuloListModulo = moduloListModulo.getSeccion();
                moduloListModulo.setSeccion(seccion);
                moduloListModulo = em.merge(moduloListModulo);
                if (oldSeccionOfModuloListModulo != null) {
                    oldSeccionOfModuloListModulo.getModuloList().remove(moduloListModulo);
                    oldSeccionOfModuloListModulo = em.merge(oldSeccionOfModuloListModulo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSeccion(seccion.getId()) != null) {
                throw new PreexistingEntityException("Seccion " + seccion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seccion seccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion persistentSeccion = em.find(Seccion.class, seccion.getId());
            List<Modulo> moduloListOld = persistentSeccion.getModuloList();
            List<Modulo> moduloListNew = seccion.getModuloList();
            List<Modulo> attachedModuloListNew = new ArrayList<Modulo>();
            for (Modulo moduloListNewModuloToAttach : moduloListNew) {
                moduloListNewModuloToAttach = em.getReference(moduloListNewModuloToAttach.getClass(), moduloListNewModuloToAttach.getId());
                attachedModuloListNew.add(moduloListNewModuloToAttach);
            }
            moduloListNew = attachedModuloListNew;
            seccion.setModuloList(moduloListNew);
            seccion = em.merge(seccion);
            for (Modulo moduloListOldModulo : moduloListOld) {
                if (!moduloListNew.contains(moduloListOldModulo)) {
                    moduloListOldModulo.setSeccion(null);
                    moduloListOldModulo = em.merge(moduloListOldModulo);
                }
            }
            for (Modulo moduloListNewModulo : moduloListNew) {
                if (!moduloListOld.contains(moduloListNewModulo)) {
                    Seccion oldSeccionOfModuloListNewModulo = moduloListNewModulo.getSeccion();
                    moduloListNewModulo.setSeccion(seccion);
                    moduloListNewModulo = em.merge(moduloListNewModulo);
                    if (oldSeccionOfModuloListNewModulo != null && !oldSeccionOfModuloListNewModulo.equals(seccion)) {
                        oldSeccionOfModuloListNewModulo.getModuloList().remove(moduloListNewModulo);
                        oldSeccionOfModuloListNewModulo = em.merge(oldSeccionOfModuloListNewModulo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = seccion.getId();
                if (findSeccion(id) == null) {
                    throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.");
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
            Seccion seccion;
            try {
                seccion = em.getReference(Seccion.class, id);
                seccion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.", enfe);
            }
            List<Modulo> moduloList = seccion.getModuloList();
            for (Modulo moduloListModulo : moduloList) {
                moduloListModulo.setSeccion(null);
                moduloListModulo = em.merge(moduloListModulo);
            }
            em.remove(seccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seccion> findSeccionEntities() {
        return findSeccionEntities(true, -1, -1);
    }

    public List<Seccion> findSeccionEntities(int maxResults, int firstResult) {
        return findSeccionEntities(false, maxResults, firstResult);
    }

    private List<Seccion> findSeccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seccion.class));
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

    public Seccion findSeccion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Seccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seccion> rt = cq.from(Seccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
