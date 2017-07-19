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
import com.machineAdmin.entities.cg.admin.postgres.BitacoraContras;
import com.machineAdmin.entities.cg.admin.postgres.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Ulises Beltrán Gómez --- beltrangomezulises@gmail.com
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getBitacoraContrasList() == null) {
            usuarios.setBitacoraContrasList(new ArrayList<BitacoraContras>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<BitacoraContras> attachedBitacoraContrasList = new ArrayList<BitacoraContras>();
            for (BitacoraContras bitacoraContrasListBitacoraContrasToAttach : usuarios.getBitacoraContrasList()) {
                bitacoraContrasListBitacoraContrasToAttach = em.getReference(bitacoraContrasListBitacoraContrasToAttach.getClass(), bitacoraContrasListBitacoraContrasToAttach.getBitacoraContrasPK());
                attachedBitacoraContrasList.add(bitacoraContrasListBitacoraContrasToAttach);
            }
            usuarios.setBitacoraContrasList(attachedBitacoraContrasList);
            em.persist(usuarios);
            for (BitacoraContras bitacoraContrasListBitacoraContras : usuarios.getBitacoraContrasList()) {
                Usuarios oldUsuariosOfBitacoraContrasListBitacoraContras = bitacoraContrasListBitacoraContras.getUsuarios();
                bitacoraContrasListBitacoraContras.setUsuarios(usuarios);
                bitacoraContrasListBitacoraContras = em.merge(bitacoraContrasListBitacoraContras);
                if (oldUsuariosOfBitacoraContrasListBitacoraContras != null) {
                    oldUsuariosOfBitacoraContrasListBitacoraContras.getBitacoraContrasList().remove(bitacoraContrasListBitacoraContras);
                    oldUsuariosOfBitacoraContrasListBitacoraContras = em.merge(oldUsuariosOfBitacoraContrasListBitacoraContras);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getId()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            List<BitacoraContras> bitacoraContrasListOld = persistentUsuarios.getBitacoraContrasList();
            List<BitacoraContras> bitacoraContrasListNew = usuarios.getBitacoraContrasList();
            List<String> illegalOrphanMessages = null;
            for (BitacoraContras bitacoraContrasListOldBitacoraContras : bitacoraContrasListOld) {
                if (!bitacoraContrasListNew.contains(bitacoraContrasListOldBitacoraContras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BitacoraContras " + bitacoraContrasListOldBitacoraContras + " since its usuarios field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BitacoraContras> attachedBitacoraContrasListNew = new ArrayList<BitacoraContras>();
            for (BitacoraContras bitacoraContrasListNewBitacoraContrasToAttach : bitacoraContrasListNew) {
                bitacoraContrasListNewBitacoraContrasToAttach = em.getReference(bitacoraContrasListNewBitacoraContrasToAttach.getClass(), bitacoraContrasListNewBitacoraContrasToAttach.getBitacoraContrasPK());
                attachedBitacoraContrasListNew.add(bitacoraContrasListNewBitacoraContrasToAttach);
            }
            bitacoraContrasListNew = attachedBitacoraContrasListNew;
            usuarios.setBitacoraContrasList(bitacoraContrasListNew);
            usuarios = em.merge(usuarios);
            for (BitacoraContras bitacoraContrasListNewBitacoraContras : bitacoraContrasListNew) {
                if (!bitacoraContrasListOld.contains(bitacoraContrasListNewBitacoraContras)) {
                    Usuarios oldUsuariosOfBitacoraContrasListNewBitacoraContras = bitacoraContrasListNewBitacoraContras.getUsuarios();
                    bitacoraContrasListNewBitacoraContras.setUsuarios(usuarios);
                    bitacoraContrasListNewBitacoraContras = em.merge(bitacoraContrasListNewBitacoraContras);
                    if (oldUsuariosOfBitacoraContrasListNewBitacoraContras != null && !oldUsuariosOfBitacoraContrasListNewBitacoraContras.equals(usuarios)) {
                        oldUsuariosOfBitacoraContrasListNewBitacoraContras.getBitacoraContrasList().remove(bitacoraContrasListNewBitacoraContras);
                        oldUsuariosOfBitacoraContrasListNewBitacoraContras = em.merge(oldUsuariosOfBitacoraContrasListNewBitacoraContras);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BitacoraContras> bitacoraContrasListOrphanCheck = usuarios.getBitacoraContrasList();
            for (BitacoraContras bitacoraContrasListOrphanCheckBitacoraContras : bitacoraContrasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the BitacoraContras " + bitacoraContrasListOrphanCheckBitacoraContras + " in its bitacoraContrasList field has a non-nullable usuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
