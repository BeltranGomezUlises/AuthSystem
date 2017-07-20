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
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContras;
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContrasPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.machineAdmin.entities.cg.admin.postgres.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class BitacoraContrasJpaController implements Serializable {

    public BitacoraContrasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BitacoraContras bitacoraContras) throws PreexistingEntityException, Exception {
        if (bitacoraContras.getBitacoraContrasPK() == null) {
            bitacoraContras.setBitacoraContrasPK(new BitacoraContrasPK());
        }
        bitacoraContras.getBitacoraContrasPK().setUsuario(bitacoraContras.getUsuario1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario1 = bitacoraContras.getUsuario1();
            if (usuario1 != null) {
                usuario1 = em.getReference(usuario1.getClass(), usuario1.getId());
                bitacoraContras.setUsuario1(usuario1);
            }
            em.persist(bitacoraContras);
            if (usuario1 != null) {
                usuario1.getBitacoraContrasList().add(bitacoraContras);
                usuario1 = em.merge(usuario1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBitacoraContras(bitacoraContras.getBitacoraContrasPK()) != null) {
                throw new PreexistingEntityException("BitacoraContras " + bitacoraContras + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BitacoraContras bitacoraContras) throws NonexistentEntityException, Exception {
        bitacoraContras.getBitacoraContrasPK().setUsuario(bitacoraContras.getUsuario1().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BitacoraContras persistentBitacoraContras = em.find(BitacoraContras.class, bitacoraContras.getBitacoraContrasPK());
            Usuario usuario1Old = persistentBitacoraContras.getUsuario1();
            Usuario usuario1New = bitacoraContras.getUsuario1();
            if (usuario1New != null) {
                usuario1New = em.getReference(usuario1New.getClass(), usuario1New.getId());
                bitacoraContras.setUsuario1(usuario1New);
            }
            bitacoraContras = em.merge(bitacoraContras);
            if (usuario1Old != null && !usuario1Old.equals(usuario1New)) {
                usuario1Old.getBitacoraContrasList().remove(bitacoraContras);
                usuario1Old = em.merge(usuario1Old);
            }
            if (usuario1New != null && !usuario1New.equals(usuario1Old)) {
                usuario1New.getBitacoraContrasList().add(bitacoraContras);
                usuario1New = em.merge(usuario1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BitacoraContrasPK id = bitacoraContras.getBitacoraContrasPK();
                if (findBitacoraContras(id) == null) {
                    throw new NonexistentEntityException("The bitacoraContras with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BitacoraContrasPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BitacoraContras bitacoraContras;
            try {
                bitacoraContras = em.getReference(BitacoraContras.class, id);
                bitacoraContras.getBitacoraContrasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bitacoraContras with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario1 = bitacoraContras.getUsuario1();
            if (usuario1 != null) {
                usuario1.getBitacoraContrasList().remove(bitacoraContras);
                usuario1 = em.merge(usuario1);
            }
            em.remove(bitacoraContras);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BitacoraContras> findBitacoraContrasEntities() {
        return findBitacoraContrasEntities(true, -1, -1);
    }

    public List<BitacoraContras> findBitacoraContrasEntities(int maxResults, int firstResult) {
        return findBitacoraContrasEntities(false, maxResults, firstResult);
    }

    private List<BitacoraContras> findBitacoraContrasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BitacoraContras.class));
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

    public BitacoraContras findBitacoraContras(BitacoraContrasPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BitacoraContras.class, id);
        } finally {
            em.close();
        }
    }

    public int getBitacoraContrasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BitacoraContras> rt = cq.from(BitacoraContras.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
